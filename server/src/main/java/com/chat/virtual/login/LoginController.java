package com.chat.virtual.login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FilenameUtils;
import animatefx.animation.Shake;

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
        new Shake(usernameField).play();
    }

    // allows user to upload an image as a profile picture
    public void uploadImage(){
        JFileChooser file_upload = new JFileChooser();

        // create file filter for only images
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", ImageIO.getReaderFileSuffixes());
        file_upload.setFileFilter(filter);

        int resVal = file_upload.showOpenDialog(null); // returns approved int if user selects a file

        if(resVal == JFileChooser.APPROVE_OPTION){ // user has uploaded a file
            try {
                FileInputStream file = new FileInputStream(file_upload.getSelectedFile().getAbsolutePath());
                if(!accept(new File(file_upload.getSelectedFile().getAbsolutePath()))){ // if file extension is not a valid image extension
                    profilePictureContainer.setEffect(new DropShadow(+25d, 0d, +2d, Color.RED));
                    new Shake(profilePictureContainer).play();
                }
                else{
                    Image image = new Image(file);
                    profilePictureContainer.setFill(new ImagePattern(image)); // set image view to picture
                    profilePictureContainer.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
                }
            } catch(FileNotFoundException fileNotFoundException){
                System.out.println("There was an error opening this file, ensure it hasn't been deleted.");
                fileNotFoundException.printStackTrace();
            }
        }
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

    // validates username and enters chat room depending socket connection
    public void enterChatRoom(){
        if(validateUsername()){

        }
    }

}
