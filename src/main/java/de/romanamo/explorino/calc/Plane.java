package de.romanamo.explorino.calc;

import de.romanamo.explorino.eval.Evaluator;
import de.romanamo.explorino.math.Complex;
import de.romanamo.explorino.math.Point;

/**
 * Class to represent a plane.
 */
public abstract class Plane implements Computable, Transformable {

    /**
     * Zoom.
     */
    private double zoom;

    /**
     * Grid size.
     */
    private Point gridSize;

    /**
     * Plane Offset.
     */
    private Complex planeOffset;

    /**
     * Plane Size.
     */
    private Complex planeSize;

    /**
     * Constructs a plane.
     *
     * @param zoom        zoom
     * @param gridSize    grid size
     * @param planeSize   plane size
     * @param planeOffset plane offset
     */
    public Plane(double zoom, Point gridSize, Complex planeSize, Complex planeOffset) {
        this.zoom = zoom;
        this.gridSize = gridSize;
        this.planeSize = planeSize;
        this.planeOffset = planeOffset;
    }

    /**
     * Computes a grid using specified evaluation.
     *
     * @param evaluator fractal
     * @return grid
     */
    public abstract Grid compute(Evaluator evaluator);

    /**
     * {@inheritDoc}
     */
    @Override
    public Complex transformToPlane(Point coords) {
        double invertedZoom = 1 / this.zoom;

        Complex scaledPlaneSize = this.planeSize.multiply(invertedZoom);
        Complex scaledTileSize = this.getTileSize();
        Complex planeOrigin = scaledPlaneSize.divide(2.0);

        //start at left side and add increment for each x
        double transformedX = -planeOrigin.getReal() + coords.getX() * scaledTileSize.getReal();

        //start at top side and add decrement for each x
        double transformedY = planeOrigin.getImag() - coords.getY() * scaledTileSize.getImag();

        return Complex.ofCartesian(transformedX, transformedY).add(this.planeOffset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point transformToPoint(Complex c) {
        throw new UnsupportedOperationException("Plane to grid needs further specification");
    }

    /**
     * Gets the scaled tle size.
     *
     * @return scaled tile size.
     */
    public Complex getTileSize() {
        return Complex.ofCartesian(
                        Math.abs(this.planeSize.getReal()) / (double) this.gridSize.getX(),
                        Math.abs(this.planeSize.getImag()) / (double) this.gridSize.getY())
                .divide(this.zoom);
    }

    /**
     * Gets the zoom.
     *
     * @return zoom
     */
    public double getZoom() {
        return zoom;
    }

    /**
     * Sets the zoom.
     *
     * @param zoom zoom
     */
    public void setZoom(double zoom) {
        this.zoom = Math.abs(zoom);
    }

    /**
     * Gets the plane offset.
     *
     * @return plane offset
     */
    public Complex getPlaneOffset() {
        return planeOffset;
    }

    /**
     * Sets the plane offset.
     *
     * @param planeOffset plane offset
     */
    public void setPlaneOffset(Complex planeOffset) {
        this.planeOffset = planeOffset;
    }

    /**
     * Gets the grid size.
     *
     * @return grid size
     */
    public Point getGridSize() {
        return gridSize;
    }

    /**
     * Sets the grid size.
     *
     * @param gridSize grid size
     */
    public void setGridSize(Point gridSize) {
        this.gridSize = gridSize;
    }

    /**
     * Gets the plane size.
     *
     * @return plane size
     */
    public Complex getPlaneSize() {
        return planeSize;
    }

    /**
     * Sets the plane size.
     *
     * @param planeSize plane size
     */
    public void setPlaneSize(Complex planeSize) {
        this.planeSize = planeSize;
    }
}
