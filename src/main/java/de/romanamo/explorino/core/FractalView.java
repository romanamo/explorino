package de.romanamo.explorino.core;

import de.romanamo.explorino.calc.Grid;
import de.romanamo.explorino.calc.PlaneFrame;
import de.romanamo.explorino.core.model.AppModel;
import de.romanamo.explorino.core.model.FractalModel;
import de.romanamo.explorino.eval.Evaluator;
import de.romanamo.explorino.eval.Mandelbrot;
import de.romanamo.explorino.eval.Newton;
import de.romanamo.explorino.eval.Polynomial;
import de.romanamo.explorino.gui.FractalDisplay;
import de.romanamo.explorino.io.colorize.*;
import de.romanamo.explorino.io.image.Export;
import de.romanamo.explorino.math.Complex;
import de.romanamo.explorino.math.Polynom;
import de.romanamo.explorino.util.I18n;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Builder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FractalView implements Builder<Region> {

    private final FractalModel model;

    private final Stage stage;

    private final AppModel appModel;

    public FractalView(FractalModel model, AppModel appModel, Stage stage) {
        this.model = model;
        this.appModel = appModel;
        this.stage = stage;
    }

    @Override
    public Region build() {
        BorderPane root = new BorderPane();

        MenuBar menuBar = createMainMenuBar();

        Pane canvasPane = new Pane();


        root.setTop(menuBar);
        root.setCenter(canvasPane);

        appModel.isSideInfoProperty().addListener(o -> {
            if (appModel.isSideInfoProperty().get()) {
                root.setRight(createInfoDisplay());
            } else {
                root.setRight(null);
            }
        });


        FractalDisplay fractalDisplay = new FractalDisplay(this.model, this.appModel);


        canvasPane.getChildren().addAll(fractalDisplay);

        fractalDisplay.fitWidthProperty().bind(canvasPane.widthProperty());
        fractalDisplay.fitHeightProperty().bind(canvasPane.heightProperty());

        canvasPane.maxWidthProperty().bind(stage.widthProperty());

        root.getStylesheets().add("style.css");

        return root;
    }

    private FileChooser createExportFileChooser() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle(I18n.getMessage("export"));

        FileChooser.ExtensionFilter extensionFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        FileChooser.ExtensionFilter extensionFilterJPG = new FileChooser.ExtensionFilter("JPEG files (*jpeg, *jpg)", "*.jpeg", "*.jpg");

        fileChooser.getExtensionFilters().addAll(extensionFilterPNG, extensionFilterJPG);

        return fileChooser;
    }

    private MenuBar createMainMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu menuFile = createFileMenu();
        Menu menuHelp = createHelpMenu();
        Menu menuView = createViewMenu();

        menuBar.getMenus().addAll(menuFile, menuView, menuHelp);
        return menuBar;
    }

    private Menu createFileMenu() {
        Menu menuFile = new Menu(I18n.getMessage("file"));
        MenuItem menuFileSettings = new MenuItem(I18n.getMessage("settings"));
        menuFileSettings.setOnAction(actionEvent -> openSettings());

        MenuItem menuFileExport = new MenuItem(I18n.getMessage("export"));
        menuFileExport.setOnAction(e -> {
            FileChooser fileChooser = createExportFileChooser();

            Evaluator evaluator = this.model.getEvaluator();
            Grid grid = this.model.getPlane().compute(evaluator);
            Colorable coloring = this.model.getColorization();

            File file = fileChooser.showSaveDialog(this.stage);

            if (file != null) {
                BufferedImage bufferedImage = Export.gridToImage(grid, coloring);
                Export.saveToImageFile(bufferedImage, file);
            }

        });
        SeparatorMenuItem menuFileSeparator = new SeparatorMenuItem();
        MenuItem menuFileExit = new MenuItem(I18n.getMessage("exit"));

        menuFile.getItems().addAll(menuFileExport, menuFileSettings, menuFileSeparator, menuFileExit);

        return menuFile;
    }

    private Menu createHelpMenu() {
        Menu menuHelp = new Menu(I18n.getMessage("help"));

        MenuItem menuHelpManual = new MenuItem(I18n.getMessage("manual"));
        ImageView helpView = new ImageView("help.png");

        helpView.setFitWidth(15);
        helpView.setFitHeight(15);

        menuHelpManual.setGraphic(helpView);
        MenuItem menuHelpVersion = new MenuItem(I18n.getMessage("version"));

        menuHelp.getItems().addAll(menuHelpManual, menuHelpVersion);

        return menuHelp;
    }

    private Menu createViewMenu() {
        Menu menuView = new Menu(I18n.getMessage("view"));

        CheckMenuItem menuViewItem = new CheckMenuItem(I18n.getMessage("fractal"));

        menuViewItem.selectedProperty().bindBidirectional(appModel.isSideInfoProperty());
        menuView.getItems().addAll(menuViewItem);

        return menuView;
    }

    private BorderPane createSetting() {
        BorderPane layout = new BorderPane();

        TreeItem<String> rootItem = new TreeItem<>("Inbox");
        rootItem.setExpanded(true);
        for (int i = 1; i < 6; i++) {
            TreeItem<String> item = new TreeItem<String>("Message" + i);
            rootItem.getChildren().add(item);
        }
        TreeView<String> tree = new TreeView<>(rootItem);

        layout.setLeft(tree);
        layout.getStylesheets().addAll("style.css");
        return layout;
    }

    private void openSettings() {
        BorderPane layout = createSetting();

        Stage stage = new Stage();
        stage.setTitle(I18n.getMessage("settings"));
        stage.setScene(new Scene(layout, 500, 500));

        stage.show();
    }

    private Pane createInfoDisplay() {
        GridPane gridPane = new GridPane();

        gridPane.setMaxWidth(400);
        gridPane.setMinWidth(400);

        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label zoomLabel = new Label();
        Label offsetLabel = new Label();

        gridPane.add(new Label(String.format("%s ", I18n.getMessage("zoom"))), 0, 0);
        gridPane.add(zoomLabel, 1, 0);

        gridPane.add(new Label(String.format("%s ", I18n.getMessage("offset"))), 0, 1);
        gridPane.add(offsetLabel, 1, 1);

        int maxIteration = this.model.getEvaluator().getMaxIteration();

        Label iterationLabel = new Label(Integer.toString(maxIteration));
        Slider iterationSlider = new Slider(0, 200, 1);
        iterationSlider.setShowTickLabels(true);
        iterationSlider.valueProperty().set(maxIteration);
        iterationLabel.setText(Integer.toString(maxIteration));

        CheckBox optimizationCheckBox = new CheckBox();

        if (this.model.getPlane() instanceof PlaneFrame) {
            PlaneFrame planeFrame = (PlaneFrame) this.model.getPlane();
            optimizationCheckBox.setSelected(planeFrame.getUseTileOptimize());
        }

        optimizationCheckBox.selectedProperty().addListener((o, s1, s2) -> {
            if (this.model.getPlane() instanceof PlaneFrame) {
                PlaneFrame planeFrame = (PlaneFrame) this.model.getPlane();
                planeFrame.setUseTileOptimize(s2);

                appModel.updateDisplayChannel();
            }
        });

        gridPane.addRow(2, new Label(I18n.getMessage("iterations")), iterationLabel, iterationSlider);
        gridPane.addRow(3, new Label(I18n.getMessage("fractal")), createFractalChooser());
        gridPane.addRow(4, new Label(I18n.getMessage("coloring")), createColorizationChoiceBox());
        gridPane.addRow(5, new Label("Optimization"), optimizationCheckBox );

        iterationSlider.valueProperty().addListener((o, s1, s2) -> {
            this.model.getEvaluator().setMaxIteration(s2.intValue());
            iterationLabel.setText(Integer.toString(s2.intValue()));

            appModel.updateDisplayChannel();
        });



        this.appModel.infoChannelProperty().addListener((o, s1, s2) -> {
            zoomLabel.setText(String.format("%.3e", this.model.getPlane().getZoom()));

            Complex offset = this.model.getPlane().getPlaneOffset();
            String offsetString = String.format("(%.3f; %.3f)", offset.getReal(), offset.getImaginary());

            offsetLabel.setText(offsetString);
        });




        gridPane.setPadding(new Insets(10, 10, 10, 10));
        return gridPane;
    }

    public ChoiceBox<String> createFractalChooser() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();

        choiceBox.getItems().add("Mandelbrot");
        choiceBox.getItems().add("Julia");
        choiceBox.getItems().add("Newton");

        choiceBox.setValue("Mandelbrot");

        choiceBox.setOnAction(e -> {
            Evaluator nextEvaluator = this.model.getEvaluator();
            int maxIteration = this.model.getEvaluator().getMaxIteration();

            switch (choiceBox.getValue()) {
                case "Mandelbrot":
                    nextEvaluator = new Mandelbrot(maxIteration);
                    break;
                case "Newton":
                    nextEvaluator = new Newton(maxIteration, Polynom.EXAMPLE);
                    break;
                case "Julia":
                    nextEvaluator = new Polynomial(maxIteration, 2, Complex.ofCartesian(-0.8, 0.156));
                    break;
                default:
            }
            this.model.setEvaluator(nextEvaluator);
            this.appModel.updateDisplayChannel();
        });

        return choiceBox;
    }

    public ChoiceBox<String> createColorizationChoiceBox() {
        Map<String, Colorable> coloringMap = new HashMap<>();

        coloringMap.put("AbsColorization", new AbsColorization());
        coloringMap.put("ArgColorization", new ArgColorization(false));
        coloringMap.put("BasicColorization", new BasicColorization());
        coloringMap.put("NewtonColorization", new NewtonColorization(Polynom.EXAMPLE));
        coloringMap.put("PaletteColorization", PaletteColorization.EXAMPLE);

        ChoiceBox<String> coloringChoiceBox = new ChoiceBox<>();

        coloringChoiceBox.getItems().addAll(coloringMap.keySet());

        coloringChoiceBox.setOnAction(e -> {
            Colorable newColoring = coloringMap.get(coloringChoiceBox.getValue());

            if (newColoring != null) {
                this.model.setColorization(newColoring);
            }
            this.appModel.updateDisplayChannel();
        });
        return coloringChoiceBox;
    }


}
