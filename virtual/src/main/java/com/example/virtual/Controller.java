package com.example.virtual;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class HelloController {
    @FXML
    private VBox messageDisplay;
    @FXML
    private TextField messageField;

    @FXML
    public void sendMessage() {
        String message;

        message = messageField.getText() + "\n";
        messageField.setText("");

        if (message.equals("sent\n")) {
            VBox sentMessage = new VBox();
            sentMessage.setAlignment(Pos.BOTTOM_LEFT);
            messageDisplay.setAlignment(Pos.BOTTOM_LEFT);
            sentMessage.getChildren().add(new Text(message));
            messageDisplay.getChildren().add(sentMessage);
        } else if (message.equals("receive\n")) {
            VBox receiveMessage = new VBox();
            receiveMessage.setAlignment(Pos.BOTTOM_RIGHT);
            messageDisplay.setAlignment(Pos.BOTTOM_RIGHT);
            receiveMessage.getChildren().add(new Text(message));
            messageDisplay.getChildren().add(receiveMessage);
        }
    }

    @FXML
    public void sendMessageKey(ActionEvent event){
        sendMessage();
    }
}