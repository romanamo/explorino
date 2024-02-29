package de.romanamo.explorino.io;

import de.romanamo.explorino.calc.Grid;
import de.romanamo.explorino.eval.Evaluation;
import de.romanamo.explorino.color.Colorable;
import de.romanamo.explorino.util.Log;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Export {

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf + 1);
    }

    public static void saveToImageFile(BufferedImage image, File file) {
        String fileExtension = getFileExtension(file);

        try {
            boolean successful = ImageIO.write(image, fileExtension, file);

            if (successful) {
                Log.LOGGER.info(String.format(
                        "Saved %s successfully to %s",
                        file.getName(),
                        file.getAbsolutePath()));
            } else {
                Log.LOGGER.warning(String.format(
                        "Unsupported file format: %s detected, unable to save %s to %s",
                        fileExtension,
                        file.getName(),
                        file.getAbsolutePath()));
            }
        } catch (IOException e) {
            Log.LOGGER.severe("Unable to create ImageOutputStream");
            throw new RuntimeException(e);
        }
    }


    public static BufferedImage gridToImage(Grid grid, Colorable coloring) {
        BufferedImage image = new BufferedImage(grid.getWidth(), grid.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getHeight(); j++) {
                Evaluation result = grid.getField(i, j);

                Color pixel = coloring.colorize(result);

                int red = (int) (pixel.getRed() * 255.0) & 0xFF;
                int green = (int) (pixel.getGreen() * 255.0) & 0xFF;
                int blue = (int) (pixel.getBlue() * 255.0) & 0xFF;
                int a = 0xFF;

                int color = a << 24 | red << 16 | green << 8 | blue;
                image.setRGB(i, j, color);
            }
        }
        return image;

    }
}
