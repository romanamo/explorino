package de.romanamo.explorino.core;

import de.romanamo.explorino.core.model.AppModel;
import de.romanamo.explorino.core.model.FractalModel;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;

public class FractalController {

    private final Builder<Region> viewBuilder;

    public FractalController(Stage stage){
        FractalModel model = new FractalModel();
        AppModel appModel = new AppModel();

        this.viewBuilder = new FractalView(model, appModel, stage);
    }

    public Region getView() {
        return this.viewBuilder.build();
    }

    //TODO put logic in controller
}
