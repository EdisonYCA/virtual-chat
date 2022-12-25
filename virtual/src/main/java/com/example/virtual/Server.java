package com.example.virtual;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private final ServerSocket server;
    //constructor
    Server(ServerSocket server){
        this.server = server;
    }

    // start server
    public void startServer(){
        try {
            while(!server.isClosed()){
                server.accept(); // blocking operation
                System.out.println("A new client has joined the chat.");
            }
        }
        catch (IOException io){
            io.printStackTrace(); // implement close server method
        }
    }

    public static void main(String[] args) throws IOException{
        ServerSocket server = new ServerSocket(1234);
        Server chatServer = new Server(server);
        chatServer.startServer();
    }
}
