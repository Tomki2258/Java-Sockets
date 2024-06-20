package org.example;

import java.io.*;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 3000);
        new Thread(client).start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            String message = reader.readLine();
            client.send(message);
        }
    }
}