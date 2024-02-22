package de.romanamo.explorino.calc;

import de.romanamo.explorino.eval.Evaluation;

public class Grid {

    private final int width;

    private final int height;

    protected final Evaluation[][] grid;

    public Grid(int width, int height) {
        if (Math.min(width, height) < 0) {
            throw new IllegalArgumentException(
                    String.format("Width: %d Height: %d have to be greater than zero", width, height));
        }
        this.width = width;
        this.height = height;

        this.grid = new Evaluation[height][width];
    }

    public Evaluation getField(int x, int y) {
        return this.grid[y][x];
    }

    public void setField(int x, int y, Evaluation content) {
        this.grid[y][x] = content;
    }

    public void insert(int xInsert, int yInsert, Grid grid) {
        //TODO do validity copy checks
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                this.grid[y + yInsert][x + xInsert] = grid.getField(x, y);
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                Evaluation field = this.getField(x,y);
                sb.append(field.getIteration() == field.getMaxIteration() ? "1" : "0");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
