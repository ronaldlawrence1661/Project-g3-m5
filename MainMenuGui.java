import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MainMenuGUI {
    public void display() {
        JFrame frame = new JFrame("VCRTS - Vehicular Cloud Real-Time System");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton clientLoginButton = new JButton("Client Login");
        JButton vehicleOwnerLoginButton = new JButton("Vehicle Owner Login");
        JButton checkClientInfoButton = new JButton("Check Client Info");
        JButton checkVehicleOwnerInfoButton = new JButton("Check Vehicle Owner Info");
        JButton exitButton = new JButton("Exit");

        styleButton(clientLoginButton);
        styleButton(vehicleOwnerLoginButton);
        styleButton(checkClientInfoButton);
        styleButton(checkVehicleOwnerInfoButton);
        exitButton.setBackground(new Color(192, 57, 43));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(clientLoginButton);
        panel.add(vehicleOwnerLoginButton);
        panel.add(checkClientInfoButton);
        panel.add(checkVehicleOwnerInfoButton);
        panel.add(exitButton);

        frame.add(panel, BorderLayout.CENTER);

        clientLoginButton.addActionListener(e -> {
            new ClientLoginGUI();
            frame.dispose();
        });

        vehicleOwnerLoginButton.addActionListener(e -> {
            new VehicleOwnerLoginGUI();
            frame.dispose();
        });

        checkClientInfoButton.addActionListener(e -> {
            try (BufferedReader reader = new BufferedReader(new FileReader("clients.txt"))) {
                StringBuilder clientInfo = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    clientInfo.append(line).append("\n");
                }
                JOptionPane.showMessageDialog(frame, "Client Info:\n" + clientInfo.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error reading clients file: " + ex.getMessage());
            }
        });

        checkVehicleOwnerInfoButton.addActionListener(e -> {
            try (BufferedReader reader = new BufferedReader(new FileReader("vehicle_owners.txt"))) {
                StringBuilder vehicleOwnerInfo = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    vehicleOwnerInfo.append(line).append("\n");
                }
                JOptionPane.showMessageDialog(frame, "Vehicle Owner Info:\n" + vehicleOwnerInfo.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error reading vehicle owners file: " + ex.getMessage());
            }
        });

        exitButton.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }
}