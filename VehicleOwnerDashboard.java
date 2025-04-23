

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class VehicleOwnerDashboard {
    private String username;

    public VehicleOwnerDashboard(String username) {
        this.username = username;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Vehicle Owner Dashboard - Welcome " + username);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton calculateCompletionTimeButton = new JButton("Calculate Completion Time");
        JButton checkRegisteredOwnersButton = new JButton("Check Registered Vehicle Owners");
        JButton activeJobsButton = new JButton("Active Jobs");
        JButton acceptJobButton = new JButton("Accept Job");
        JButton viewVehicleInfoButton = new JButton("View My Vehicle Info");
        JButton backButton = new JButton("Back");

        styleButton(calculateCompletionTimeButton);
        styleButton(checkRegisteredOwnersButton);
        styleButton(activeJobsButton);
        styleButton(acceptJobButton);
        styleButton(viewVehicleInfoButton);
        
        backButton.setBackground(new Color(192, 57, 43));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(calculateCompletionTimeButton);
        panel.add(checkRegisteredOwnersButton);
        panel.add(activeJobsButton);
        panel.add(acceptJobButton);
        panel.add(viewVehicleInfoButton);
        panel.add(backButton);

        calculateCompletionTimeButton.addActionListener(e -> calculateCompletionTime(frame));
        checkRegisteredOwnersButton.addActionListener(e -> checkRegisteredOwners(frame));
        activeJobsButton.addActionListener(e -> showActiveJobs(frame));
        acceptJobButton.addActionListener(e -> {
            new AcceptJobGUI(username);
            frame.dispose();
        });
        viewVehicleInfoButton.addActionListener(e -> viewVehicleInfo(frame));
        backButton.addActionListener(e -> {
            MainMenu.showMainMenu();
            frame.dispose();
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void calculateCompletionTime(JFrame frame) {
        try (BufferedReader reader = new BufferedReader(new FileReader("jobs.txt"))) {
            String line;
            StringBuilder completionTimes = new StringBuilder("Completion Times:\n");
            int totalTime = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    try {
                        int duration = Integer.parseInt(parts[4].trim());
                        totalTime += duration;
                        completionTimes.append("Job for ").append(parts[0])
                                      .append(": Duration=").append(duration)
                                      .append(" hours, Completion Time=").append(totalTime)
                                      .append(" hours\n");
                    } catch (NumberFormatException ex) {
                        completionTimes.append("Invalid duration for job: ").append(line).append("\n");
                    }
                }
            }
            JOptionPane.showMessageDialog(frame, completionTimes.toString());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "No jobs found or error reading file.");
        }
    }

    private void checkRegisteredOwners(JFrame frame) {
        try (BufferedReader reader = new BufferedReader(new FileReader("vehicle_owners.txt"))) {
            StringBuilder ownersInfo = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                ownersInfo.append(line).append("\n");
            }
            JOptionPane.showMessageDialog(frame, "Registered Vehicle Owners:\n" + ownersInfo.toString());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "No owner data found or error reading file.");
        }
    }

    private void showActiveJobs(JFrame frame) {
        try (BufferedReader reader = new BufferedReader(new FileReader("jobs.txt"))) {
            StringBuilder jobsInfo = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jobsInfo.append(line).append("\n");
            }
            JOptionPane.showMessageDialog(frame, "Active Jobs:\n" + jobsInfo.toString());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "No jobs found or error reading file.");
        }
    }

    private void viewVehicleInfo(JFrame frame) {
        try (BufferedReader reader = new BufferedReader(new FileReader("vehicle_owners.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username)) {
                    StringBuilder vehicleInfo = new StringBuilder();
                    vehicleInfo.append("Owner ID: ").append(parts[2]).append("\n")
                              .append("Contact: ").append(parts[3]).append("\n")
                              .append("Vehicle ID: ").append(parts[4]).append("\n")
                              .append("Residency Type: ").append(parts[5]).append("\n")
                              .append("Residency Time: ").append(parts[6]).append(" hours\n")
                              .append("Vehicle Make: ").append(parts[7]).append("\n")
                              .append("Vehicle Model: ").append(parts[8]).append("\n")
                              .append("Vehicle Year: ").append(parts[9]);
                    JOptionPane.showMessageDialog(frame, "Your Vehicle Info:\n" + vehicleInfo.toString());
                    return;
                }
            }
            JOptionPane.showMessageDialog(frame, "No vehicle information found for your account.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error reading vehicle owner data.");
        }
    }
    
    private void styleButton(JButton button) {
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }
}