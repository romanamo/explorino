package de.romanamo.explorino.calc;

import de.romanamo.explorino.eval.Evaluator;
import de.romanamo.explorino.math.Complex;
import de.romanamo.explorino.math.Point;

import java.util.Objects;

public abstract class Plane implements Computable, Transformable {

    protected double zoom;
    protected Point gridSize;
    protected Complex planeOffset;
    protected Complex planeSize;

    public Plane(double zoom, Point gridSize, Complex planeSize, Complex planeOffset) {
        this.zoom = zoom;
        this.gridSize = gridSize;
        this.planeSize = planeSize;
        this.planeOffset = planeOffset;
    }

    public abstract Grid compute(Evaluator evaluator);

    @Override
    public Complex transformToPlane(Point coords) {
        double invertedZoom = 1 / this.zoom;

        Complex scaledPlaneSize = this.planeSize.multiply(invertedZoom);
        Complex scaledTileSize = this.getTileSize().multiply(invertedZoom);
        Complex planeOrigin = scaledPlaneSize.divide(2.0);

        //start at left side and add increment for each x
        double transformedX = -planeOrigin.getReal() + coords.x * scaledTileSize.getReal();

        //start at top side and add decrement for each x
        double transformedY = planeOrigin.getImag() - coords.y * scaledTileSize.getImag();

        return Complex.ofCartesian(transformedX, transformedY).add(this.planeOffset);
    }

    @Override
    public Point transformToPoint(Complex c) {
        throw new UnsupportedOperationException("plane to grid needs further specification");
    }

    public Complex getTileSize() {
        return Complex.ofCartesian(
                Math.abs(this.planeSize.getReal()) / (double) this.gridSize.x,
                Math.abs(this.planeSize.getImag()) / (double) this.gridSize.y);
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = Math.abs(zoom);
    }

    public Complex getPlaneOffset() {
        return planeOffset;
    }

    public Point getGridSize() {
        return gridSize;
    }

    public void setGridSize(Point gridSize) {
        this.gridSize = gridSize;
    }

    public void setPlaneOffset(Complex planeOffset) {
        this.planeOffset = planeOffset;
    }


    @Override
    public int hashCode() {
        return Objects.hash(zoom, gridSize, planeOffset, planeSize);
    }
}
