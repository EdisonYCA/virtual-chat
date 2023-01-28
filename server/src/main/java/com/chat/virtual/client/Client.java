package com.chat.virtual.client;
import com.chat.virtual.server.Server;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
public class Client {
    private Socket socket; // socket to connect to server
    private BufferedReader br; // buffered reader to read messages
    private BufferedWriter bw; // buffered writer to send messages


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

    /**
     * this method is responsible for sending a string message through a buffered writer to the server
     * */
    public void sendMessageToServer(String message){
        try {
            bw.write(message);
            bw.newLine(); // indicate end of message
            bw.flush();
        }catch (IOException io){
            Server.closeEverything(socket, br, bw);
        }
    }

    /**
     * this method is responsible for listening for messages from the server on a thread
     * */
    public void receiveMessageFromServer(VBox messageDisplay){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()) {
                    try {
                        String messageFromClient = br.readLine(); // read messages when "\n" appears
                        ClientController.displayMessageFromServer(messageFromClient, messageDisplay);
                    } catch (IOException io) {
                        Server.closeEverything(socket, br, bw);
                        break;
                    }
                }
            }
        }).start();
    }
}
