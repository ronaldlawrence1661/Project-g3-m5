import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class ClientDashboard {
    private String username;

    public ClientDashboard(String username) {
        this.username = username;
        JFrame frame = new JFrame("Client Dashboard - Welcome " + username);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField jobDateField = new JTextField();
        JTextField clientIDField = new JTextField();
        JTextField durationField = new JTextField();
        JButton submitButton = new JButton("Submit Job");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Job Date:"));
        panel.add(jobDateField);
        panel.add(new JLabel("Client ID:"));
        panel.add(clientIDField);
        panel.add(new JLabel("Duration:"));
        panel.add(durationField);
        panel.add(submitButton);
        panel.add(backButton);

        submitButton.addActionListener(e -> {
            String jobDate = jobDateField.getText();
            String clientID = clientIDField.getText();
            int duration = Integer.parseInt(durationField.getText());

            try (FileWriter writer = new FileWriter("jobs.txt", true)) {
                writer.write(username + "," + jobDate + "," + clientID + "," + duration + "\n");
                JOptionPane.showMessageDialog(frame, "Job Submitted Successfully!");
                new ClientDashboard(username); // Return to dashboard
                frame.dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error writing to jobs file: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> {
            VCRTS.showMainMenu();
            frame.dispose();
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}