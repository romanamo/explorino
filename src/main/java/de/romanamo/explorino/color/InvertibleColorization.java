package de.romanamo.explorino.color;

public abstract class InvertibleColorization extends Colorization {

    protected boolean inverted;

    public InvertibleColorization() {
        this.inverted = false;
    }

    public InvertibleColorization(boolean inverted) {
        this.inverted = inverted;
    }

    public boolean getInverted() {
        return this.inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }
}
