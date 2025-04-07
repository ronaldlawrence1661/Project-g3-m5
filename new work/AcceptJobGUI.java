package com.vcrts.gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class AcceptJobGUI {
    private String username;

    public AcceptJobGUI(String username) {
        this.username = username;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Accept Job");
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> jobComboBox = new JComboBox<>();
        JTextField completedDurationField = new JTextField();
        JLabel statusLabel = new JLabel("Status:");
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Pending", "In Progress", "Completed"});
        JButton acceptButton = new JButton("Accept Job");
        JButton backButton = new JButton("Back");

        styleButton(acceptButton);
        
        backButton.setBackground(new Color(192, 57, 43));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));

        try (BufferedReader reader = new BufferedReader(new FileReader("jobs.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jobComboBox.addItem(line);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "No jobs found or error reading file.");
        }

        panel.add(new JLabel("Select Job:"));
        panel.add(jobComboBox);
        panel.add(new JLabel("Completed Duration (hours):"));
        panel.add(completedDurationField);
        panel.add(statusLabel);
        panel.add(statusCombo);
        panel.add(acceptButton);
        panel.add(backButton);

        acceptButton.addActionListener(e -> acceptJob(frame, jobComboBox, completedDurationField, statusCombo));
        backButton.addActionListener(e -> {
            new VehicleOwnerDashboard(username);
            frame.dispose();
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void acceptJob(JFrame frame, JComboBox<String> jobComboBox, 
                         JTextField completedDurationField, JComboBox<String> statusCombo) {
        try {
            String selectedJob = (String) jobComboBox.getSelectedItem();
            int completedDuration = Integer.parseInt(completedDurationField.getText());
            
            if (selectedJob == null) {
                JOptionPane.showMessageDialog(frame, "No job selected.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(frame, 
                "Are you sure you want to accept this job?\n" + selectedJob,
                "Confirm Job Acceptance", JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            List<String> jobs = new ArrayList<>();
            boolean jobFound = false;
            
            try (BufferedReader reader = new BufferedReader(new FileReader("jobs.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.equals(selectedJob)) {
                        jobFound = true;
                        String[] parts = line.split(",");
                        if (parts.length >= 6) {
                            int totalDuration = Integer.parseInt(parts[4].trim());
                            int remainingDuration = totalDuration - completedDuration;
                            if (remainingDuration > 0) {
                                String updatedJob = String.format("%s,%s,%s,%s,%d,%s,%s,%s,%s", 
                                    parts[0], parts[1], parts[2], parts[3], remainingDuration, 
                                    parts.length > 5 ? parts[5] : "",
                                    parts.length > 6 ? parts[6] : "",
                                    statusCombo.getSelectedItem(),
                                    parts.length > 8 ? parts[8] : "");
                                jobs.add(updatedJob);
                            }
                        }
                    } else {
                        jobs.add(line);
                    }
                }
            }

            if (!jobFound) {
                JOptionPane.showMessageDialog(frame, "Selected job not found.");
                return;
            }

            try (FileWriter writer = new FileWriter("jobs.txt")) {
                for (String job : jobs) {
                    writer.write(job + "\n");
                }
                JOptionPane.showMessageDialog(frame, "Job Accepted and Updated Successfully!");
                new VehicleOwnerDashboard(username);
                frame.dispose();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number for completed duration.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error updating jobs file.");
        }
    }
    
    private void styleButton(JButton button) {
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }
}