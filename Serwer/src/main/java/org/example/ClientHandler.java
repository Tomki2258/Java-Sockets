package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ClientHandler implements Runnable{
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final Server server;
    private String nick;

    private void close() throws IOException {
        socket.close(); server.removeHandler(this);
    }

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();
        reader = new BufferedReader(new InputStreamReader(input));
        writer = new PrintWriter(output, true);
    }
    @Override
    public void run() {
        System.out.println("Client connected");
        String message;
        try {
            while ((message = reader.readLine()) != null) {
                if(checkMessage(message)){
                    String finalMessage = this.getClientName()+ ": " + message;
                    System.out.println(finalMessage); // Wyswietlanie nadawcow i wiadomosci po stronie serwera
                    server.broadcast(message); // wysylanie wiadomosci do wszysstkich
                }
                else{
                    server.singleBrodcast(this.getClientName(),this);
                }
            }
            socket.close();
        } catch (IOException e) { throw new RuntimeException(e); }
        System.out.println(this.getClientName() + " leaves");
    }
    public void send(String message) {
        writer.println(message);
    }

    //Ta funkcja sprawdza specjalnie typy wiadomosci
    private boolean checkMessage(String message) throws IOException {
        if(message.contains("NICK")){ // nick przesyłany w formacie NICK:TwojStary ( PREFIX:NICK )
            this.nick = message.split(":")[1];
            return false;
        }
        else if(message.equals("/getNick")) // zwrocienie aktualnej nazwy uzytkownika
        {
            System.out.println(nick);
            return false;
        }
        else if(message.equals("/online")){ // zwrocenie listy dostenych uzytkownikow
            server.singleBrodcast(
                    server.getNickNames(),
                    this
            );
            return false;
        }
        else if(message.startsWith("/w")){ // Wiadomosc prywatna do konkretnego uzytkownika
            server.recipentMessage(message,this);
            return false;
        }
        else if(message.equals("/EndSerwer")){
            server.broadcast("SERVER END by: "+this.getClientName());
            server.close();
            return true;
        }
        return true;
    }
    public String getClientName() {
        return nick;
    }
}
