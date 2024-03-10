package de.romanamo.explorino.core;

import de.romanamo.explorino.Launcher;
import de.romanamo.explorino.calc.Grid;
import de.romanamo.explorino.color.Colorization;
import de.romanamo.explorino.math.Complex;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;

import java.util.concurrent.atomic.AtomicLong;

public class FractalDisplay extends ImageView {

    /**
     * Last Draw time stamp.
     */
    private final AtomicLong lastDraw = new AtomicLong(0);

    /**
     * Last drag.
     */
    private Complex lastDrag = Complex.ZERO;

    /**
     * Fractal model.
     */
    private final FractalModel fractalModel;

    /**
     * Fractal state.
     */
    private final FractalState fractalState;

    /**
     * Constructs a {@link FractalDisplay}.
     *
     * @param fractalModel fractalModel
     * @param fractalState fractalState
     */
    public FractalDisplay(FractalModel fractalModel, FractalState fractalState) {
        super();
        this.fractalModel = fractalModel;
        this.fractalState = fractalState;

        this.setImage(new WritableImage(450, 450));
        this.setSmooth(false);
        this.setCache(true);
        this.setFocusTraversable(true);

        this.fitWidthProperty().addListener(evt -> this.draw());
        this.fitHeightProperty().addListener(evt -> this.draw());

        this.setOnScroll(scrollEvent -> {
            double zoom = this.fractalModel.getPlane().getZoom();

            this.fractalModel.getPlane().setZoom(zoom + scrollEvent.getDeltaY() * 0.007 * zoom);

            this.draw();
        });

        this.fractalState.displayChannelProperty().addListener(c -> this.draw());


        this.addEventFilter(KeyEvent.ANY, e -> {
            double distance = 0.2 / this.fractalModel.getPlane().getZoom();
            double x = this.fractalModel.getPlane().getPlaneOffset().getReal();
            double y = this.fractalModel.getPlane().getPlaneOffset().getImag();

            switch (e.getCode()) {
                case W:
                    y += distance;
                    break;
                case S:
                    y -= distance;
                    break;
                case A:
                    x -= distance;
                    break;
                case D:
                    x += distance;
                    break;
                case I:
                    this.fractalModel.getPlane().setZoom(
                            this.fractalModel.getPlane().getZoom() + 0.1 * this.fractalModel.getPlane().getZoom());
                    break;
                case O:
                    this.fractalModel.getPlane().setZoom(
                            this.fractalModel.getPlane().getZoom() - 0.1 * this.fractalModel.getPlane().getZoom());
                    break;
                case R:
                    this.fractalModel.getPlane().setZoom(1.0);
                    this.fractalModel.getPlane().setPlaneOffset(Complex.ZERO);
                    this.draw();
                    break;
                default:
                    break;
            }
            this.fractalModel.getPlane().setPlaneOffset(Complex.ofCartesian(x, y));
            this.draw();
        });

        this.setOnMouseDragged(e -> {
            Complex dragVector = Complex.ofCartesian(e.getSceneX(), -e.getSceneY());
            Complex utilizedVector = lastDrag
                    .subtract(dragVector)
                    .divide(1.9e2 * this.fractalModel.getPlane().getZoom());

            this.fractalModel.getPlane().setPlaneOffset(
                    this.fractalModel.getPlane().getPlaneOffset().add(utilizedVector));
            this.lastDrag = dragVector;

            this.draw();
        });

        this.setOnMousePressed(e -> this.lastDrag = Complex.ofCartesian(e.getSceneX(), -e.getSceneY()));
    }

    /**
     * Draws the canvas.
     */
    public void draw() {
        long now = System.currentTimeMillis();

        if (now - this.lastDraw.get() <= 50) {
            Launcher.getLogger().fine("Skipped Draw");
            return;
        }
        this.fractalState.updateInfoChannel();

        double width = this.getImage().getWidth();
        double height = this.getImage().getHeight();

        int imageWidth = this.fractalModel.getPlane().getGridSize().getX();
        int imageHeight = this.fractalModel.getPlane().getGridSize().getY();

        WritableImage wi = (WritableImage) this.getImage();
        PixelWriter pw = wi.getPixelWriter();
        Grid grid = this.fractalModel.getPlane().compute(this.fractalModel.getEvaluator());

        for (int h = 0; h < (int) height; h++) {
            for (int w = 0; w < (int) width; w++) {
                int relX = (int) Math.round((w / width) * imageWidth);
                int relY = (int) Math.round((h / height) * imageHeight);
                relX = Math.min(relX, imageWidth - 1);
                relY = Math.min(relY, imageHeight - 1);

                Colorization colorization = this.fractalModel.getColorization();

                //set pixel
                pw.setColor(w, h, colorization.colorize(grid.getField(relX, relY)));
            }
        }

        this.lastDraw.set(System.currentTimeMillis());
    }


}
