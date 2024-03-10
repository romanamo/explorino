package de.romanamo.explorino.calc;

import de.romanamo.explorino.Launcher;
import de.romanamo.explorino.eval.Evaluation;
import de.romanamo.explorino.eval.Evaluator;
import de.romanamo.explorino.math.Complex;
import de.romanamo.explorino.math.Numeric;
import de.romanamo.explorino.math.Point;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class PlaneFrame extends Plane {

    /**
     * Automated Thread pool time out.
     */
    private static final int TIME_OUT = 64;

    /**
     * Tile grid size.
     */
    private Point tileGridSize;

    /**
     * Optimization.
     */
    private boolean optimization = false;

    /**
     * Constructs a {@link PlaneFrame}.
     *
     * @param zoom         zoom
     * @param gridSize     grid size
     * @param planeSize    plane size
     * @param planeOffset  plane offset
     * @param tileGridSize tile grid size
     */
    public PlaneFrame(double zoom, Point gridSize, Complex planeSize, Complex planeOffset, Point tileGridSize) {
        super(zoom, gridSize, planeSize, planeOffset);
        this.tileGridSize = tileGridSize;
    }

    /**
     * Gets the tile raster.
     *
     * @return tile raster
     */
    private PlaneTile[][] getTileRaster() {
        //calculate size of the tile-raster
        int tileRasterHeight = (int) Numeric.ceilDiv(getGridSize().getY(), tileGridSize.getY());
        int tileRasterWidth = (int) Numeric.ceilDiv(getGridSize().getX(), tileGridSize.getX());

        //create the raster and fill it with tiles
        PlaneTile[][] tileRaster = new PlaneTile[tileRasterHeight][tileRasterWidth];
        for (int x = 0; x < tileRasterWidth; x++) {
            for (int y = 0; y < tileRasterHeight; y++) {
                //Detect if the tiles are "edge cases"
                boolean onHeightEdge = (y + 1) * tileGridSize.getY() > getGridSize().getY();
                boolean onWidthEdge = (x + 1) * tileGridSize.getX() > getGridSize().getX();

                //Set the Size of the Tile considering the special case that the tiles do not fit perfectly in
                int curHeight = onHeightEdge ? (getGridSize().getY() - y * tileGridSize.getY()) : tileGridSize.getY();
                int curWidth = onWidthEdge ? (getGridSize().getX() - x * tileGridSize.getX()) : tileGridSize.getX();

                Complex top = this.transformToPlane(new Point(x * tileGridSize.getX(), y * tileGridSize.getY()));

                Complex tileSize = this.getTileSize();

                double adjustedReal = tileSize.getReal() * curWidth;
                double adjustedImag = tileSize.getImag() * curHeight;

                Complex adjustedTileSize = Complex.ofCartesian(adjustedReal, adjustedImag);


                tileRaster[y][x] = new PlaneTile(new Point(curWidth, curHeight), adjustedTileSize, top);
            }
        }
        return tileRaster;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Grid compute(Evaluator evaluator) {
        long start = System.currentTimeMillis();

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        Grid grid = new Grid(getGridSize().getX(), getGridSize().getY());
        PlaneTile[][] tileRaster = this.getTileRaster();

        for (int y = 0; y < tileRaster.length; y++) {
            for (int x = 0; x < tileRaster[y].length; x++) {
                int fixedY = y;
                int fixedX = x;
                executor.submit(() -> {
                    PlaneTile tile = tileRaster[fixedY][fixedX];
                    Grid resultGrid = tile.compute(evaluator);

                    grid.insert(fixedX * this.tileGridSize.getX(), fixedY * this.tileGridSize.getY(), resultGrid);
                });
            }
        }
        executor.shutdown();
        try {
            boolean state = executor.awaitTermination(TIME_OUT, TimeUnit.SECONDS);
            if (!state) {
                Launcher.getLogger().warning("Failed Termination");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted");
        }
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;

        Launcher.getLogger().fine(String.format("Calculated Frame for %d ms", timeElapsed));
        return grid;
    }

    /**
     * Gets the optimization.
     *
     * @return optimization
     */
    public boolean getOptimization() {
        return this.optimization;
    }

    /**
     * Sets the optimization.
     *
     * @param optimization optimization
     */
    public void setOptimization(boolean optimization) {
        this.optimization = optimization;
    }

    /**
     * Class to represent a plane tile.
     */
    class PlaneTile implements Transformable, Computable {

        /**
         * Tile grid size.
         */
        private final Point tileGridSize;

        /**
         * Tile plane size.
         */
        private final Complex tilePlaneSize;

        /**
         * Tile origin.
         */
        private final Complex tileOrigin;

        /**
         * Constructs a plane tile.
         *
         * @param tileGridSize  tile grid size
         * @param tilePlaneSize tile plane size
         * @param tileOrigin    tile origin
         */
        PlaneTile(Point tileGridSize, Complex tilePlaneSize, Complex tileOrigin) {
            this.tileGridSize = tileGridSize;
            this.tilePlaneSize = tilePlaneSize;
            this.tileOrigin = tileOrigin;
        }

        @Override
        public Complex transformToPlane(Point point) {
            //calculate tile dimensions
            double tileWidth = this.tilePlaneSize.getReal() / this.tileGridSize.getX();
            double tileHeight = this.tilePlaneSize.getImag() / this.tileGridSize.getY();

            double real = this.tileOrigin.getReal() + point.getX() * tileWidth;
            double imag = this.tileOrigin.getImag() - point.getY() * tileHeight;

            return Complex.ofCartesian(real, imag);
        }

        @Override
        public Point transformToPoint(Complex complex) {
            throw new UnsupportedOperationException("Unsupported Operation");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Grid compute(Evaluator evaluator) {

            AtomicBoolean isConnected = new AtomicBoolean(optimization);
            Evaluation initialEvaluation = evaluator.evaluate(this.transformToPlane(new Point(0, 0)));

            Grid grid = new Grid(this.tileGridSize.getX(), this.tileGridSize.getY());

            Function<Point, Void> handle = point -> {
                Evaluation evaluation = evaluator.evaluate(this.transformToPlane(point));
                grid.setField(point.getX(), point.getY(), evaluation);
                //using 0 instead of initialEvaluation.getIteration() yields more exact results
                if (evaluation.getIteration() != initialEvaluation.getIteration()) {
                    isConnected.set(false);
                }
                return null;
            };

            //Check if the top and bottom row is inside the set
            for (int col = 0; col < this.tileGridSize.getX(); col++) {
                for (int row : new int[]{0, this.tileGridSize.getY() - 1}) {
                    handle.apply(new Point(col, row));
                }
            }
            //Check if the left and right column, without first and last rows, is inside the Set
            for (int row = 1; row < this.tileGridSize.getY() - 1; row++) {
                for (int col : new int[]{0, this.tileGridSize.getX() - 1}) {
                    handle.apply(new Point(col, row));
                }
            }

            //Calculate all entries, which were left out
            for (int x = 1; x < this.tileGridSize.getX() - 1; x++) {
                for (int y = 1; y < this.tileGridSize.getY() - 1; y++) {
                    if (isConnected.get()) {
                        //If set is connected set all fields to initial value

                        //construct correct evaluation
                        Evaluation actual = new Evaluation(
                                this.transformToPlane(new Point(x, y)),
                                initialEvaluation.getEnd(),
                                initialEvaluation.getIteration(),
                                initialEvaluation.getMaxIteration());

                        grid.setField(x, y, actual);
                    } else {
                        //Else do calculations one by one
                        grid.setField(x, y, evaluator.evaluate(this.transformToPlane(new Point(x, y))));
                    }
                }
            }
            return grid;
        }
    }

    /**
     * Gets the tile grid size.
     *
     * @return tile grid size
     */
    public Point getTileGridSize() {
        return tileGridSize;
    }

    /**
     * Sets the tile grid size.
     *
     * @param tileGridSize tile grid size
     */
    public void setTileGridSize(Point tileGridSize) {
        this.tileGridSize = tileGridSize;
    }
}
