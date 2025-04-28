import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AcceptJobGUI {
    private String username;

    public AcceptJobGUI(String username) {
        this.username = username;
        JFrame frame = new JFrame("Accept Job");
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> jobComboBox = new JComboBox<>();
        JTextField completedDurationField = new JTextField();
        JButton acceptButton = new JButton("Accept Job");
        JButton backButton = new JButton("Back");

        // Populate the jobComboBox with active jobs
        try (BufferedReader reader = new BufferedReader(new FileReader("jobs.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jobComboBox.addItem(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error reading jobs file: " + ex.getMessage());
        }

        panel.add(new JLabel("Select Job:"));
        panel.add(jobComboBox);
        panel.add(new JLabel("Completed Duration:"));
        panel.add(completedDurationField);
        panel.add(acceptButton);
        panel.add(backButton);

        acceptButton.addActionListener(e -> {
            String selectedJob = (String) jobComboBox.getSelectedItem();
            int completedDuration = Integer.parseInt(completedDurationField.getText());

            try (BufferedReader reader = new BufferedReader(new FileReader("jobs.txt"))) {
                StringBuilder updatedJobs = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (line.equals(selectedJob)) {
                        int totalDuration = Integer.parseInt(parts[3]);
                        int remainingDuration = totalDuration - completedDuration;
                        if (remainingDuration > 0) {
                            updatedJobs.append(parts[0] + "," + parts[1] + "," + parts[2] + "," + remainingDuration + "\n");
                        }
                    } else {
                        updatedJobs.append(line).append("\n");
                    }
                }

                try (FileWriter writer = new FileWriter("jobs.txt")) {
                    writer.write(updatedJobs.toString());
                }

                JOptionPane.showMessageDialog(frame, "Job Accepted and Updated Successfully!");
                new VehicleOwnerDashboard(username);
                frame.dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error updating jobs file: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Invalid duration entered.");
            }
        });

        backButton.addActionListener(e -> {
            new VehicleOwnerDashboard(username);
            frame.dispose();
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}