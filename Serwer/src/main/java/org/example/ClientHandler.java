package org.example;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final Server server;

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
        String message;
        try {
            while ((message = reader.readLine()) != null) {
                server.broadcast(message);
            }
            socket.close();
        } catch (IOException e) { throw new RuntimeException(e); }
    }
    public void send(String message) {
        writer.println(message);
    }
}
