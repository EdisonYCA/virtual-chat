package com.chat.virtual.client;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
public class Client {
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;


    public Client(Socket socket){
        try {
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        catch(IOException io){
            io.printStackTrace();
        }
    }

    // send message from client -> server through socket
    public void sendMessageToServer(String message){
        try {
            bw.write(message);
            bw.newLine(); // indicate end of message
            bw.flush();
        }catch (IOException io){
            io.printStackTrace();
        }
    }

    // listen for messages from server
    public void receiveMessageFromServer(VBox messageDisplay){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()) {
                    try {
                        String messageFromClient = br.readLine(); // read messages when "\n" appears
                        ClientController.displayMessageFromServer(messageFromClient, messageDisplay);
                    } catch (IOException io) {
                        io.printStackTrace();
                        break;
                    }
                }
            }
        }).start();
    }
}
