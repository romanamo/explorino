package de.romanamo.explorino.calc;

import de.romanamo.explorino.eval.Evaluation;

/**
 * Class to represent a bundled collection of evaluations
 * in accessible grid form.
 */
public class Grid {

    /**
     * Grid width.
     */
    private final int width;

    /**
     * Grid height.
     */
    private final int height;

    /**
     * Grid.
     */
    private final Evaluation[][] grid;

    /**
     * Constructs a grid.
     *
     * @param width  width > 0
     * @param height height > 0
     */
    public Grid(int width, int height) {
        if (Math.min(width, height) < 1) {
            throw new IllegalArgumentException(
                    String.format("Width: %d Height: %d have to be greater than zero", width, height));
        }
        this.width = width;
        this.height = height;
        this.grid = new Evaluation[height][width];
    }

    /**
     * Gets the evaluation at specified field indices.
     *
     * @param x x-index
     * @param y y-index
     * @return evaluation at specified field
     */
    public synchronized Evaluation getField(int x, int y) {
        return this.grid[y][x];
    }

    /**
     * Sets the evaluation at specified field indices.
     *
     * @param x       x-index
     * @param y       y-index
     * @param content evaluation
     */
    public synchronized void setField(int x, int y, Evaluation content) {
        this.grid[y][x] = content;
    }

    /**
     * Inserts a grid at specified insertion indices.
     *
     * @param xInsert x-insertion index
     * @param yInsert y-insertion index
     * @param grid    grid to insert
     */
    public synchronized void insert(int xInsert, int yInsert, Grid grid) {
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                this.grid[y + yInsert][x + xInsert] = grid.grid[y][x];
            }
        }
    }

    /**
     * Gets the grid height.
     *
     * @return grid height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the grid width.
     *
     * @return grid width
     */
    public int getWidth() {
        return width;
    }
}
