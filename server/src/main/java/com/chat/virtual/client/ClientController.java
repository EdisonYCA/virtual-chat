package com.chat.virtual.client;

import com.chat.virtual.server.ServerController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
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
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    public static Image pfpImg = new Image(defProfileImg());
    @FXML
    private TextField messageField; // contains the message the user types in GUI
    @FXML
    private VBox messageDisplay; // Aligns all nodes vertically
    @FXML
    private ScrollPane scrollPane;  // responsible for allowing users to scroll through messages
    private Client client; // client object to init a connection
    public static String username = "User2"; // users username, initially contains "user1"
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        messageDisplay.setAlignment(Pos.BOTTOM_CENTER); // display all messages from bottom

        try {
            client = new Client(new Socket("localhost", 1234)); // initialize a new client object on port 1234
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Add a height listener to the main vbox and set that new height to the scroll pane*/
        messageDisplay.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number newHeight) {
                scrollPane.setVvalue((Double) newHeight);
            }
        });

        client.receiveMessageFromServer(messageDisplay);  // thread listening for any messages that are sent
    }

    /**
     * this method is responsible for the styling, sending, and displaying a message sent from the client user
     * */
    @FXML
    public void sendMessage(ActionEvent event){
        String messageToSend = messageField.getText(); // message entered

        if(!messageToSend.isEmpty()) { // ensure message to be sent isn't empty
            /* Store message from user in Text object */
            messageField.clear();
            Text message = new Text(messageToSend);
            message.setFill(Color.WHITE);

            /* Create a HBox container to store username */
            Text username = new Text(ClientController.username);
            username.setFill(Color.WHITE);
            HBox usernameHBox = new HBox(username);
            usernameHBox.setAlignment(Pos.TOP_RIGHT);

            /* Create a HBox container to store clients message and profile picture */
            Circle pfpImgContainer = new Circle(15);
            pfpImgContainer.setFill(new ImagePattern(pfpImg));
            HBox messageAndPfpHBox = new HBox();
            messageAndPfpHBox.setSpacing(5);
            messageAndPfpHBox.getChildren().addAll(styleMessage(message, true), pfpImgContainer);
            VBox.setMargin(pfpImgContainer, new Insets(5, 0, 0, 0));

            /* Create a VBox to align usernameHBox above messageAndPfpHBox */
            VBox alignUsernameAndMessageVBox = new VBox(); //controls textContainer and the username's vertical alignment
            alignUsernameAndMessageVBox.getChildren().addAll(usernameHBox, messageAndPfpHBox);
            alignUsernameAndMessageVBox.setAlignment(Pos.BOTTOM_RIGHT);

            /* Create HBox to horizontally align alignUsernameAndMessageVBox */
            HBox alignUsernameAndMessageHBox = styleSendContainer(true);
            alignUsernameAndMessageHBox.getChildren().add(alignUsernameAndMessageVBox);
            messageDisplay.getChildren().add(alignUsernameAndMessageHBox);
        }

        client.sendMessageToServer(messageToSend);
    }

    /**
     * this method is responsible for displaying and styling a message received from the server user
     * */
    public static void displayMessageFromServer(String message, VBox messageDisplay){
        /* Store message from user in Text object */
        Text msg = new Text(message);
        msg.setFill(Color.BLACK);

        /* Create a HBox container to store client username */
        Text clientUsername = new Text(ServerController.username);
        clientUsername.setFill(Color.WHITE);
        HBox usernameHBox = new HBox(clientUsername);
        usernameHBox.setAlignment(Pos.TOP_LEFT);

        /* Create a HBox container to store clients message and profile picture */
        Circle clientPfpImgContainer = new Circle(15);
        clientPfpImgContainer.setFill(new ImagePattern(ServerController.pfpImg));
        HBox messageAndPfpHBox = new HBox();
        messageAndPfpHBox.setSpacing(5);
        messageAndPfpHBox.getChildren().addAll(clientPfpImgContainer, styleMessage(msg, false));
        VBox.setMargin(clientPfpImgContainer, new Insets(5, 0, 0, 0));

        /* Create a VBox to align usernameHBox above messageAndPfpHBox */
        VBox alignUsernameAndMessageVBox = new VBox();
        alignUsernameAndMessageVBox.getChildren().addAll(usernameHBox, messageAndPfpHBox);
        alignUsernameAndMessageVBox.setAlignment(Pos.BOTTOM_RIGHT);

        /* Create HBox to horizontally align alignUsernameAndMessageVBox */
        HBox alignUsernameAndMessageHBox = styleSendContainer(false);
        alignUsernameAndMessageHBox.getChildren().add(alignUsernameAndMessageVBox);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messageDisplay.getChildren().add(alignUsernameAndMessageHBox); // add VBox storing message to messageDisplay
            }
        });
    }

    /**
     * this method is responsible for the styling of the HBox containing two HBoxes:
     * 1) The HBox containing the user's username and online status
     * 2) The HBox containing the user's profile picture and message
     * @return HBox with styling with a left, or right alignment
     * */
    public static HBox styleSendContainer(boolean send){
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


    @FXML
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


    public boolean accept(File f){
        String extension = FilenameUtils.getExtension(f.getName());

        for(int i = 0; i < ImageIO.getReaderFileSuffixes().length; i++){
            if(extension.equals(ImageIO.getReaderFileSuffixes()[i])){
                return true;
            }
        }
        return false;
    }

    /**
     * this method is responsible for setting the default profile image for the user
     * @return A FileInputStream containing the path of the default profile picture
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
}

