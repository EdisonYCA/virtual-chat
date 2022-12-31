package com.example.virtual;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
        VBox messageContainer = new VBox(); // create VBox to store new message in
        messageContainer.setAlignment(Pos.BOTTOM_RIGHT);
        messageContainer.getChildren().add(new Text(message));
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
                VBox messageContainer = new VBox(); // create VBox to store new message in
                messageContainer.setAlignment(Pos.BOTTOM_LEFT);
                messageContainer.getChildren().add(new Text(messageToSend));
                messageDisplay.getChildren().add(messageContainer); // add VBox storing message to messageDisplay
            }
            // sending message
            server.sendMessageToClient(messageToSend);
    }
}