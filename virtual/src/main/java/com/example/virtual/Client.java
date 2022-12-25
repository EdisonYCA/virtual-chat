package com.example.virtual;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    public static ArrayList<Client> clients = new ArrayList<>(); // contain all instances of clients
    private Socket socket;
    private Window chatWindow;
    private String username; // create interface?
    private BufferedReader bf; // read messages
    private BufferedWriter bw; // write messages
    @FXML
    private TextField messageField;

    // constructor
    Client(Socket socket, Window chatWindow, String username){
        try {
            this.socket = socket;
            this.bf = new BufferedReader(new InputStreamReader(socket.getInputStream())); // convert from byte to char
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // convert from byte to char
            this.chatWindow = chatWindow;
            this.username = username;
            clients.add(this); // adds each client to clients arraylist
            Window.startWindow();
        } catch(IOException io){
            io.printStackTrace();
        }
    }

    // send message to another client
    @FXML
    public void sendMessage()
    {
        String message = messageField.getText() + "\n";
        messageField.setText("");

        // send message to other client
        for(Client client: clients){
            try {
                if (!client.username.equals(username)) { // if not this client
                    client.bw.write(message);
                    client.bw.flush();
                }
            }
            catch (IOException io){
                io.printStackTrace();
            }
        }
    }



    // receive message method


    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1234);
        Window window = new Window();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        Client client = new Client(socket, window, username);
    }
}
