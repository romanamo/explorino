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

        MenuBar menuBar = createMenuBar();

        Pane canvasPane = new Pane();


        root.setTop(menuBar);
        root.setCenter(canvasPane);

        state.isSideInfoProperty().addListener(o -> {
            if (state.isSideInfoProperty().get()) {
                root.setRight(createInfo());
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

    /**
     * Creates an export file chooser.
     *
     * @return file chooser
     */
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

    /**
     * Creates menu bar.
     *
     * @return menu bar
     */
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu menuFile = createFileMenu();
        Menu menuHelp = createHelpMenu();
        Menu menuView = createViewMenu();

        menuBar.getMenus().addAll(menuFile, menuView, menuHelp);
        return menuBar;
    }

    /**
     * Creates file menu.
     *
     * @return file menu
     */
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
            Colorization coloring = this.model.getColorization();

            File file = fileChooser.showSaveDialog(this.stage);

            if (file != null) {
                BufferedImage bufferedImage = Export.writeGridToImage(grid, coloring);
                Export.saveImageToFile(bufferedImage, file);
            }

        });

        menuFile.getItems().addAll(menuFileExport, menuFileSettings, menuFileSeparator, menuFileExit);

        return menuFile;
    }

    /**
     * Creates help menu.
     *
     * @return help menu
     */
    private Menu createHelpMenu() {
        Menu menuHelp = new Menu(I18n.getMessage("help"));

        MenuItem menuHelpLink = new MenuItem("Github");

        menuHelp.getItems().addAll(menuHelpLink);

        return menuHelp;
    }

    /**
     * Creates view menu.
     *
     * @return view menu
     */
    private Menu createViewMenu() {
        Menu menuView = new Menu(I18n.getMessage("view"));

        CheckMenuItem menuViewItem = new CheckMenuItem(I18n.getMessage("fractal"));

        menuViewItem.selectedProperty().bindBidirectional(state.isSideInfoProperty());
        menuView.getItems().addAll(menuViewItem);

        return menuView;
    }

    /**
     * Creates the side info panel.
     *
     * @return side info panel
     */
    private Region createInfo() {
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

    /**
     * Creates an information panel about the plane.
     *
     * @return plane panel
     */
    public Region createPlaneInfo() {
        GridPane grid = new GridPane();

        grid.getStyleClass().add("option");
        grid.setHgap(8);
        grid.setVgap(8);
        grid.getColumnConstraints().addAll(new ColumnConstraints(64));
        grid.setPadding(new Insets(8));

        double zoom = this.model.getPlane().getZoom();
        Complex offset = this.model.getPlane().getPlaneOffset();
        Point resolution = this.model.getPlane().getGridSize();

        Spinner<Double> zoomSpinner = new Spinner<>(-Double.MAX_VALUE, Double.MAX_VALUE, zoom);
        Spinner<Double> realSpinner = new Spinner<>(-Double.MAX_VALUE, Double.MAX_VALUE, offset.getReal());
        Spinner<Double> imagSpinner = new Spinner<>(-Double.MAX_VALUE, Double.MAX_VALUE, offset.getImag());

        Spinner<Integer> resWidthSpinner = new Spinner<>(1, 1000, resolution.getX(), 1);
        Spinner<Integer> resHeightSpinner = new Spinner<>(1, 1000, resolution.getY(), 1);

        resWidthSpinner.setEditable(true);
        resHeightSpinner.setEditable(true);

        this.state.infoChannelProperty().addListener((o, s1, s2) -> {
            Plane plane = this.model.getPlane();
            Complex offsets = plane.getPlaneOffset();

            zoomSpinner.getValueFactory().setValue(plane.getZoom());
            realSpinner.getValueFactory().setValue(offsets.getReal());
            imagSpinner.getValueFactory().setValue(offsets.getImag());
        });

        resHeightSpinner.valueProperty().addListener((o, s1, s2) -> {
            Plane plane = this.model.getPlane();
            Point updated = new Point(plane.getGridSize().getX(), s2);
            plane.setGridSize(updated);
            this.display.setImage(new WritableImage(updated.getX(), updated.getY()));
            this.state.updateDisplayChannel();
        });

        resWidthSpinner.valueProperty().addListener((o, s1, s2) -> {
            Plane plane = this.model.getPlane();
            Point updated = new Point(s2, plane.getGridSize().getY());
            plane.setGridSize(updated);
            this.display.setImage(new WritableImage(updated.getX(), updated.getY()));
            this.state.updateDisplayChannel();
        });

        grid.addRow(0, new Label(I18n.getMessage("zoom")), zoomSpinner);
        grid.addRow(1, new Label(I18n.getMessage("offset")), realSpinner, imagSpinner);
        grid.addRow(2, new Label(I18n.getMessage("resolution")), resWidthSpinner, resHeightSpinner);

        return grid;
    }

    /**
     * Creates an information panel about fractals.
     *
     * @return fractal panel
     */
    public Region createFractalInfo() {
        GridPane grid = new GridPane();

        grid.getStyleClass().addAll("option");
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(8));

        Slider iterationSlider = new Slider(0, 200, this.model.getEvaluator().getMaxIteration());
        CheckBox optimizationCheckBox = new CheckBox();

        optimizationCheckBox.setSelected(false);
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
        grid.addRow(2, new Label(I18n.getMessage("fractal")), createFractalChoiceBox());
        grid.addRow(3, new Label(I18n.getMessage("modifiers")), new Pane());

        return grid;
    }

    /**
     * Retrieves the matching fractal modifier.
     *
     * @param evaluator evaluator
     * @return fitting modifier
     */
    public Region retrieveFractalModifier(Evaluator evaluator) {
        if (evaluator instanceof MultiBrot) {
            return createMultiBrotModifier((MultiBrot) evaluator);
        } else if (evaluator instanceof MultiJulia) {
            return createMultiJuliaModifier((MultiJulia) evaluator);
        } else if (evaluator instanceof Newton) {
            return createNewtonModifier((Newton) evaluator);
        } else if (evaluator instanceof Lyapunov) {
            return createLyapunovModifier((Lyapunov) evaluator);
        }
        return new Pane();
    }

    /**
     * Retrieves the matching color modifier.
     *
     * @param colorization colorization
     * @return fitting modifier
     */
    public Region retrieveColorableModifier(Colorization colorization) {
        if (colorization instanceof InvertibleColorization) {
            return createInvertibleModifier((InvertibleColorization) colorization);
        }
        return new Pane();
    }

    /**
     * Creates a Modifier for {@link InvertibleColorization}.
     *
     * @param invertibleColorization invertible colorization
     * @return modifier
     */
    private Region createInvertibleModifier(InvertibleColorization invertibleColorization) {
        GridPane grid = new GridPane();

        grid.setHgap(8);
        grid.setVgap(8);

        CheckBox checkBox = new CheckBox();

        checkBox.setSelected(invertibleColorization.getInverted());

        checkBox.selectedProperty().addListener((o, s1, s2) -> {
            invertibleColorization.setInverted(s2);
            this.state.updateDisplayChannel();
        });

        grid.addRow(0, new Label(I18n.getMessage("inverted")), checkBox);
        return grid;
    }

    /**
     * Creates an information panel about colorization.
     *
     * @return color panel
     */
    public Region createColorInfo() {
        GridPane grid = new GridPane();

        grid.getStyleClass().addAll("option");
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(8));

        this.state.infoChannelProperty().addListener((o, s1, s2) -> {
            grid.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == 1 && GridPane.getRowIndex(node) == 1);
            grid.add(retrieveColorableModifier(this.model.getColorization()), 1, 1);
        });

        grid.addRow(0, new Label(I18n.getMessage("colorization")), createColorChoiceBox());
        grid.addRow(1, new Label(I18n.getMessage("modifiers")), new Pane());

        return grid;
    }

    /**
     * Creates a choice box for fractals.
     *
     * @return fractal choice box
     */
    public ChoiceBox<Evaluator> createFractalChoiceBox() {
        ChoiceBox<Evaluator> choiceBox = new ChoiceBox<>();

        choiceBox.setValue(this.model.getEvaluator());
        choiceBox.valueProperty().addListener((o, s1, s2) -> {
            s2.setMaxIteration(this.model.getEvaluator().getMaxIteration());
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

    /**
     * Creates a choice box for coloring methods.
     *
     * @return coloring choice box
     */
    public ChoiceBox<Colorization> createColorChoiceBox() {
        ChoiceBox<Colorization> coloringChoiceBox = new ChoiceBox<>();
        coloringChoiceBox.setValue(this.model.getColorization());

        coloringChoiceBox.setOnAction(e -> {
            Colorization selected = coloringChoiceBox.getValue();
            if (selected != null) {
                this.model.setColorization(selected);
            }
            this.state.updateDisplayChannel();
        });

        coloringChoiceBox.getItems().addAll(
                new AbsColorization(),
                new ArgColorization(false),
                new BasicColorization(),
                PaletteColorization.EXAMPLE
        );

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

    /**
     * Creates a Modifier for Lyapunov fractals.
     *
     * @param lyapunov lyapunov instance
     * @return modifier
     */
    public Region createLyapunovModifier(Lyapunov lyapunov) {
        GridPane grid = new GridPane();
        grid.getColumnConstraints().add(new ColumnConstraints(50));

        grid.setHgap(8);
        grid.setVgap(8);

        TextField sequenceField = new TextField(Lyapunov.indicesToSequence(lyapunov.getSequence()));

        sequenceField.setOnAction(event -> {
            lyapunov.setSequence(Lyapunov.sequenceToIndices(sequenceField.getText()));
            sequenceField.setText(sequenceField.getText());
            this.state.updateDisplayChannel();
        });

        grid.addRow(0, new Label(I18n.getMessage("sequence")), sequenceField);

        return grid;
    }

    /**
     * Creates a Modifier for Newton fractals.
     *
     * @param newton newton instance
     * @return modifier
     */
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

    /**
     * Creates a helper Modifier for Newton fractals.
     *
     * @param i      index
     * @param newton newton instance
     * @return modifier
     */
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
