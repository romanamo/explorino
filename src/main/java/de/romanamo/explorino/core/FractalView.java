package de.romanamo.explorino.core;

import de.romanamo.explorino.calc.Grid;
import de.romanamo.explorino.calc.Plane;
import de.romanamo.explorino.calc.PlaneFrame;
import de.romanamo.explorino.color.*;
import de.romanamo.explorino.core.model.Model;
import de.romanamo.explorino.core.model.State;
import de.romanamo.explorino.eval.*;
import de.romanamo.explorino.io.Export;
import de.romanamo.explorino.math.Complex;
import de.romanamo.explorino.math.Point;
import de.romanamo.explorino.math.Polynomial;
import de.romanamo.explorino.util.I18n;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Builder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FractalView implements Builder<Region> {

    private final Model model;

    private final Stage stage;

    private final State state;

    private final FractalDisplay display;

    public FractalView(Model model, State state, Stage stage, FractalDisplay display) {
        this.model = model;
        this.state = state;
        this.stage = stage;
        this.display = display;
    }

    @Override
    public Region build() {
        BorderPane root = new BorderPane();

        MenuBar menuBar = createMainMenuBar();

        Pane canvasPane = new Pane();


        root.setTop(menuBar);
        root.setCenter(canvasPane);

        state.isSideInfoProperty().addListener(o -> {
            if (state.isSideInfoProperty().get()) {
                root.setRight(createInfoDisplay());
            } else {
                root.setRight(null);
            }
        });

        canvasPane.getChildren().addAll(this.display);

        this.display.fitWidthProperty().bind(canvasPane.widthProperty());
        this.display.fitHeightProperty().bind(canvasPane.heightProperty());

        canvasPane.maxWidthProperty().bind(stage.widthProperty());

        root.getStylesheets().add("style.css");

        return root;
    }

    private FileChooser createExportFileChooser() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle(I18n.getMessage("export"));

        String fileTitle = I18n.getMessage("file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(String.format("PNG %s", fileTitle), "*.png"),
                new FileChooser.ExtensionFilter(String.format("JPG %s", fileTitle), "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter(String.format("TIFF %s", fileTitle), "*.tif, *.tiff")
        );

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

        //elements
        MenuItem menuFileSettings = new MenuItem(I18n.getMessage("settings"));
        MenuItem menuFileExport = new MenuItem(I18n.getMessage("export"));
        SeparatorMenuItem menuFileSeparator = new SeparatorMenuItem();
        MenuItem menuFileExit = new MenuItem(I18n.getMessage("exit"));

        //actions
        menuFileExit.setOnAction(e -> stage.close());

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

        menuFile.getItems().addAll(menuFileExport, menuFileSettings, menuFileSeparator, menuFileExit);

        return menuFile;
    }

    private Menu createHelpMenu() {
        Menu menuHelp = new Menu(I18n.getMessage("help"));

        MenuItem menuHelpLink = new MenuItem("Github");

        menuHelp.getItems().addAll(menuHelpLink);

        return menuHelp;
    }

    private Menu createViewMenu() {
        Menu menuView = new Menu(I18n.getMessage("view"));

        CheckMenuItem menuViewItem = new CheckMenuItem(I18n.getMessage("fractal"));

        menuViewItem.selectedProperty().bindBidirectional(state.isSideInfoProperty());
        menuView.getItems().addAll(menuViewItem);

        return menuView;
    }

    private Region createInfoDisplay() {
        VBox box = new VBox();

        box.setMaxWidth(400);
        box.setMinWidth(400);
        box.setFillWidth(true);
        box.setSpacing(4);
        box.setPadding(new Insets(4));

        box.getChildren().addAll(
                createPlaneInfo(),
                createFractalInfo(),
                createColorInfo()
        );

        box.getStyleClass().add("info");
        return box;
    }

    public Region createPlaneInfo() {
        GridPane grid = new GridPane();

        grid.getStyleClass().add("option");
        grid.setHgap(8);
        grid.setVgap(8);
        grid.getColumnConstraints().addAll(new ColumnConstraints(64));
        grid.setPadding(new Insets(8));

        Spinner<Double> zoomSpinner = new Spinner<>(0, Double.MAX_VALUE, 10);
        Spinner<Double> realSpinner = new Spinner<>(-Double.MAX_VALUE, Double.MAX_VALUE, 0.1);
        Spinner<Double> imagSpinner = new Spinner<>(-Double.MAX_VALUE, Double.MAX_VALUE, 0.1);

        Spinner<Integer> resWidthSpinner = new Spinner<>(1, 1000, 400, 1);
        Spinner<Integer> resHeightSpinner = new Spinner<>(1, 1000, 400, 1);

        resWidthSpinner.setEditable(true);
        resHeightSpinner.setEditable(true);

        this.state.infoChannelProperty().addListener((o, s1, s2) -> {
            Plane plane = this.model.getPlane();
            Complex offset = plane.getPlaneOffset();

            zoomSpinner.getValueFactory().setValue(plane.getZoom());
            realSpinner.getValueFactory().setValue(offset.getReal());
            imagSpinner.getValueFactory().setValue(offset.getImag());
        });

        resHeightSpinner.valueProperty().addListener((o, s1, s2) -> {
            Plane plane = this.model.getPlane();
            Point updated = new Point(plane.getGridSize().x, s2);
            plane.setGridSize(updated);
            this.display.setImage(new WritableImage(updated.x, updated.y));
            this.state.updateDisplayChannel();
        });

        resWidthSpinner.valueProperty().addListener((o, s1, s2) -> {
            Plane plane = this.model.getPlane();
            Point updated = new Point(s2, plane.getGridSize().y);
            plane.setGridSize(updated);
            this.display.setImage(new WritableImage(updated.x, updated.y));
            this.state.updateDisplayChannel();
        });

        grid.addRow(0, new Label(I18n.getMessage("zoom")), zoomSpinner);
        grid.addRow(1, new Label(I18n.getMessage("offset")), realSpinner, imagSpinner);
        grid.addRow(2, new Label(I18n.getMessage("resolution")), resWidthSpinner, resHeightSpinner);

        return grid;
    }

    public Region createFractalInfo() {
        GridPane grid = new GridPane();

        grid.getStyleClass().addAll("option");
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(8));

        Slider iterationSlider = new Slider(0, 200, 1);
        CheckBox optimizationCheckBox = new CheckBox();

        iterationSlider.setShowTickLabels(true);
        iterationSlider.setMaxWidth(Double.MAX_VALUE);
        iterationSlider.setValue(this.model.getEvaluator().getMaxIteration());

        iterationSlider.valueProperty().addListener((o, s1, s2) -> {
            this.model.getEvaluator().setMaxIteration(s2.intValue());
            state.updateDisplayChannel();
        });

        optimizationCheckBox.selectedProperty().addListener((o, s1, s2) -> {
            if (this.model.getPlane() instanceof PlaneFrame) {
                PlaneFrame planeFrame = (PlaneFrame) this.model.getPlane();
                planeFrame.setUseTileOptimize(s2);

                state.updateDisplayChannel();
            }
        });

        this.state.infoChannelProperty().addListener((o, s1, s2) -> {
            grid.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == 1 && GridPane.getRowIndex(node) == 3);
            grid.add(retrieveFractalModifier(this.model.getEvaluator()), 1, 3);
        });

        grid.addRow(0, new Label(I18n.getMessage("iterations")), iterationSlider);
        grid.addRow(1, new Label(I18n.getMessage("optimization")), optimizationCheckBox);
        grid.addRow(2, new Label(I18n.getMessage("fractal")), createFractalChoice());
        grid.addRow(3, new Label(I18n.getMessage("modifiers")), new Pane());

        return grid;
    }

    public Region retrieveFractalModifier(Evaluator evaluator) {
        if (evaluator instanceof MultiBrot) {
            return createMultiBrotModifier((MultiBrot) evaluator);
        } else if (evaluator instanceof MultiJulia) {
            return createMultiJuliaModifier((MultiJulia) evaluator);
        } else if (evaluator instanceof Newton) {
            return createNewtonModifier((Newton) evaluator);
        }
        return new Pane();
    }

    public Region createColorInfo() {
        GridPane grid = new GridPane();

        grid.getStyleClass().addAll("option");
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(8));

        grid.addRow(0, new Label(I18n.getMessage("colorization")), createColorChoiceBox());

        return grid;
    }

    public ChoiceBox<Evaluator> createFractalChoice() {
        ChoiceBox<Evaluator> choiceBox = new ChoiceBox<>();

        choiceBox.valueProperty().addListener((o, s1, s2) -> {
            this.model.setEvaluator(s2);
            this.state.updateInfoChannel();
            this.state.updateDisplayChannel();
        });

        choiceBox.getItems().addAll(
                new Mandelbrot(10),
                new MultiBrot(10, 2),
                new MultiJulia(10, 2, Complex.ofCartesian(0, 0)),
                new Newton(10, new Polynomial(-1, 0, 0, 1, 2)),
                new Lyapunov(10, new int[]{0, 1}));

        return choiceBox;
    }

    public Region createColorChoiceBox() {
        ChoiceBox<String> coloringChoiceBox = new ChoiceBox<>();

        Map<String, Colorable> coloringMap = new HashMap<>();

        coloringMap.put("AbsColorization", new AbsColorization());
        coloringMap.put("ArgColorization", new ArgColorization(false));
        coloringMap.put("BasicColorization", new BasicColorization());
        coloringMap.put("PaletteColorization", PaletteColorization.EXAMPLE);


        coloringChoiceBox.getItems().addAll(coloringMap.keySet());

        coloringChoiceBox.setOnAction(e -> {
            Colorable newColoring = coloringMap.get(coloringChoiceBox.getValue());

            if (newColoring != null) {
                this.model.setColorization(newColoring);
            }
            this.state.updateDisplayChannel();
        });
        return coloringChoiceBox;
    }

    /**
     * Creates a Modifier for Multi julia fractals.
     *
     * @param multiJulia multi julia instance
     * @return modifier
     */
    public Region createMultiJuliaModifier(MultiJulia multiJulia) {
        GridPane grid = new GridPane();

        grid.setHgap(8);
        grid.setVgap(8);

        Spinner<Integer> degreeSpinner = new Spinner<>(0, 32, multiJulia.getDegree());
        Spinner<Double> realSpinner = new Spinner<>(-2.0, 2.0, multiJulia.getParameter().getReal(), 0.01);
        Spinner<Double> imagSpinner = new Spinner<>(-2.0, 2.0, multiJulia.getParameter().getImag(), 0.01);

        degreeSpinner.valueProperty().addListener((o, s1, s2) -> {
            multiJulia.setDegree(s2);
            this.state.updateDisplayChannel();
        });

        realSpinner.valueProperty().addListener((o, s1, s2) -> {
            multiJulia.setParameter(multiJulia.getParameter().changeReal(s2));
            this.state.updateDisplayChannel();
        });

        imagSpinner.valueProperty().addListener((o, s1, s2) -> {
            multiJulia.setParameter(multiJulia.getParameter().changeImag(s2));
            this.state.updateDisplayChannel();
        });

        grid.addRow(0, new Label(I18n.getMessage("degree")), degreeSpinner);
        grid.addRow(1, new Label(I18n.getMessage("real")), realSpinner);
        grid.addRow(2, new Label(I18n.getMessage("imaginary")), imagSpinner);

        return grid;
    }

    /**
     * Creates a Modifier for Multi brot fractals.
     *
     * @param multiBrot multi brot instance
     * @return modifier
     */
    public Region createMultiBrotModifier(MultiBrot multiBrot) {
        GridPane grid = new GridPane();
        grid.getColumnConstraints().add(new ColumnConstraints(50));

        grid.setHgap(8);
        grid.setVgap(8);

        Spinner<Integer> degreeSpinner = new Spinner<>(0, 32, multiBrot.getDegree());
        degreeSpinner.setEditable(true);

        degreeSpinner.valueProperty().addListener((o, s1, s2) -> {
            multiBrot.setDegree(s2);
            this.state.updateDisplayChannel();
        });

        grid.addRow(0, new Label(I18n.getMessage("degree")), degreeSpinner);

        return grid;
    }

    public Region createNewtonModifier(Newton newton) {

        Polynomial polynomial = newton.getPolynom();
        GridPane grid = new GridPane();

        grid.setHgap(8);
        grid.setVgap(8);

        for (int i = 0; i < polynomial.getDegree(); i++) {
            Spinner<Double> coefficientSpinner = createCoefficientModifier(i, newton);

            grid.addRow(i,
                    new Label(String.format("%d. %s", i, I18n.getMessage("coefficient"))), coefficientSpinner);
        }

        return grid;
    }

    private Spinner<Double> createCoefficientModifier(int i, Newton newton) {
        Polynomial polynomial = newton.getPolynom();

        Spinner<Double> coefficientSpinner = new Spinner<>(-100.0, 100.0, polynomial.getCoefficients()[i], 1.0);
        coefficientSpinner.setEditable(true);

        coefficientSpinner.valueProperty().addListener((o, s1, s2) -> {
            Polynomial pol = newton.getPolynom();

            pol.getCoefficients()[i] = s2;
            newton.setDerivative(newton.getPolynom().derivate());

            this.state.updateDisplayChannel();
        });

        return coefficientSpinner;
    }

}
