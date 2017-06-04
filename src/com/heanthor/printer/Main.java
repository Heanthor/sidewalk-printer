package com.heanthor.printer;

import com.heanthor.printer.utils.ImageUtils;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;

/**
 * @author reedt
 */
public class Main extends Application {
    private static final int PRINTER_WIDTH = 24; // inches
    private static final int PRINTER_DPI = 4;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws MalformedURLException {
        primaryStage.setTitle("Image display");
        StackPane root = new StackPane();

        String filePath = new File(getParameters().getRaw().get(0)).toURI().toURL().toString();

        Image img = new Image(filePath);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(img, null);
        BufferedImage resizedImage = ImageUtils.resizeImage(bufferedImage, Main.PRINTER_WIDTH * Main.PRINTER_DPI, bufferedImage.getHeight());
        float[][][] cmykAry = ImageUtils.rgbToCMYK(resizedImage);

        ImageView iv = new ImageView(SwingFXUtils.toFXImage(resizedImage, null));
        root.getChildren().add(iv);
        primaryStage.setScene(new Scene(root, 1920, 1080));
        primaryStage.show();
    }
}
