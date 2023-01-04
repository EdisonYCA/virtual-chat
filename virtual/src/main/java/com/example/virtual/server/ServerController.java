package com.example.virtual.server;

import com.example.virtual.server.Server;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    @FXML
    private Button sendButton; // sends message to GUI when clicked
    @FXML
    private TextField messageField; // contains message user wants to send
    @FXML
    private VBox messageDisplay; // aligns messages sent/received vertically GUI
    private Server server; // server

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { // allows manipulation of FXML widgets
        try{
            server = new Server(new ServerSocket(1234)); // initialize a new server object on port 1234
        } catch(IOException io){
            io.printStackTrace();
        }

        server.receiveMessageFromClient(messageDisplay);
    }

    // display message from client -> server
    public static void displayMessageFromClient(String message, VBox messageDisplay){
        HBox messageContainer = new HBox(); // create HBox to store new message in
        messageContainer.setAlignment(Pos.CENTER_LEFT);
        messageContainer.setPadding(new Insets(5,5,5,5));

        Text sentMessage = new Text(message);
        sentMessage.setFill(Color.BLACK);

        TextFlow textFlow = new TextFlow(sentMessage); // wrap message in text flow to add styling
        textFlow.setStyle("-fx-background-color: #D9D9D9; " +
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

        // send message from server -> client and display message on server GUI
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
                textFlow.setStyle("-fx-background-color: #2F8588; " +
                        "-fx-background-radius: 20px;");
                textFlow.setPadding(new Insets(5, 10, 5, 10));
                textFlow.setTextAlignment(TextAlignment.CENTER);

                messageContainer.getChildren().add(textFlow);
                messageDisplay.getChildren().add(messageContainer);
            }
            // sending message
            server.sendMessageToClient(messageToSend);
    }
}