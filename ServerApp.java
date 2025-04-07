// ServerApp.java
import java.io.*;
import java.net.*;

public class ServerApp {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected.");

                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                // Your existing Server class handles this connection
                Server serverHandler = new Server(socket, dis, dos);
                serverHandler.start(); // Start the thread to handle the client
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
