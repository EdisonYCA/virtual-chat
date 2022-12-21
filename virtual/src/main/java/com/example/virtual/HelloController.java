package com.example.virtual;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private TextArea sentMessages;
    @FXML
    private TextField messageField;

    @FXML
    public void sendMessage() {
        String message;

        message = messageField.getText() + "\n";
        messageField.setText("");

        sentMessages.appendText(message);
    }
}