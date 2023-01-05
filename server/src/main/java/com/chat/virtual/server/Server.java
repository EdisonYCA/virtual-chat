package com.chat.virtual.server;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket; // accepts incoming requests to join server
    private BufferedWriter bw; // write messages to client
    private BufferedReader br; // read messages to client
    private Socket socket; // communication end point for client
    public Server(ServerSocket serverSocket){
        try {
            this.serverSocket = serverSocket;
            socket = serverSocket.accept(); // blocking method to accept connection
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException io){
            io.printStackTrace();
            // close streams
        }
    }

    // send message from sever -> client through socket
    public void sendMessageToClient(String message){
        try {
            bw.write(message);
            bw.newLine(); // indicate end of message
            bw.flush();
        }catch (IOException io){
            io.printStackTrace();
        }
    }

    // listen for messages from clients
    public void receiveMessageFromClient(VBox messageDisplay){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()) {
                    try {
                        String messageFromClient = br.readLine(); // read messages when "\n" appears
                        ServerController.displayMessageFromClient(messageFromClient, messageDisplay);
                    } catch (IOException io) {
                        io.printStackTrace();
                        break;
                    }
                }
            }
        }).start();
    }
}
