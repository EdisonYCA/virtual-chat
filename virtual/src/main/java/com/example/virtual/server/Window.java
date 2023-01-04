package com.example.virtual.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Window extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Window.class.getResource("server-gui.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("main.css")).toExternalForm());
        stage.setTitle("Server");
//        stage.getIcons().add(new Image(Objects.requireNonNull(Window.class.getResourceAsStream("logo.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}