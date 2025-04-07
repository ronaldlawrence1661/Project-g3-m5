package com.vcrts.gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VehicleOwnerLoginGUI {
    public VehicleOwnerLoginGUI() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Vehicle Owner Login");
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
        if (validateLogin(username, password, "vehicle_owners.txt")) {
            JOptionPane.showMessageDialog(frame, "Login Successful!");
            new VehicleOwnerDashboard(username);
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
        showOwnerRegistrationForm(frame, username, password);
    }

    private void showOwnerRegistrationForm(JFrame parentFrame, String username, String password) {
        JFrame registrationFrame = new JFrame("Vehicle Owner Registration");
        registrationFrame.setSize(700, 500);
        registrationFrame.setLayout(new BorderLayout());
        
        JPanel ownerPanel = new JPanel();
        ownerPanel.setLayout(new GridLayout(12, 2, 5, 5));
        
        // Form fields and buttons
        JLabel ownerIdLabel = new JLabel("Owner ID (XXXX-XXXX):");
        JTextField ownerIdField = new JTextField();
        JLabel ownerContactLabel = new JLabel("Owner Contact Info (number):");
        JTextField ownerContactField = new JTextField();
        JLabel vehicleIdLabel = new JLabel("Vehicle ID:");
        JTextField vehicleIdField = new JTextField();
        JLabel residencyTypeLabel = new JLabel("Residency Type:");
        JComboBox<String> residencyTypeCombo = new JComboBox<>(new String[]{"Permanent", "Temporary"});
        JLabel residencyTimeLabel = new JLabel("Residency Time (hours):");
        JTextField residencyTimeField = new JTextField();
        JLabel vehicleMakeLabel = new JLabel("Vehicle Make:");
        JTextField vehicleMakeField = new JTextField();
        JLabel vehicleModelLabel = new JLabel("Vehicle Model:");
        JTextField vehicleModelField = new JTextField();
        JLabel vehicleYearLabel = new JLabel("Vehicle Year:");
        JTextField vehicleYearField = new JTextField();
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");
        
        styleButton(submitButton);
        backButton.setBackground(new Color(192, 57, 43));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.PLAIN, 12));
        
        ownerPanel.add(ownerIdLabel);
        ownerPanel.add(ownerIdField);
        ownerPanel.add(ownerContactLabel);
        ownerPanel.add(ownerContactField);
        ownerPanel.add(vehicleIdLabel);
        ownerPanel.add(vehicleIdField);
        ownerPanel.add(residencyTypeLabel);
        ownerPanel.add(residencyTypeCombo);
        ownerPanel.add(residencyTimeLabel);
        ownerPanel.add(residencyTimeField);
        ownerPanel.add(vehicleMakeLabel);
        ownerPanel.add(vehicleMakeField);
        ownerPanel.add(vehicleModelLabel);
        ownerPanel.add(vehicleModelField);
        ownerPanel.add(vehicleYearLabel);
        ownerPanel.add(vehicleYearField);
        ownerPanel.add(submitButton);
        ownerPanel.add(backButton);
        
        submitButton.addActionListener(e -> {
            try {
                int residencyTime = Integer.parseInt(residencyTimeField.getText());
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String ownerData = String.format("%s,%s,%s,%s,%s,%s,%d,%s,%s,%s,%s", 
                    username, password, 
                    ownerIdField.getText(), 
                    ownerContactField.getText(),
                    vehicleIdField.getText(),
                    residencyTypeCombo.getSelectedItem(),
                    residencyTime,
                    vehicleMakeField.getText(),
                    vehicleModelField.getText(),
                    vehicleYearField.getText(),
                    timestamp);
                
                sendDataToServer("VEHICLE_OWNER_REGISTRATION", ownerData, registrationFrame, parentFrame);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(registrationFrame, "Please enter a valid number for residency time.");
            }
        });
        
        backButton.addActionListener(e -> {
            registrationFrame.dispose();
            parentFrame.setVisible(true);
        });
        
        registrationFrame.add(ownerPanel, BorderLayout.CENTER);
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