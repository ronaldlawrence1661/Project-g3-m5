
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MainMenu {
    public static void showCoverPage() {
        JFrame coverFrame = new JFrame("Welcome to Group 3's VCRTS");
        coverFrame.setSize(400, 400);
        coverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        coverFrame.setLayout(new BorderLayout());

        JLabel coverLabel = new JLabel("Welcome to Group 3's VCRTS", SwingConstants.CENTER);
        coverLabel.setFont(new Font("Serif", Font.BOLD, 22));
        coverLabel.setForeground(new Color(59, 89, 182));

        coverFrame.add(coverLabel, BorderLayout.CENTER);
        coverFrame.setVisible(true);

        Timer timer = new Timer(3000, e -> {
            coverFrame.dispose();
            showMainMenu();
        });
        timer.setRepeats(false);
        timer.start();
    }

    public static void showMainMenu() {
        JFrame frame = new JFrame("VCRTS - Vehicular Cloud Real-Time System");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton clientLoginButton = new JButton("Client Login");
        JButton vehicleOwnerLoginButton = new JButton("Vehicle Owner Login");
        JButton checkClientInfoButton = new JButton("Check Client Info");
        JButton checkVehicleOwnerInfoButton = new JButton("Check Vehicle Owner Info");
        JButton vcControllerButton = new JButton("VC Controller");
        JButton serverClientButton = new JButton("Start Server & Client");
        JButton exitButton = new JButton("Exit");

        styleButton(clientLoginButton);
        styleButton(vehicleOwnerLoginButton);
        styleButton(checkClientInfoButton);
        styleButton(checkVehicleOwnerInfoButton);
        styleButton(vcControllerButton);
        styleButton(serverClientButton);
        exitButton.setBackground(new Color(192, 57, 43));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(clientLoginButton);
        panel.add(vehicleOwnerLoginButton);
        panel.add(checkClientInfoButton);
        panel.add(checkVehicleOwnerInfoButton);
        panel.add(vcControllerButton);
        panel.add(serverClientButton);
        panel.add(exitButton);

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
                JOptionPane.showMessageDialog(frame, "No client data found or error reading file.");
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
                JOptionPane.showMessageDialog(frame, "No vehicle owner data found or error reading file.");
            }
        });

        vcControllerButton.addActionListener(e -> {
            new VCControllerGUI();
            frame.dispose();
        });

        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        serverClientButton.addActionListener((e) -> {
            new Thread(() -> {
                try {
                    ServerApp.main(null); // call your server
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    Thread.sleep(1000); // small delay to let the server start first
                    ClientApp.main(null); // call your client
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();

            JOptionPane.showMessageDialog(frame, "Server and Client started.");
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void styleButton(JButton button) {
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }
}