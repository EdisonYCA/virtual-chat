package com.chat.virtual.client;

import com.chat.virtual.server.ServerController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
    private final String username = ServerController.generateUsername(); // user's username
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
        String messageToSend = messageField.getText(); // message entered

        if(!messageToSend.isEmpty()) { // ensure message to be sent isn't empty
            messageField.clear();

            Text message = new Text(messageToSend);
            message.setFill(Color.WHITE);

            Circle userStatus = new Circle(4, Color.DARKOLIVEGREEN); // green when online, red when offline
            Text username = new Text(this.username); // needs func
            username.setFill(Color.WHITE);
            Text sentMessage = new Text(messageToSend);
            sentMessage.setFill(Color.WHITE);

            // userInfo stores user and user status and aligns them properly
            HBox userInfo = new HBox(username, userStatus);
            userInfo.setAlignment(Pos.TOP_RIGHT);
            HBox.setMargin(userStatus, new Insets(0,0,0,3));

            /* displaying messages with proper alignment */
            HBox textContainer = new HBox(); //controls the message and profile picture's horizontal alignment
            textContainer.getChildren().addAll(styleMessage(message, true), defProfileImg());
            VBox.setMargin(textContainer, new Insets(5, 0, 0, 0));

            /* display messages*/
            VBox profileMsg = new VBox(); //controls textContainer and the username's vertical alignment
            profileMsg.getChildren().addAll(userInfo, textContainer);
            profileMsg.setAlignment(Pos.BOTTOM_RIGHT);
            HBox messageContainer = styleMessageContainer(true);
            messageContainer.getChildren().add(profileMsg);
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

    // styles the message container
    public static HBox styleMessageContainer(boolean send){
        HBox hbox = new HBox(); // create HBox to store new message in

        if(send) hbox.setAlignment(Pos.CENTER_RIGHT);
        else hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5, 5, 5, 5));

        return hbox;
    }


    // style the message to be sent
    public static TextFlow styleMessage(Text msg, boolean send){
        TextFlow textFlow = new TextFlow(msg);

        if(send) {
            textFlow.setStyle("-fx-background-color: #7D52D9; " +
                    "-fx-background-radius: 20px;");
        } else {
            textFlow.setStyle("-fx-background-color: #e1e1e1; " +
                    "-fx-background-radius: 20px;");
        }

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        textFlow.setTextAlignment(TextAlignment.CENTER);

        return textFlow;
    }

    /**
     * this method is responsible for creating a default profile image for the user
     * @return A StackPane instance containing two Objects (Circle & Text)
     * */
    private StackPane defProfileImg(){
        Text text = new Text("U");
        text.setFill(Color.BLACK);
        StackPane stackPane = new StackPane(new Circle(15, Color.BEIGE),text);
        HBox.setMargin(stackPane, new Insets(0, 0, 0, 10));
        return stackPane;
    }



    @FXML
    void uploadImage(){

    }
}

