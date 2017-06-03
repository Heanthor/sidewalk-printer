package com.heanthor.printer.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author reedt
 */
public class ImageUtils {
    /**
     * @param i        RenderedImage to save, such as a BufferedImage
     * @param filename Name to give the file (sans extension) e.g. "image01"
     * @param format   Extension to give the file e.g. "png"
     * @return Success or failure of save operation
     */
    public static boolean saveToFile(RenderedImage i, String filename, String format) {
        if (format.contains(".")) {
            throw new IllegalArgumentException("Format string format incorrect");
        }

        if (filename.contains(".")) {
            throw new IllegalArgumentException("Filename format incorrect");
        }

        try {
            return ImageIO.write(i, format, new File(filename + "." + format));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public BufferedImage loadImageFromFile(String filePath) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("strawberry.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }
}
