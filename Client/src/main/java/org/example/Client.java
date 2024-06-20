package org.example;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;
    public Client(String address, int port) throws IOException {
        socket = new Socket(address, port);
        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();
        reader = new BufferedReader(new InputStreamReader(input));
        writer = new PrintWriter(output, true);
    }
    @Override
    public void run() {
        try {
            String message;
            while ((message = reader.readLine()) != null)
                System.out.println(message);
        } catch (IOException e) { e.printStackTrace(); }
    }
    public void send(String message) {
        writer.println(message);
    }
}