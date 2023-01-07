package com.chat.virtual.server;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.TextField;
import animatefx.animation.*;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;


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

    // validates username and enters chat room depending socket connection
    public void enterChatRoom(){
        if(validateUsername()){
            System.out.println("valid username.");
        }
    }

}
