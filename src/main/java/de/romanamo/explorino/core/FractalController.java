package de.romanamo.explorino.core;

import de.romanamo.explorino.core.model.State;
import de.romanamo.explorino.core.model.Model;
import de.romanamo.explorino.gui.FractalDisplay;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;

public class FractalController {

    private final Builder<Region> viewBuilder;

    public FractalController(Stage stage){
        Model model = new Model();
        State state = new State();

        FractalDisplay fractalDisplay = new FractalDisplay(model, state);

        this.viewBuilder = new FractalView(model, state, stage, fractalDisplay);
    }

    public Region getView() {
        return this.viewBuilder.build();
    }

    //TODO put logic in controller
}
