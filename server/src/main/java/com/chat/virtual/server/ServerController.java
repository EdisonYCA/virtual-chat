package com.chat.virtual.server;

import com.chat.virtual.client.ClientController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
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
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    @FXML
    private TextField messageField; // contains the message the user types in GUI
    @FXML
    private VBox messageDisplay; // Aligns all nodes vertically
    private Server server; // server object to init a connection
    private static Image pfpImg = new Image(defProfileImg()); // users profile picture, initially contains the default picture
    private String username = "User1"; // users username, initially contains "user1"

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            server = new Server(new ServerSocket(1234)); // initialize a new server object on port 1234
        } catch(IOException io){
            io.printStackTrace();
        }

        server.receiveMessageFromClient(messageDisplay); // thread listening for any messages that are sent
    }

    /**
     * this method is responsible for displaying and styling a message received from the client user
     * */
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
        Circle clientPfpImgContainer = new Circle(15);
        clientPfpImgContainer.setFill(new ImagePattern(ClientController.pfpImg));
        messageAndPfpHBox.getChildren().addAll(clientPfpImgContainer, styleMessage(msg, false));

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

        /**
         * this method is responsible for the styling, sending, and displaying a message sent from the server user
         * */
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

    /**
     * this method is responsible for the styling of the HBox containing two HBoxes:
     * 1) The HBox containing the user's username and online status
     * 2) The HBox containing the user's profile picture and message
     * @return HBox with styling with a left, or right alignment
     * */
    public static HBox styleMessageContainer(boolean send){
        HBox hbox = new HBox();

        if(send) hbox.setAlignment(Pos.CENTER_RIGHT);
        else hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5, 5, 5, 5));

        return hbox;
    }

    /**
     * this method is responsible for the styling the message to be sent using a TextFlow object
     * @return TextFlow containing the message with styling
     * */
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
     * this method is called when the menu item "change profile picture" is clicked. It will allow the user to select a file to upload
     * as a profile picture, and validate that the file is a valid image.
     * */
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
     * this method is responsible for setting the default profile image for the user
     * @return A FileInputStream containing the path of the default profile picture
     * */
    private static FileInputStream defProfileImg() {
        FileInputStream profileImg = null;

        try {
            profileImg = new FileInputStream("C:\\Users\\joand\\IdeaProjects\\virtual-chat2\\server\\src\\main\\resources\\com.chat.virtual\\assets\\defaultPfpLogo.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return profileImg;
    }

    /**
     * this method is responsible for parsing a file for its extension.
     * @return true if the extension is an img, false otherwise.
     * */
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