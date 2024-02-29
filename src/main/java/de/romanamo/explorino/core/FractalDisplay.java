package de.romanamo.explorino.core;

import de.romanamo.explorino.calc.Grid;
import de.romanamo.explorino.core.model.State;
import de.romanamo.explorino.core.model.Model;
import de.romanamo.explorino.color.Colorable;
import de.romanamo.explorino.math.Complex;
import de.romanamo.explorino.util.Log;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;

import java.util.concurrent.atomic.AtomicLong;

public class FractalDisplay extends ImageView {

    private final AtomicLong lastDraw = new AtomicLong(0);

    private Complex lastDrag = Complex.ZERO;

    private final Model model;

    private final State state;

    public FractalDisplay(Model model, State appmodel) {
        super();
        this.model = model;
        this.state = appmodel;

        this.setImage(new WritableImage(450, 450));
        this.setSmooth(false);
        this.setCache(true);
        this.setFocusTraversable(true);

        this.fitWidthProperty().addListener(evt -> this.draw());
        this.fitHeightProperty().addListener(evt -> this.draw());

        this.setOnScroll(scrollEvent -> {
            double zoom = this.model.getPlane().getZoom();

            this.model.getPlane().setZoom(zoom + scrollEvent.getDeltaY() * 0.01 * zoom);

            this.draw();
        });

        this.state.displayChannelProperty().addListener(c -> this.draw());


        this.addEventFilter(KeyEvent.ANY, e -> {
            double distance = 0.2 / this.model.getPlane().getZoom();
            double x = this.model.getPlane().getPlaneOffset().getReal();
            double y = this.model.getPlane().getPlaneOffset().getImag();

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
                    this.model.getPlane().setZoom(this.model.getPlane().getZoom() + 0.1 * this.model.getPlane().getZoom());
                    break;
                case O:
                    this.model.getPlane().setZoom(this.model.getPlane().getZoom() - 0.1 * this.model.getPlane().getZoom());
                    break;
                case R: {
                    this.model.getPlane().setZoom(1.0);
                    this.model.getPlane().setPlaneOffset(Complex.ZERO);
                    this.draw();
                    return;
                }
            }
            this.model.getPlane().setPlaneOffset(Complex.ofCartesian(x, y));
            this.draw();
        });

        this.setOnMouseDragged(e -> {
            Complex dragVector = Complex.ofCartesian(e.getSceneX(), -e.getSceneY());
            Complex utilizedVector = lastDrag.subtract(dragVector).divide(1.9e2 * this.model.getPlane().getZoom());

            this.model.getPlane().setPlaneOffset(this.model.getPlane().getPlaneOffset().add(utilizedVector));
            this.lastDrag = dragVector;

            this.draw();
        });

        this.setOnMousePressed(e -> this.lastDrag = Complex.ofCartesian(e.getSceneX(), -e.getSceneY()));
    }

    public void draw() {
        long now = System.currentTimeMillis();

        if (now - this.lastDraw.get() <= 50) {
            Log.LOGGER.fine("Skipped Draw");
            return;
        }
        this.state.updateInfoChannel();

        double width = this.getImage().getWidth();
        double height = this.getImage().getHeight();

        int imageWidth = this.model.getPlane().getGridSize().x;
        int imageHeight = this.model.getPlane().getGridSize().y;

        WritableImage wi = (WritableImage) this.getImage();
        PixelWriter pw = wi.getPixelWriter();
        Grid grid = this.model.getPlane().compute(this.model.getEvaluator());

        for (int h = 0; h < (int) height; h++) {
            for (int w = 0; w < (int) width; w++) {
                int relX = (int) Math.round((w / width) * imageWidth);
                int relY = (int) Math.round((h / height) * imageHeight);
                relX = Math.min(relX, imageWidth - 1);
                relY = Math.min(relY, imageHeight - 1);

                Colorable colorable = this.model.getColorization();

                //set pixel
                pw.setColor(w, h, colorable.colorize(grid.getField(relX, relY)));
            }
        }

        this.lastDraw.set(System.currentTimeMillis());
    }


}
