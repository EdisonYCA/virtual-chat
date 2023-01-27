package com.chat.virtual.server;

import com.chat.virtual.client.ClientController;
import javafx.application.Platform;
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
import java.util.Random;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    @FXML
    private TextField messageField; // contains message user wants to send
    @FXML
    private VBox messageDisplay; // aligns messages sent/received vertically GUI
    private Server server; // server
    private static Image pfpImg = new Image(defProfileImg());

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
        /* Store message from user in Text object */
        Text msg = new Text(message);
        msg.setFill(Color.BLACK);

        /* Create a HBox container to store client username & their online activity */
        Circle userStatus = new Circle(4, Color.DARKOLIVEGREEN);
        Text clientUsername = new Text(ClientController.username);
        clientUsername.setFill(Color.WHITE);
        HBox usernameAndActivityHBox = new HBox(3, clientUsername, userStatus);
        usernameAndActivityHBox.setAlignment(Pos.TOP_RIGHT);
        HBox.setMargin(userStatus, new Insets(0,10,0, 0));
        HBox.setMargin(clientUsername, new Insets(0,10,0,0));

        /* Create a HBox container to store clients message and profile picture */
        HBox messageAndPfpHBox = new HBox();
        messageAndPfpHBox.setSpacing(10);
        messageAndPfpHBox.setAlignment(Pos.TOP_RIGHT);
//        messageAndPfpHBox.getChildren().addAll(defProfileImg(), styleMessage(msg, false));

        /* Create a VBox to align usernameAndActivityHBox above messageAndPfpHBox */
        VBox alignUsernameAndMessageVBox = new VBox();
        alignUsernameAndMessageVBox.getChildren().addAll(usernameAndActivityHBox, messageAndPfpHBox);
        alignUsernameAndMessageVBox.setAlignment(Pos.BOTTOM_LEFT);

        /* Create HBox to horizontally align messageAndPfpHBox & usernameAndActivityHBox stored in alignUsernameAndMessageVBox */
        HBox alignUsernameAndMessageHBox = styleMessageContainer(false);
        alignUsernameAndMessageHBox.getChildren().add(alignUsernameAndMessageVBox);

        /* add to main GUI VBox */
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messageDisplay.getChildren().add(alignUsernameAndMessageHBox);
            }
        });
    }

    private Color randomColor() {
        Random rand = new Random();

        int r = rand.nextInt(0, 255);
        int g = rand.nextInt(0, 255);
        int b = rand.nextInt(0, 255);

        return Color.rgb(r,g,b);
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

                Circle userStatus = new Circle(4, Color.DARKOLIVEGREEN); // green when online, red when offline
                // setting username
                Text username = new Text(this.username);
                username.setFill(Color.WHITE);
                Text sentMessage = new Text(messageToSend);
                sentMessage.setFill(Color.WHITE);

                // userInfo stores username and user status and aligns them properly
                HBox userInfo = new HBox(username, userStatus);
                userInfo.setAlignment(Pos.TOP_RIGHT);
                HBox.setMargin(userStatus, new Insets(0,0,0,3));

                /* displaying messages with proper alignment */
                Circle pfpImgContainer = new Circle(15);
                pfpImgContainer.setFill(new ImagePattern(pfpImg));
                HBox textContainer = new HBox(); //controls the message and profile picture's horizontal alignment
                textContainer.setSpacing(5);
                textContainer.getChildren().addAll(styleMessage(message, true), pfpImgContainer);
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

    /**
     * this method is responsible for creating a default profile image for the user
     * @return A StackPane instance containing two Objects (Circle & Text)
     * */
    private static FileInputStream defProfileImg() {
        FileInputStream profileImg = null;

        try {
            profileImg = new FileInputStream("C:\\Users\\Ediso\\IdeaProjects\\virtual-chat\\server\\src\\main\\resources\\com.chat.virtual\\assets\\defaultPfpLogo.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return profileImg;
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
    public static String generateUsername(){
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