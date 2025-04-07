package com.vcrts.controller;

import com.vcrts.gui.VCControllerGUI;
import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class VCControllerServer {
    static ServerSocket serverSocket;
    static boolean running = true;
    static final ConcurrentHashMap<String, String> pendingDecisions = new ConcurrentHashMap<>();
    static VCControllerGUI gui;

    public static void setGUI(VCControllerGUI gui) {
        VCControllerServer.gui = gui;
    }

    public static void startServer() {
        try {
            serverSocket = new ServerSocket(9806);
            System.out.println("VC Controller Server started on port 9806");

            while (running) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            if (!running) {
                System.out.println("Server stopped gracefully");
            } else {
                e.printStackTrace();
            }
        }
    }

    public static void stopServer() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void setDecisionForRequest(String requestKey, String decision) {
        pendingDecisions.put(requestKey, decision);
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private DataInputStream input;
        private DataOutputStream output;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                this.input = new DataInputStream(clientSocket.getInputStream());
                this.output = new DataOutputStream(clientSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String requestType = input.readUTF();
                String data = input.readUTF();
                String requestKey = requestType + ":" + data;

                System.out.println("Received " + requestType + " request with data: " + data);

                if (gui != null) {
                    VCControllerGUI.addPendingRequest(requestType, data, requestKey);
                }

                String decision = null;
                while (decision == null && running) {
                    decision = pendingDecisions.get(requestKey);
                    if (decision == null) {
                        Thread.sleep(500);
                    }
                }

                if (decision != null) {
                    output.writeUTF(decision);
                    if (decision.equals("ACCEPT")) {
                        saveDataToFile(requestType, data);
                    }
                    pendingDecisions.remove(requestKey);
                }

                clientSocket.close();
            } catch (IOException | InterruptedException e) {
                if (!running) {
                    System.out.println("Client handler interrupted during shutdown");
                } else {
                    e.printStackTrace();
                }
            }
        }

        private void saveDataToFile(String requestType, String data) throws IOException {
            String filename = requestType.equals("CLIENT_REGISTRATION") ? "clients.txt" : 
                           requestType.equals("VEHICLE_OWNER_REGISTRATION") ? "vehicle_owners.txt" : 
                           "jobs.txt";
            
            try (FileWriter writer = new FileWriter(filename, true)) {
                writer.write(data + "\n");
            }
        }
    }
}