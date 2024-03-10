package de.romanamo.explorino.core;

import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;

public class FractalController {

    /**
     * View builder.
     */
    private final Builder<Region> viewBuilder;

    /**
     * Constructs a {@link FractalController}.
     *
     * @param stage stage
     */
    public FractalController(Stage stage) {
        FractalModel fractalModel = new FractalModel();
        FractalState fractalState = new FractalState();

        FractalDisplay fractalDisplay = new FractalDisplay(fractalModel, fractalState);

        this.viewBuilder = new FractalView(fractalModel, fractalState, stage, fractalDisplay);
    }

    /**
     * Gets the view.
     *
     * @return view.
     */
    public Region getView() {
        return this.viewBuilder.build();
    }
}
