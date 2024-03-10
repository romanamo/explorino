package de.romanamo.explorino.io;

import de.romanamo.explorino.Launcher;
import de.romanamo.explorino.calc.Grid;
import de.romanamo.explorino.color.Colorization;
import de.romanamo.explorino.eval.Evaluation;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class Export {

    private static final double RGB_SCALE = 255.0;

    private static final int MASK = 0xFF;

    private static final int BIT1 = 24;

    private static final int BIT2 = 16;

    private static final int BIT3 = 8;


    private Export() {

    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf + 1);
    }

    public static void saveImageToFile(BufferedImage image, File file) {
        String fileExtension = getFileExtension(file);

        try {
            boolean successful = ImageIO.write(image, fileExtension, file);

            if (successful) {
                Launcher.getLogger().info(String.format(
                        "Saved %s successfully to %s",
                        file.getName(),
                        file.getAbsolutePath()));
            } else {
                Launcher.getLogger().warning(String.format(
                        "Unsupported file format: %s detected, unable to save %s to %s",
                        fileExtension,
                        file.getName(),
                        file.getAbsolutePath()));
            }
        } catch (IOException e) {
            Launcher.getLogger().severe("Unable to create ImageOutputStream");
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage writeGridToImage(Grid grid, Colorization coloring) {
        BufferedImage image = new BufferedImage(grid.getWidth(), grid.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getHeight(); j++) {
                Evaluation result = grid.getField(i, j);

                Color pixel = coloring.colorize(result);

                int red = (int) (pixel.getRed() * RGB_SCALE) & MASK;
                int green = (int) (pixel.getGreen() * RGB_SCALE) & MASK;
                int blue = (int) (pixel.getBlue() * RGB_SCALE) & MASK;

                int color = MASK << BIT1 | red << BIT2 | green << BIT3 | blue;
                image.setRGB(i, j, color);
            }
        }
        return image;

    }
}
