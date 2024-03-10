package de.romanamo.explorino.core;

import de.romanamo.explorino.core.model.State;
import de.romanamo.explorino.core.model.Model;
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
        Model model = new Model();
        State state = new State();

        FractalDisplay fractalDisplay = new FractalDisplay(model, state);

        this.viewBuilder = new FractalView(model, state, stage, fractalDisplay);
    }

    /**
     * Gets the view.
     *
     * @return view.
     */
    public Region getView() {
        return this.viewBuilder.build();
    }

    //TODO put logic in controller
}
