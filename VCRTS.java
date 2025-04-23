
// VCRTS.java
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class VCRTS {
    public static void main(String[] args) {
        showCoverPage();
    }

    private static void showCoverPage() {
        JFrame coverFrame = new JFrame("Welcome to VCRTS");
        coverFrame.setSize(400, 300);
        coverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        coverFrame.setLayout(new BorderLayout());

        JLabel coverLabel = new JLabel("Welcome to Vehicular Cloud RTS", SwingConstants.CENTER);
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
        JFrame frame = new JFrame("VCRTS - Main Menu");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton clientLoginButton = new JButton("Client Login");
        JButton vehicleOwnerLoginButton = new JButton("Vehicle Owner Login");
        JButton vcControllerButton = new JButton("VC Controller");
        JButton serverClientButton = new JButton("Start Server & Client");
        JButton exitButton = new JButton("Exit");

        // Style buttons
        Color buttonColor = new Color(59, 89, 182);
        Color exitButtonColor = new Color(192, 57, 43);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        styleButton(clientLoginButton, buttonColor, buttonFont);
        styleButton(vehicleOwnerLoginButton, buttonColor, buttonFont);
        styleButton(vcControllerButton, buttonColor, buttonFont);
        styleButton(exitButton, exitButtonColor, buttonFont);
        styleButton(serverClientButton, buttonColor, buttonFont);

        panel.add(clientLoginButton);
        panel.add(vehicleOwnerLoginButton);
        panel.add(vcControllerButton);
        panel.add(serverClientButton);
        panel.add(exitButton);

        frame.add(panel, BorderLayout.CENTER);

        // Button actions
        clientLoginButton.addActionListener(e -> {
            new ClientLoginGUI();
            frame.dispose();
        });

        vehicleOwnerLoginButton.addActionListener(e -> {
            new VehicleOwnerLoginGUI();
            frame.dispose();
        });

        vcControllerButton.addActionListener(e -> {
            new VCControllerGUI();
            frame.dispose();
        });

        exitButton.addActionListener(e -> {
            frame.dispose();
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

        frame.setVisible(true);
    }

    private static void styleButton(JButton button, Color color, Font font) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(font);
    }
}

class ClientLoginGUI {
    public ClientLoginGUI() {
        JFrame frame = new JFrame("Client Login");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(4, 2, 10, 10));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");
        JButton backButton = new JButton("Back");

        frame.add(new JLabel("Username:"));
        frame.add(usernameField);
        frame.add(new JLabel("Password:"));
        frame.add(passwordField);
        frame.add(loginButton);
        frame.add(signUpButton);
        frame.add(backButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                VCClient client = new VCClient("localhost", 9806);
                if (client.validateClient(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful!");
                    new ClientDashboard(username, client);
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password.");
                }
                client.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Connection error: " + ex.getMessage());
            }
        });

        signUpButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username and password cannot be empty.");
                return;
            }

            try {
                VCClient client = new VCClient("localhost", 9806);
                if (client.registerClient(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Registration Successful!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Registration Failed!");
                }
                client.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Connection error: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> {
            VCRTS.showMainMenu();
            frame.dispose();
        });

        frame.setVisible(true);
    }
}

class VehicleOwnerLoginGUI {
    public VehicleOwnerLoginGUI() {
        JFrame frame = new JFrame("Vehicle Owner Login");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(4, 2, 10, 10));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");
        JButton backButton = new JButton("Back");

        frame.add(new JLabel("Username:"));
        frame.add(usernameField);
        frame.add(new JLabel("Password:"));
        frame.add(passwordField);
        frame.add(loginButton);
        frame.add(signUpButton);
        frame.add(backButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                VCClient client = new VCClient("localhost", 9806);
                if (client.validateVehicleOwner(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful!");
                    new VehicleOwnerDashboard(username, client);
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password.");
                }
                client.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Connection error: " + ex.getMessage());
            }
        });

        signUpButton.addActionListener(e -> {
            new VehicleOwnerSignUpGUI();
            frame.dispose();
        });

        backButton.addActionListener(e -> {
            VCRTS.showMainMenu();
            frame.dispose();
        });

        frame.setVisible(true);
    }
}

class VehicleOwnerSignUpGUI {
    public VehicleOwnerSignUpGUI() {
        JFrame frame = new JFrame("Vehicle Owner Sign Up");
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(7, 2, 10, 10));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField makeField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField residencyField = new JTextField();
        JTextField phoneField = new JTextField();
        JButton signUpButton = new JButton("Sign Up");
        JButton backButton = new JButton("Back");

        frame.add(new JLabel("Username:"));
        frame.add(usernameField);
        frame.add(new JLabel("Password:"));
        frame.add(passwordField);
        frame.add(new JLabel("Make:"));
        frame.add(makeField);
        frame.add(new JLabel("Model:"));
        frame.add(modelField);
        frame.add(new JLabel("Residency Time (years):"));
        frame.add(residencyField);
        frame.add(new JLabel("Phone:"));
        frame.add(phoneField);
        frame.add(signUpButton);
        frame.add(backButton);

        signUpButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String make = makeField.getText();
            String model = modelField.getText();
            int residencyTime;
            try {
                residencyTime = Integer.parseInt(residencyField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number for residency time.");
                return;
            }
            String phone = phoneField.getText();

            if (username.isEmpty() || password.isEmpty() || make.isEmpty() || model.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required.");
                return;
            }

            try {
                VCClient client = new VCClient("localhost", 9806);
                if (client.registerVehicleOwner(username, password, make, model, residencyTime, phone)) {
                    JOptionPane.showMessageDialog(frame, "Registration Successful!");
                    new VehicleOwnerLoginGUI();
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Registration Failed!");
                }
                client.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Connection error: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> {
            new VehicleOwnerLoginGUI();
            frame.dispose();
        });

        frame.setVisible(true);
    }
}

class ClientDashboard {
    private String username;
    private VCClient client;

    public ClientDashboard(String username, VCClient client) {
        this.username = username;
        this.client = client;

        JFrame frame = new JFrame("Client Dashboard - " + username);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField jobDateField = new JTextField();
        JTextField jobDescField = new JTextField();
        JTextField durationField = new JTextField();
        JButton submitButton = new JButton("Submit Job");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Job Date:"));
        panel.add(jobDateField);
        panel.add(new JLabel("Job Description:"));
        panel.add(jobDescField);
        panel.add(new JLabel("Duration (hours):"));
        panel.add(durationField);
        panel.add(submitButton);
        panel.add(backButton);

        submitButton.addActionListener(e -> {
            String jobDate = jobDateField.getText();
            String jobDesc = jobDescField.getText();
            int duration;
            try {
                duration = Integer.parseInt(durationField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number for duration.");
                return;
            }

            if (jobDate.isEmpty() || jobDesc.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required.");
                return;
            }

            try {
                if (client.submitJob(username, jobDate, jobDesc, duration)) {
                    JOptionPane.showMessageDialog(frame, "Job Submitted Successfully!");
                    jobDateField.setText("");
                    jobDescField.setText("");
                    durationField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Job Submission Failed!");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Connection error: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> {
            try {
                client.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            VCRTS.showMainMenu();
            frame.dispose();
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}

class VehicleOwnerDashboard {
    private String username;
    private VCClient client;

    public VehicleOwnerDashboard(String username, VCClient client) {
        this.username = username;
        this.client = client;

        JFrame frame = new JFrame("Vehicle Owner Dashboard - " + username);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton viewJobsButton = new JButton("View Available Jobs");
        JButton myJobsButton = new JButton("View My Accepted Jobs");
        JButton backButton = new JButton("Back");

        panel.add(viewJobsButton);
        panel.add(myJobsButton);
        panel.add(backButton);

        viewJobsButton.addActionListener(e -> {
            new AvailableJobsGUI(username, client);
            frame.dispose();
        });

        myJobsButton.addActionListener(e -> {
            new MyJobsGUI(username, client);
            frame.dispose();
        });

        backButton.addActionListener(e -> {
            try {
                client.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            VCRTS.showMainMenu();
            frame.dispose();
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}

class AvailableJobsGUI {
    private String username;
    private VCClient client;

    public AvailableJobsGUI(String username, VCClient client) {
        this.username = username;
        this.client = client;

        JFrame frame = new JFrame("Available Jobs - " + username);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JTextArea jobsArea = new JTextArea();
        jobsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(jobsArea);

        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> refreshJobs(jobsArea));

        backButton.addActionListener(e -> {
            new VehicleOwnerDashboard(username, client);
            frame.dispose();
        });

        refreshJobs(jobsArea);
        frame.setVisible(true);
    }

    private void refreshJobs(JTextArea jobsArea) {
        try {
            String jobsData = client.getPendingJobs();
            if (jobsData.equals("NO_JOBS")) {
                jobsArea.setText("No pending jobs available.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            String[] jobs = jobsData.split(";");

            for (String job : jobs) {
                if (!job.isEmpty()) {
                    String[] parts = job.split(",");
                    sb.append("Job ID: ").append(parts[0]).append("\n");
                    sb.append("Client: ").append(parts[1]).append("\n");
                    sb.append("Date: ").append(parts[2]).append("\n");
                    sb.append("Description: ").append(parts[3]).append("\n");
                    sb.append("Duration: ").append(parts[4]).append(" hours\n\n");
                }
            }

            jobsArea.setText(sb.toString());
        } catch (IOException ex) {
            jobsArea.setText("Error fetching jobs: " + ex.getMessage());
        }
    }
}

class MyJobsGUI {
    private String username;
    private VCClient client;

    public MyJobsGUI(String username, VCClient client) {
        this.username = username;
        this.client = client;

        JFrame frame = new JFrame("My Jobs - " + username);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JTextArea jobsArea = new JTextArea();
        jobsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(jobsArea);

        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> refreshJobs(jobsArea));

        backButton.addActionListener(e -> {
            new VehicleOwnerDashboard(username, client);
            frame.dispose();
        });

        refreshJobs(jobsArea);
        frame.setVisible(true);
    }

    private void refreshJobs(JTextArea jobsArea) {
        try {
            // In a real system, we would get the owner ID from the username
            // For this example, we'll just use 1 as the owner ID
            String jobsData = client.getOwnerJobs(1);
            if (jobsData.equals("NO_JOBS")) {
                jobsArea.setText("No jobs assigned to you.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            String[] jobs = jobsData.split(";");

            for (String job : jobs) {
                if (!job.isEmpty()) {
                    String[] parts = job.split(",");
                    sb.append("Job ID: ").append(parts[0]).append("\n");
                    sb.append("Client: ").append(parts[1]).append("\n");
                    sb.append("Date: ").append(parts[2]).append("\n");
                    sb.append("Description: ").append(parts[3]).append("\n");
                    sb.append("Duration: ").append(parts[4]).append(" hours\n\n");
                }
            }

            jobsArea.setText(sb.toString());
        } catch (IOException ex) {
            jobsArea.setText("Error fetching jobs: " + ex.getMessage());
        }
    }
}
