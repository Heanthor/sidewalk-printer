package com.heanthor.printer.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.*;
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

    public static BufferedImage loadImageFromFile(String filePath) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("strawberry.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public static BufferedImage resizeImage(BufferedImage i, int newWidth, int newHeight) {
        return toBufferedImage(i.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT));
    }

    /**
     * Approximate conversion from RGBA to CMYKA
     * @param in BufferedImage to convert
     * @return Float array of [row][col][cmyka] pixel data.
     */
    public static float[][][] rgbToCMYK(BufferedImage in) {
        int[][] imageInBytes = intArrayFromBufferedImage(in);

        float[][][] cmykImage = new float[imageInBytes.length][imageInBytes[0].length][5];

        for (int i = 0; i < imageInBytes.length; i++) {
            for (int j = 0; j < imageInBytes[i].length; j++) {
                int a = (imageInBytes[i][j] >> 24) & 0xff;
                int r = (imageInBytes[i][j] & (0xff << 16)) >> 16;
                int g = (imageInBytes[i][j] & (0xff << 8)) >> 8;
                int b = imageInBytes[i][j] & 0xff;

                try {
                    // for CMYK alpha
                    float alpha = (float) a / 255;
                    float[] cmyk = rgbToCmyk((float) r / 255, (float) g / 255, (float) b / 255, (float) a / 255);
                    System.out.println("RGBA: (" + r + ", " + g + ", " + b + ", " + a + ") " + "CMYK: (" + cmyk[0] + ", " + cmyk[1] + ", " + cmyk[2] + ", " + cmyk[3] + ")");

                    cmykImage[i][j] = new float[]{cmyk[0], cmyk[1], cmyk[2], cmyk[3], alpha};
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return cmykImage;
    }

    private static int[][] intArrayFromBufferedImage(BufferedImage image) {
        final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];

        if (hasAlphaChannel) {
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel++) {
                result[row][col] = pixels[pixel];
                col++;

                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel++) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += (pixels[pixel] & 0xff); // blue
                argb += ((pixels[pixel] >> 8) & 0xff) << 8; // green
                argb += ((pixels[pixel] >> 16) & 0xff) << 16; // red
                result[row][col] = argb;
                col++;

                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

    private static float[] rgbToCmyk(float... rgb) throws IOException {
        String path = "C:\\Users\\reedt\\Dropbox\\IntelliJ Git\\sidewalk-printer\\resources\\icc_profiles\\USWebCoatedSWOP.icc";

        if (rgb.length != 4) {
            throw new IllegalArgumentException("Need RGBA");
        }

        ColorSpace csrgb = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        //System.out.println("Min: " + csrgb.getMinValue(0) + ", max: " + csrgb.getMaxValue(0));
        float[] ciexyz = csrgb.toCIEXYZ(rgb);
        ColorSpace instance = new ICC_ColorSpace(ICC_Profile.getInstance(path));

        return instance.fromCIEXYZ(ciexyz);
    }
}
