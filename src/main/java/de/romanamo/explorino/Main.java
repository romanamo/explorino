package de.romanamo.explorino;

import de.romanamo.explorino.core.FractalController;
import de.romanamo.explorino.util.I18n;
import de.romanamo.explorino.util.Log;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.logging.Level;

public class Main extends Application {


    @Override
    public void start(Stage stage) {
        //configure logging
        Log.LOGGER.setLevel(Level.FINEST);
        Log.LOGGER.info(String.format("Set Language to %s", Locale.getDefault().getDisplayLanguage()));

        FractalController controller = new FractalController(stage);
        Scene scene = new Scene(controller.getView());

        //configure stage
        stage.setTitle(I18n.getMessage("title"));
        stage.getIcons().add(new Image("logo.png"));
        stage.setScene(scene);
        stage.setMaximized(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}