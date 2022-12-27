package com.example.virtual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Window extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Window.class.getResource("views.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 380);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("main.css")).toExternalForm());
        stage.setTitle("J&E Virtual Chat");
        stage.getIcons().add(new Image(Objects.requireNonNull(Window.class.getResourceAsStream("logo.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void startWindow() {
        launch();
    }
}