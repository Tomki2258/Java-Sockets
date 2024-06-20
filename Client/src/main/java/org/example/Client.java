package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;

public class Client implements Runnable {
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private String nick;

    public Client(String address, int port,String nick) throws IOException {
        this.nick = nick;
        socket = new Socket(address, port);
        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();
        reader = new BufferedReader(new InputStreamReader(input));
        writer = new PrintWriter(output, true);
        send(String.format("NICK:%s",this.nick));
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

    public void sendFile(String path) throws IOException {
        File file = new File(path);
        long fileSize = file.length();
        writer.println(fileSize);
        FileInputStream fileIn = new FileInputStream(file);
        DataOutputStream fileOut = new DataOutputStream(socket.getOutputStream());
        byte[] buffer = new byte[64];
        int count;
        while ((count = fileIn.read(buffer)) > 0)
            fileOut.write(buffer,0,count);
        fileIn.close();
    }
    public void receiveFile(String size) throws IOException {
        long fileSize = Long.parseLong(size);
        File file = new File(String.valueOf(
                Path.of(System.getProperty("java.io.tmpdir")).resolve("result.bin")
        ));
        DataInputStream fileIn = new DataInputStream(socket.getInputStream());
        FileOutputStream fileOut = new FileOutputStream(file);
        byte[] buffer = new byte[64];
        int count, receivedSize = 0;
        while ((count = fileIn.read(buffer)) > 0) {
            System.out.print(
                    "\r" + (receivedSize * 100 / fileSize) + "%"
            );
            fileOut.write(buffer, 0, count);
        }
        fileOut.close();
    }
}