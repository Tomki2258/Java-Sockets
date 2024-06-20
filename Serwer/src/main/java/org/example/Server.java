package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private  Socket socket = null;
    private PrintWriter printWriter = null;
    private ArrayList<ClientHandler> handlers = new ArrayList<>();

    public void close() throws IOException {
        serverSocket.close();
    }
    public Server() throws IOException {
        serverSocket = new ServerSocket(3000);
    }
    public void broadcast(String message) {
        handlers.forEach(handler -> handler.send(handler.getClientName() + ": " + message));
    }
    public void singleBrodcast(String message,ClientHandler clientHandler){
        clientHandler.send(message);
    }
    public void listen() throws IOException{
        System.out.println("Server started");
        while(true) {
            Socket socket = serverSocket.accept();
            ClientHandler handler = new ClientHandler(socket,this);
            Thread thread = new Thread(handler);
            thread.start();
            handlers.add(handler);
        }
    }
    public void removeHandler(ClientHandler handler) {
        handlers.remove(handler);
    }
    public String getNickNames(){
        String nickNames = null;
        for (ClientHandler client : handlers){
            nickNames += client.getClientName() + "\n";
        }
        return "All chat users :" + nickNames;
    }
    public void recipentMessage(String message,ClientHandler sender){
        List<String> splittedMessage = List.of(message.split(" ",3));
        for (ClientHandler client : handlers){
            if(client.getClientName().equals(splittedMessage.get(1))){
                singleBrodcast(splittedMessage.get(3),client);
                return;
            }
        }
        singleBrodcast("Wrong with minecraft private message",sender);
    }
}
