package com.heanthor.printer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

/**
 * @author reedt
 */
public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws MalformedURLException {
        primaryStage.setTitle("Image display");
        StackPane root = new StackPane();

        String filePath = new File(getParameters().getRaw().get(0)).toURI().toURL().toString();

        Image img = new Image(filePath);
        ImageView iv = new ImageView(img);
        root.getChildren().add(iv);
        primaryStage.setScene(new Scene(root, 1920, 1080));
        primaryStage.show();
    }
}
