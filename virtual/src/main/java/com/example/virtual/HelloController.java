package com.example.virtual;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private Label messageSent;
    @FXML
    private TextField messageField;

    @FXML
    public void sendMessage() {
        messageSent.setText(messageField.getText() + "\n");
        messageField.setText("");
    }
}