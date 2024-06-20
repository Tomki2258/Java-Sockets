package org.example;

import java.io.*;
import java.net.Socket;

public class Main {
    private static String nick;
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Podaj nick");
        try {
            nick = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Client client = new Client("localhost", 3000,nick);
        new Thread(client).start();

        while(true) {
            String message = reader.readLine();
            client.send(message);
        }
    }
}