package com.chat.virtual.server;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
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
    private Image pfpImg;

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

        // load font
        Font font = new Font("/com.chat.virtual/fonts/SourceSansPro-Regular.ttf", 12);

        TextFlow textFlow = new TextFlow(sentMessage);
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

                HBox textContainer = new HBox();//controls the message and profile picture's horizontal layout
                VBox profileMsg = new VBox();//controls the message and profile picture and username vertical layout

                Text sentMessage = new Text(messageToSend);
                sentMessage.setFill(Color.WHITE);

                //dummy data
                Circle pfp = new Circle(15,Color.RED);
                Circle userStatus = new Circle(4, Color.DARKOLIVEGREEN);
                Text username = new Text("Money Man");
                username.setFill(Color.GREEN);
                HBox userInfo = new HBox(username, userStatus);
                userInfo.setAlignment(Pos.TOP_RIGHT);
                HBox.setMargin(userStatus, new Insets(0,0,0,3));


                TextFlow textFlow = new TextFlow(sentMessage); // wrap message in text flow to add styling
                textFlow.setStyle("-fx-background-color: #7D52D9; " +
                        "-fx-background-radius: 20px;");
                textFlow.setPadding(new Insets(5, 10, 5, 10));
                textFlow.setTextAlignment(TextAlignment.CENTER);

                /*note: the order in which nodes are added matters*/

                //this will contain a Hbox that will manage the message and profile img horizontal position.
                textContainer.getChildren().addAll(textFlow, pfp);
                HBox.setMargin(pfp, new Insets(0, 0 ,0 ,5));
                VBox.setMargin(textContainer, new Insets(5, 0, 0, 0));

                //this will contain a Vbox that will manage (textContainer) and the username's vertical position.
                profileMsg.getChildren().addAll(userInfo,textContainer);

                profileMsg.setAlignment(Pos.BOTTOM_RIGHT);

                messageContainer.getChildren().add(profileMsg);
                messageDisplay.getChildren().add(messageContainer);
            }
            // sending message
            server.sendMessageToClient(messageToSend);
    }

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
}