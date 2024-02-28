package de.romanamo.explorino.calc;

import de.romanamo.explorino.eval.Evaluation;
import de.romanamo.explorino.math.Numeric;
import de.romanamo.explorino.math.Point;
import de.romanamo.explorino.util.Log;
import de.romanamo.explorino.eval.Evaluator;
import de.romanamo.explorino.math.Complex;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class PlaneFrame extends Plane {

    private Point tileGridSize;

    private boolean useTileOptimize = true;

    public PlaneFrame(double zoom, Point gridSize, Complex planeSize, Complex planeOffset, Point tileGridSize) {
        super(zoom, gridSize, planeSize, planeOffset);
        this.tileGridSize = tileGridSize;
    }

    private PlaneTile[][] getTileRaster() {
        //calculate size of the tile-raster
        int tileRasterHeight = (int) Numeric.ceilDiv(gridSize.y, tileGridSize.y);
        int tileRasterWidth = (int) Numeric.ceilDiv(gridSize.x, tileGridSize.x);

        //create the raster and fill it with tiles
        PlaneTile[][] tileRaster = new PlaneTile[tileRasterHeight][tileRasterWidth];
        for (int x = 0; x < tileRasterWidth; x++) {
            for (int y = 0; y < tileRasterHeight; y++) {
                //Detect if the tiles are "edge cases"
                boolean onHeightEdge = (y + 1) * tileGridSize.y > gridSize.y;
                boolean onWidthEdge = (x + 1) * tileGridSize.x > gridSize.x;

                //Set the Size of the Tile considering the special case that the tiles do not fit perfectly in
                int singleHeight = onHeightEdge ? (gridSize.y - y * tileGridSize.y) : tileGridSize.y;
                int singleWidth = onWidthEdge ? (gridSize.x - x * tileGridSize.x) : tileGridSize.x;

                Complex top = this.transformToPlane(new Point(x * tileGridSize.x, y * tileGridSize.y));

                Complex tileSize = this.getTileSize().divide(this.zoom);

                double adjustedReal = tileSize.getReal() * singleWidth;
                double adjustedImag = tileSize.getImag() * singleHeight;

                Complex adjustedTileSize = Complex.ofCartesian(adjustedReal, adjustedImag);


                tileRaster[y][x] = new PlaneTile(new Point(singleWidth, singleHeight), adjustedTileSize, top);
            }
        }
        return tileRaster;
    }

    @Override
    public Grid compute(Evaluator evaluator) {
        long start = System.currentTimeMillis();

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        Grid grid = new Grid(gridSize.x, gridSize.y);
        PlaneTile[][] tileRaster = this.getTileRaster();

        for (int y = 0; y < tileRaster.length; y++) {
            for (int x = 0; x < tileRaster[y].length; x++) {
                int fixedY = y;
                int fixedX = x;
                executor.submit(() -> {
                    PlaneTile tile = tileRaster[fixedY][fixedX];
                    Grid resultGrid = tile.compute(evaluator);

                    grid.insert(fixedX * this.tileGridSize.x, fixedY * this.tileGridSize.y, resultGrid);
                });
            }
        }
        executor.shutdown();
        try {
            boolean state = executor.awaitTermination(64, TimeUnit.SECONDS);
            if (!state) {
                Log.LOGGER.warning("Failed Termination");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted");
        }
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;

        Log.LOGGER.fine(String.format("Calculated Frame for %d ms", timeElapsed));
        return grid;
    }

    public boolean getUseTileOptimize() {
        return this.useTileOptimize;
    }

    public void setUseTileOptimize(boolean useTileOptimize) {
        this.useTileOptimize = useTileOptimize;
    }

    class PlaneTile implements Transformable, Computable {

        private final Point tileGridSize;
        private final Complex tilePlaneSize;
        private final Complex tileOrigin;

        public PlaneTile(Point tileGridSize, Complex tilePlaneSize, Complex tileOrigin) {
            this.tileGridSize = tileGridSize;
            this.tilePlaneSize = tilePlaneSize;
            this.tileOrigin = tileOrigin;
        }

        @Override
        public Complex transformToPlane(Point point) {
            //calculate tile dimensions
            double tileWidth = this.tilePlaneSize.getReal() / this.tileGridSize.x;
            double tileHeight = this.tilePlaneSize.getImag() / this.tileGridSize.y;

            double real = this.tileOrigin.getReal() + point.x * tileWidth;
            double imag = this.tileOrigin.getImag() - point.y * tileHeight;

            return Complex.ofCartesian(real, imag);
        }

        @Override
        public Point transformToPoint(Complex complex) {
            throw new UnsupportedOperationException("Unsupported Operation");
        }

        @Override
        public Grid compute(Evaluator evaluator) {

            AtomicBoolean isConnected = new AtomicBoolean(useTileOptimize);
            Evaluation initialEvaluation = evaluator.evaluate(this.transformToPlane(new Point(0, 0)));

            Grid grid = new Grid(this.tileGridSize.x, this.tileGridSize.y);

            Function<Point, Void> handle = point -> {
                Evaluation evaluation = evaluator.evaluate(this.transformToPlane(point));
                grid.setField(point.x, point.y, evaluation);
                //using 0 instead of initialEvaluation.getIteration() yields more exact results
                if (evaluation.getIteration() != initialEvaluation.getIteration()) {
                    isConnected.set(false);
                }
                return null;
            };

            //Check if the top and bottom row is inside the set
            for (int col = 0; col < this.tileGridSize.x; col++) {
                for (int row : new int[]{0, this.tileGridSize.y - 1}) {
                    handle.apply(new Point(col, row));
                }
            }
            //Check if the left and right column, without first and last rows, is inside the Set
            for (int row = 1; row < this.tileGridSize.y - 1; row++) {
                for (int col : new int[]{0, this.tileGridSize.x - 1}) {
                    handle.apply(new Point(col, row));
                }
            }

            //Calculate all entries, which were left out
            for (int x = 1; x < this.tileGridSize.x - 1; x++) {
                for (int y = 1; y < this.tileGridSize.y - 1; y++) {
                    if (isConnected.get()) {
                        //If set is connected set all fields to initial value

                        //construct correct evaluation
                        Evaluation actual = new Evaluation(
                                this.transformToPlane(new Point(x, y)),
                                initialEvaluation.getEnd(),
                                initialEvaluation.getIteration(),
                                initialEvaluation.getMaxIteration());

                        grid.setField(x, y, actual);
                        //TODO for now incorrect getEnd maybe interpolate between points
                    } else {
                        //Else do calculations one by one
                        grid.setField(x, y, evaluator.evaluate(this.transformToPlane(new Point(x, y))));
                    }
                }
            }
            return grid;
        }
    }
}
