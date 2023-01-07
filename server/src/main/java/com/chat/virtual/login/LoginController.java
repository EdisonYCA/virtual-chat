package com.chat.virtual.login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.jar.JarFile;

import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private Circle profilePictureContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    // returns TRUE is username is valid or FALSE if username is invalid
    public boolean validateUsername(){
        String username = usernameField.getText();

        // check length of username
        if(!(username.length() >= 6) || !(username.length() <= 10)){
            invalidUsernameAnimation();
            return false;
        }
        else{
            if(isAlphanumeric(username)){
                usernameField.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-border-color: green");
                return true;
            }
            else{
                invalidUsernameAnimation();
                return false;
            }
        }
    }

    // returns true all characters of a string s are alphanumeric
    public boolean isAlphanumeric(String s){
        for(int i = 0; i < s.length(); i++){
            if(!Character.isLetterOrDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // create animate and change border color to red for invalid username
    public void invalidUsernameAnimation(){
        usernameField.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-border-color: #B21807");
        // add shake animation
    }

    // allows user to upload an image as a profile picture
    public void uploadImage(){
        JFileChooser file_upload = new JFileChooser();

        // create file filter for only images
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpeg", "jpg", "gif", "bmp", "png");
        file_upload.setFileFilter(filter);

        int resVal = file_upload.showOpenDialog(null); // returns approved int if user selects a file

        if(resVal == JFileChooser.APPROVE_OPTION){ // user has uploaded a file
            try {
                FileInputStream file = new FileInputStream(file_upload.getSelectedFile().getAbsolutePath());
                Image image = new Image(file);
                profilePictureContainer.setFill(new ImagePattern(image)); // set image view to picture
                profilePictureContainer.setEffect(new DropShadow(+25d, 0d, +2d, Color.WHITE));
            } catch(FileNotFoundException fileNotFoundException){
                System.out.println("There was an error opening this file, ensure it hasn't been deleted.");
                fileNotFoundException.printStackTrace();
            }
        }
    }

    // validates username and enters chat room depending socket connection
    public void enterChatRoom(){
        if(validateUsername()){
            System.out.println("valid username.");
        }
    }

}
