package com.vcrts.gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientLoginGUI {
    public ClientLoginGUI() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Client Login");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 2, 10, 10));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");
        JButton backButton = new JButton("Back to Main Menu");

        styleButton(loginButton);
        styleButton(signUpButton);
        backButton.setBackground(new Color(192, 57, 43));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.PLAIN, 12));

        frame.add(new JLabel("Username:"));
        frame.add(usernameField);
        frame.add(new JLabel("Password:"));
        frame.add(passwordField);
        frame.add(loginButton);
        frame.add(signUpButton);
        frame.add(backButton);

        loginButton.addActionListener(e -> handleLogin(frame, usernameField, passwordField));
        signUpButton.addActionListener(e -> handleSignUp(frame, usernameField, passwordField));
        backButton.addActionListener(e -> {
            MainMenu.showMainMenu();
            frame.dispose();
        });

        frame.setVisible(true);
    }

    private void handleLogin(JFrame frame, JTextField usernameField, JPasswordField passwordField) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (validateLogin(username, password, "clients.txt")) {
            JOptionPane.showMessageDialog(frame, "Login Successful!");
            new ClientDashboard(username);
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid username or password.");
        }
    }

    private void handleSignUp(JFrame frame, JTextField usernameField, JPasswordField passwordField) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Username and password cannot be empty.");
            return;
        }
        showClientRegistrationForm(frame, username, password);
    }

    private void showClientRegistrationForm(JFrame parentFrame, String username, String password) {
        JFrame registrationFrame = new JFrame("Client Registration");
        registrationFrame.setSize(600, 400);
        registrationFrame.setLayout(new BorderLayout());
        
        JPanel clientPanel = new JPanel();
        clientPanel.setLayout(new GridLayout(9, 2, 5, 5));
        
        // Form fields and buttons
        JLabel clientIdLabel = new JLabel("Client ID (XXX-XXX):");
        JTextField clientIdField = new JTextField();
        JLabel clientContactLabel = new JLabel("Client Contact Info (number):");
        JTextField clientContactField = new JTextField();
        JLabel clientNameLabel = new JLabel("Full Name:");
        JTextField clientNameField = new JTextField();
        JLabel clientEmailLabel = new JLabel("Email:");
        JTextField clientEmailField = new JTextField();
        JLabel clientAddressLabel = new JLabel("Address:");
        JTextField clientAddressField = new JTextField();
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");
        
        styleButton(submitButton);
        backButton.setBackground(new Color(192, 57, 43));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.PLAIN, 12));
        
        clientPanel.add(clientIdLabel);
        clientPanel.add(clientIdField);
        clientPanel.add(clientContactLabel);
        clientPanel.add(clientContactField);
        clientPanel.add(clientNameLabel);
        clientPanel.add(clientNameField);
        clientPanel.add(clientEmailLabel);
        clientPanel.add(clientEmailField);
        clientPanel.add(clientAddressLabel);
        clientPanel.add(clientAddressField);
        clientPanel.add(submitButton);
        clientPanel.add(backButton);
        
        submitButton.addActionListener(e -> {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String clientData = String.format("%s,%s,%s,%s,%s,%s,%s,%s", 
                username, password, 
                clientIdField.getText(), 
                clientContactField.getText(),
                clientNameField.getText(),
                clientEmailField.getText(),
                clientAddressField.getText(),
                timestamp);
            
            sendDataToServer("CLIENT_REGISTRATION", clientData, registrationFrame, parentFrame);
        });
        
        backButton.addActionListener(e -> {
            registrationFrame.dispose();
            parentFrame.setVisible(true);
        });
        
        registrationFrame.add(clientPanel, BorderLayout.CENTER);
        registrationFrame.setVisible(true);
        parentFrame.setVisible(false);
    }

    private void sendDataToServer(String requestType, String data, JFrame currentFrame, JFrame parentFrame) {
        try (Socket socket = new Socket("localhost", 9806);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream input = new DataInputStream(socket.getInputStream())) {
            
            output.writeUTF(requestType);
            output.writeUTF(data);
            
            String response = input.readUTF();
            if (response.equals("ACCEPT")) {
                JOptionPane.showMessageDialog(currentFrame, "Registration approved by VC Controller!");
                currentFrame.dispose();
                parentFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(currentFrame, "Registration rejected by VC Controller.");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(currentFrame, "Error connecting to VC Controller: " + ex.getMessage());
        }
    }

    private boolean validateLogin(String username, String password, String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // File not found is treated as invalid login
        }
        return false;
    }
    
    private void styleButton(JButton button) {
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
    }
}