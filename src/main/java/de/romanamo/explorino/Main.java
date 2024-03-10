package de.romanamo.explorino;

import de.romanamo.explorino.core.FractalController;
import de.romanamo.explorino.util.I18n;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.logging.Level;

public final class Main extends Application {


    @Override
    public void start(Stage stage) {
        //configure logging
        Launcher.getLogger().setLevel(Level.ALL);
        Launcher.getLogger().info(String.format("Set Language to %s", Locale.getDefault().getDisplayLanguage()));

        FractalController controller = new FractalController(stage);
        Scene scene = new Scene(controller.getView());

        //configure stage
        stage.setTitle(I18n.getMessage("title"));
        stage.getIcons().add(new Image("logo.png"));
        stage.setScene(scene);
        stage.setMaximized(true);

        stage.show();
    }

    /**
     * Main method.
     *
     * @param args arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}
