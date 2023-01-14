package com.chat.virtual.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    private TextField messageField; // contains message user wants to send
    @FXML
    private VBox messageDisplay; // aligns messages sent/received vertically GUI

    private Client client;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        messageDisplay.setAlignment(Pos.BOTTOM_CENTER); // display all messages from bottom

        try {
            client = new Client(new Socket("localhost", 1234));
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.receiveMessageFromServer(messageDisplay);
    }

    // send message from client -> server and display message on client GUI
    @FXML
    public void sendMessage(ActionEvent event){
        // displaying message
        String messageToSend = messageField.getText();

        if(!messageToSend.isEmpty()) { // ensure message to be sent isn't empty
            messageField.clear();
            HBox messageContainer = new HBox(); // create HBox to store new message in
            messageContainer.setAlignment(Pos.CENTER_RIGHT);
            messageContainer.setPadding(new Insets(5, 5, 5, 5)); // padding

            Text sentMessage = new Text(messageToSend);
            sentMessage.setFill(Color.WHITE);

            TextFlow textFlow = new TextFlow(sentMessage); // wrap message in text flow to add styling
            textFlow.setStyle("-fx-background-color: #7D52D9; " +
                    "-fx-background-radius: 20px;");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            textFlow.setTextAlignment(TextAlignment.CENTER);

            messageContainer.getChildren().add(textFlow);
            messageDisplay.getChildren().add(messageContainer);
        }

        client.sendMessageToServer(messageToSend);
    }

    // display message from server -> client
    public static void displayMessageFromServer(String message, VBox messageDisplay){
        HBox messageContainer = new HBox(); // create HBox to store new message in
        messageContainer.setAlignment(Pos.CENTER_LEFT);
        messageContainer.setPadding(new Insets(5,5,5,5));

        Text sentMessage = new Text(message);
        sentMessage.setFill(Color.BLACK);

        TextFlow textFlow = new TextFlow(sentMessage); // wrap message in text flow to add styling
        textFlow.setStyle("-fx-background-color: #e1e1e1; " +
                "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        textFlow.setTextAlignment(TextAlignment.CENTER);

        messageContainer.getChildren().add(textFlow);
        // ensure application thread is modifying GUI
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messageDisplay.getChildren().add(messageContainer); // add VBox storing message to messageDisplay
            }
        });
    }
}

