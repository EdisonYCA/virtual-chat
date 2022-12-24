package com.example.virtual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Window extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Window.class.getResource("views.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 380);
        stage.setTitle("J&E Virtual Chat");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}