package com.chat.virtual.server;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import org.apache.commons.io.FilenameUtils;
import java.lang.Math;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private Image pfpImg; // profile picture

    private final String username = generateUsername(); // user's username

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
        HBox messageContainer = styleMessageContainer(false);

        Text msg = new Text(message);
        msg.setFill(Color.BLACK);

        messageContainer.getChildren().add(styleMessage(msg, false));

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
        public void sendMessage(){
            String messageToSend = messageField.getText(); // message entered

            if(!messageToSend.isEmpty()) { // ensure message to be sent isn't empty
                messageField.clear();

                Text message = new Text(messageToSend);
                message.setFill(Color.WHITE);

                /* Set and style users profile picture, username, and online status */
                Circle pfp = new Circle(15, Color.ALICEBLUE); // profile picture
                Circle userStatus = new Circle(4, Color.DARKOLIVEGREEN); // green when online, red when offline
                // setting username
                Text username = new Text(this.username);
                username.setFill(Color.WHITE);
                // userInfo stores user and user status and aligns them properly
                HBox userInfo = new HBox(username, userStatus);
                userInfo.setAlignment(Pos.TOP_RIGHT);
                HBox.setMargin(userStatus, new Insets(0,0,0,3));

                /* displaying messages with proper alignment */
                HBox textContainer = new HBox(); //controls the message and profile picture's horizontal alignment
                textContainer.getChildren().addAll(styleMessage(message, true), pfp);
                HBox.setMargin(pfp, new Insets(0, 0 ,0 ,5));
                VBox.setMargin(textContainer, new Insets(5, 0, 0, 0));

                /* display messages*/
                VBox profileMsg = new VBox(); //controls textContainer and the username's vertical alignment
                profileMsg.getChildren().addAll(userInfo,textContainer);
                profileMsg.setAlignment(Pos.BOTTOM_RIGHT);

                HBox messageContainer = styleMessageContainer(true);
                messageContainer.getChildren().add(profileMsg);
                messageDisplay.getChildren().add(messageContainer);
            }

            // send message
            server.sendMessageToClient(messageToSend);
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

    // allows a user to choose a valid image and display it as there profile picture
    public void uploadImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JFileChooser file_upload = new JFileChooser();

                // create file filter for only images
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", ImageIO.getReaderFileSuffixes());
                file_upload.setFileFilter(filter);

                int resVal = file_upload.showOpenDialog(null); // returns approved int if user selects a file

                if(resVal == JFileChooser.APPROVE_OPTION){ // user has uploaded a file
                    try {
                        FileInputStream file = new FileInputStream(file_upload.getSelectedFile().getAbsolutePath());
                        if(!accept(new File(file_upload.getSelectedFile().getAbsolutePath()))){ // if file extension is not a valid image extension
                            System.out.println("invalid");
                        }
                        else{
                            pfpImg = new Image(file);
                        }
                    } catch(FileNotFoundException fileNotFoundException){
                        System.out.println("There was an error opening this file, ensure it hasn't been deleted.");
                        fileNotFoundException.printStackTrace();
                    }
                }
            }
        }).start();
    }

    // returns true if a file ends with an image extensions
    public boolean accept(File f){
        String extension = FilenameUtils.getExtension(f.getName());

        for(int i = 0; i < ImageIO.getReaderFileSuffixes().length; i++){
            if(extension.equals(ImageIO.getReaderFileSuffixes()[i])){
                return true;
            }
        }
        return false;
    }

    // returns a random username
    public String generateUsername(){
        String username = "user";

        int max = 20000;
        int min = 10000;
        int range = max - min + 1;

        // generate random numbers from 20,000 to 10,000
        int rand = (int)(Math.random() * range) + min;
        username += Integer.toString(rand);

        return username;
    }
}