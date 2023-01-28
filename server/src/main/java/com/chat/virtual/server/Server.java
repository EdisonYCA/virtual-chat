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
            closeEverything(socket, br, bw);
        }
    }

    /**
     * this method is responsible for sending a string message through a buffered writer to the client
     * */
    public void sendMessageToClient(String message){
        try {
            bw.write(message);
            bw.newLine(); // indicate end of message
            bw.flush();
        }catch (IOException io){
            closeEverything(socket, br, bw);
        }
    }

    /**
     * this method is responsible for listening for messages from the client on a thread
     * */
    public void receiveMessageFromClient(VBox messageDisplay){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()) {
                    try {
                        String messageFromClient = br.readLine();
                        ServerController.displayMessageFromClient(messageFromClient, messageDisplay);
                    } catch (IOException io) {
                        io.printStackTrace();
                        break;
                    }
                }
            }
        }).start();
    }

    /**
     * this method is responsible for listening for closing the server socket, buffered reader, and buffered writer
     * */
    public static void closeEverything(Socket socket, BufferedReader bf, BufferedWriter bw){
        try{
            if(bf != null){
                bf.close();
            }

            if(bw != null){
                bw.close();
            }

            if(socket != null){
                socket.close();
            }

        } catch(IOException io){
            io.printStackTrace();
        }
    }
}
