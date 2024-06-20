package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    private  Socket socket = null;
    private PrintWriter printWriter = null;
    private ArrayList<ClientHandler> handlers = new ArrayList<>();

    public Server() throws IOException {
        serverSocket = new ServerSocket(3000);
    }
    public void broadcast(String message) {
        handlers.forEach(handler -> handler.send(message));
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
    private void finishSocket() throws IOException , SocketException {
        socket.close();
        serverSocket.close();
        System.out.println("Server closed");
    }
    private void resendMessage(String message){
        if(message.equals("Siemanko")){
            printWriter.println("Siemanko rowniez !");
        }
    }
    public void serveClient() throws IOException {
        socket = serverSocket.accept();
        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        String message;
        writer.println("Hello!");
        while((message = reader.readLine()) != null) {
            writer.println(message);
        }
        socket.close();
    }
    public void removeHandler(ClientHandler handler) {
        handlers.remove(handler);
    }
    public void disconnectHandlers() {
        handlers.forEach(handler-> handler.send("Bye!"));
        handlers.clear();
    }
}
