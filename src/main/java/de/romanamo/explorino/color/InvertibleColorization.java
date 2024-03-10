package de.romanamo.explorino.color;

public abstract class InvertibleColorization extends Colorization {

    /**
     * Inverted.
     */
    private boolean inverted;

    /**
     * Constructs an Invertible Colorization.
     */
    public InvertibleColorization() {
        this.inverted = false;
    }

    /**
     * Constructs an Invertible Colorization.
     *
     * @param inverted inverted
     */
    public InvertibleColorization(boolean inverted) {
        this.inverted = inverted;
    }

    /**
     * Gets the inverted.
     *
     * @return inverted
     */
    public boolean getInverted() {
        return this.inverted;
    }

    /**
     * Sets the inverted.
     *
     * @param inverted inverted
     */
    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }
}
