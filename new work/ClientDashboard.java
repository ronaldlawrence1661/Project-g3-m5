package com.vcrts.gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientDashboard {
    private String username;

    public ClientDashboard(String username) {
        this.username = username;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Client Dashboard - Welcome " + username);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField jobDateField = new JTextField();
        JTextField deadlineField = new JTextField();
        JTextField clientIDField = new JTextField();
        JTextField jobDurationField = new JTextField();
        JTextField jobDescriptionField = new JTextField();
        JComboBox<String> priorityCombo = new JComboBox<>(new String[]{"Low", "Medium", "High", "Critical"});
        JButton submitButton = new JButton("Submit Job");
        JButton backButton = new JButton("Back");

        styleButton(submitButton);
        backButton.setBackground(new Color(192, 57, 43));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(new JLabel("Job Date (YYYY-MM-DD):"));
        panel.add(jobDateField);
        panel.add(new JLabel("Job Deadline (YYYY-MM-DD):"));
        panel.add(deadlineField);
        panel.add(new JLabel("Client ID:"));
        panel.add(clientIDField);
        panel.add(new JLabel("Job Duration (hours):"));
        panel.add(jobDurationField);
        panel.add(new JLabel("Job Description:"));
        panel.add(jobDescriptionField);
        panel.add(new JLabel("Priority:"));
        panel.add(priorityCombo);
        panel.add(submitButton);
        panel.add(backButton);

        submitButton.addActionListener(e -> {
            try {
                String jobDate = jobDateField.getText();
                String deadline = deadlineField.getText();
                String clientID = clientIDField.getText();
                String jobDurationStr = jobDurationField.getText();
                String jobDescription = jobDescriptionField.getText();
                String priority = (String) priorityCombo.getSelectedItem();
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                String jobData = String.format("%s,%s,%s,%s,%s,%s,%s,%s", 
                    username, jobDate, deadline, clientID, jobDurationStr, 
                    jobDescription, priority, timestamp);
                
                sendJobToServer(jobData, frame);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number for job duration.");
            }
        });

        backButton.addActionListener(e -> {
            MainMenu.showMainMenu();
            frame.dispose();
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void sendJobToServer(String jobData, JFrame frame) {
        try (Socket socket = new Socket("localhost", 9806);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream input = new DataInputStream(socket.getInputStream())) {
            
            output.writeUTF("JOB_SUBMISSION");
            output.writeUTF(jobData);
            
            String response = input.readUTF();
            if (response.equals("ACCEPT")) {
                JOptionPane.showMessageDialog(frame, "Job Submitted and Approved by VC Controller!");
            } else {
                JOptionPane.showMessageDialog(frame, "Job Rejected by VC Controller.");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error connecting to VC Controller: " + ex.getMessage());
        }
    }
    
    private void styleButton(JButton button) {
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }
}