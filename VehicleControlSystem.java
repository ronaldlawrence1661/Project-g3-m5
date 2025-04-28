import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import javax.swing.Timer;

/**
 * Vehicle Control System - Main Application Class
 * 
 * Updated with job acceptance and removal functionality
 */
public class VehicleControlSystem extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "vehicle_control";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "lilronnie03";
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JTable vehicleOwnersTable;
    private JTable taskOwnersTable;
    private JTextArea consoleLog;
    
    // Vehicle Owner Form Components
    private JTextField ownerIdField;
    private JTextField ownerNameField;
    private JTextField vehicleMakeField;
    private JTextField vehicleModelField;
    private JTextField vehicleYearField;
    private JTextField residencyTimeField;
    
    // Task Owner Form Components
    private JTextField clientIdField;
    private JTextField clientNameField;
    private JTextField jobDescriptionField;
    private JTextField jobDurationField;
    private JTextField jobDeadlineField;
    
    // Table Models
    private DefaultTableModel vehicleOwnersModel;
    private DefaultTableModel taskOwnersModel;
    
    private static void showCoverPage() {
        JFrame coverFrame = new JFrame("Welcome to Group 3's VCRTS");
        coverFrame.setSize(1000, 1000);
        coverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        coverFrame.setLayout(new BorderLayout());
        
        // Main label
        JLabel coverLabel = new JLabel("Welcome to Group 3's VCRTS", SwingConstants.CENTER);
        coverLabel.setFont(new Font("Serif", Font.BOLD, 22));
        coverLabel.setForeground(new Color(59, 89, 182));
        
        // Additional information
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(new JLabel("Vehicle Control and Request", SwingConstants.CENTER));
        infoPanel.add(new JLabel("Tracking System", SwingConstants.CENTER));
        infoPanel.add(new JLabel("Loading...", SwingConstants.CENTER));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(coverLabel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        
        coverFrame.add(mainPanel, BorderLayout.CENTER);
        coverFrame.setLocationRelativeTo(null);
        coverFrame.setVisible(true);
        
        Timer timer = new Timer(3000, e -> {
            coverFrame.dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    public VehicleControlSystem() {
        super("Vehicle Control System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        showCoverPage();
        initializeDatabase();
        createGUI();
        refreshDatabaseView();
        
        setVisible(true);
        logMessage("System started. ");
    }
    
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            stmt.executeUpdate("USE " + DB_NAME);
            
            // Vehicle Owners table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS vehicle_owners (" +
                "owner_id VARCHAR(10) PRIMARY KEY, " +
                "owner_name VARCHAR(100), " +
                "vehicle_make VARCHAR(50), " +
                "vehicle_model VARCHAR(50), " +
                "vehicle_year INT, " +
                "residency_time INT, " +
                "status VARCHAR(20) DEFAULT 'Pending', " +
                "entry_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            
            // Task Owners table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS task_owners (" +
                "client_id VARCHAR(10) PRIMARY KEY, " +
                "client_name VARCHAR(100), " +
                "job_description VARCHAR(255), " +
                "job_duration INT, " +
                "job_deadline DATE, " +
                "status VARCHAR(20) DEFAULT 'Pending', " +
                "completion_time TIMESTAMP NULL, " +
                "entry_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            
            logMessage("Database initialized successfully");
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void createGUI() {
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("VEHICLE CONTROL SYSTEM DEMO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(1, 2));
        
        // Vehicle Owner Form
        JPanel vehicleOwnerPanel = new JPanel(new BorderLayout());
        vehicleOwnerPanel.setBorder(BorderFactory.createTitledBorder("Vehicle Owner Request"));
        vehicleOwnerPanel.add(createVehicleOwnerForm(), BorderLayout.CENTER);
        
        // Task Owner Form
        JPanel taskOwnerPanel = new JPanel(new BorderLayout());
        taskOwnerPanel.setBorder(BorderFactory.createTitledBorder("Client Request"));
        taskOwnerPanel.add(createTaskOwnerForm(), BorderLayout.CENTER);
        
        formPanel.add(vehicleOwnerPanel);
        formPanel.add(taskOwnerPanel);
        mainPanel.add(formPanel, BorderLayout.NORTH);
        
        // Database View Panel
        JPanel dbPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Vehicle Owners", createVehicleOwnersTable());
        tabbedPane.addTab("Task Owners", createTaskOwnersTable());
        dbPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Refresh Button
        JButton refreshButton = new JButton("Refresh Database View");
        refreshButton.addActionListener(e -> refreshDatabaseView());
        dbPanel.add(refreshButton, BorderLayout.SOUTH);
        
        mainPanel.add(dbPanel, BorderLayout.CENTER);
        
        // Console Panel
        JPanel consolePanel = new JPanel(new BorderLayout());
        consolePanel.setBorder(BorderFactory.createTitledBorder("System Log"));
        consolePanel.setPreferredSize(new Dimension(1000, 150));
        consoleLog = new JTextArea();
        consoleLog.setEditable(false);
        consolePanel.add(new JScrollPane(consoleLog), BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        add(consolePanel, BorderLayout.SOUTH);
    }
    
    private JPanel createVehicleOwnerForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Owner ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Owner ID:"), gbc);
        gbc.gridx = 1;
        ownerIdField = new JTextField(15);
        panel.add(ownerIdField, gbc);
        gbc.gridx = 2;
        JButton genOwnerIdBtn = new JButton("Generate");
        genOwnerIdBtn.addActionListener(e -> ownerIdField.setText("VO" + (1000 + new Random().nextInt(9000))));
        panel.add(genOwnerIdBtn, gbc);
        
        // Owner Name
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Owner Name:"), gbc);
        gbc.gridx = 1;
        ownerNameField = new JTextField(15);
        panel.add(ownerNameField, gbc);
        
        // Vehicle Make
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Vehicle Make:"), gbc);
        gbc.gridx = 1;
        vehicleMakeField = new JTextField(15);
        panel.add(vehicleMakeField, gbc);
        
        // Vehicle Model
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Vehicle Model:"), gbc);
        gbc.gridx = 1;
        vehicleModelField = new JTextField(15);
        panel.add(vehicleModelField, gbc);
        
        // Vehicle Year
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Vehicle Year:"), gbc);
        gbc.gridx = 1;
        vehicleYearField = new JTextField(15);
        panel.add(vehicleYearField, gbc);
        
        // Residency Time
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Residency Time (hours):"), gbc);
        gbc.gridx = 1;
        residencyTimeField = new JTextField(15);
        panel.add(residencyTimeField, gbc);
        
        // Submit Button
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submitBtn = new JButton("Submit Vehicle Request");
        submitBtn.setBackground(new Color(0, 153, 51));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.addActionListener(e -> submitVehicleRequest());
        panel.add(submitBtn, gbc);
        
        return panel;
    }
    
    private JPanel createTaskOwnerForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Client ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Client ID:"), gbc);
        gbc.gridx = 1;
        clientIdField = new JTextField(15);
        panel.add(clientIdField, gbc);
        gbc.gridx = 2;
        JButton genClientIdBtn = new JButton("Generate");
        genClientIdBtn.addActionListener(e -> clientIdField.setText("TO" + (1000 + new Random().nextInt(9000))));
        panel.add(genClientIdBtn, gbc);
        
        // Client Name
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Client Name:"), gbc);
        gbc.gridx = 1;
        clientNameField = new JTextField(15);
        panel.add(clientNameField, gbc);
        
        // Job Description
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Job Description:"), gbc);
        gbc.gridx = 1;
        jobDescriptionField = new JTextField(15);
        panel.add(jobDescriptionField, gbc);
        
        // Job Duration
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Job Duration (hours):"), gbc);
        gbc.gridx = 1;
        jobDurationField = new JTextField(15);
        panel.add(jobDurationField, gbc);
        
        // Job Deadline
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Job Deadline (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        jobDeadlineField = new JTextField(15);
        panel.add(jobDeadlineField, gbc);
        
        // Submit Button
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submitBtn = new JButton("Submit Task Request");
        submitBtn.setBackground(new Color(0, 153, 51));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.addActionListener(e -> submitTaskRequest());
        panel.add(submitBtn, gbc);
        
        // Calculate Completion Button
        gbc.gridy = 6;
        JButton calcBtn = new JButton("Calculate Completion Time");
        calcBtn.setBackground(new Color(255, 153, 0));
        calcBtn.setForeground(Color.WHITE);
        calcBtn.addActionListener(e -> calculateCompletionTime());
        panel.add(calcBtn, gbc);
        
        return panel;
    }
    
    private JScrollPane createVehicleOwnersTable() {
        String[] columns = {"Owner ID", "Owner Name", "Vehicle Make", "Model", "Year", "Residency Time", "Status", "Entry Date"};
        vehicleOwnersModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        vehicleOwnersTable = new JTable(vehicleOwnersModel);
        return new JScrollPane(vehicleOwnersTable);
    }
    
    private JScrollPane createTaskOwnersTable() {
        String[] columns = {"Client ID", "Client Name", "Job Description", "Duration", "Deadline", "Status", "Completion Time", "Entry Date", "Actions"};
        taskOwnersModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { 
                return column == getColumnCount() - 1; // Only the last column (Actions) is editable
            }
        };
        
        taskOwnersTable = new JTable(taskOwnersModel);
        
        // Add a custom renderer and editor for the Actions column
        TableColumn actionsColumn = taskOwnersTable.getColumnModel().getColumn(taskOwnersTable.getColumnCount() - 1);
        actionsColumn.setCellRenderer(new ButtonRenderer());
        actionsColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
        
        return new JScrollPane(taskOwnersTable);
    }
    
    // Button renderer for the table
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    // Button editor for the table
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private int currentRow;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            currentRow = row;
            return button;
        }
        
        public Object getCellEditorValue() {
            if (isPushed) {
                String clientId = (String) taskOwnersTable.getValueAt(currentRow, 0);
                
                if (label.equals("Accept")) {
                    acceptJob(clientId);
                } else if (label.equals("Complete")) {
                    completeJob(clientId);
                }
            }
            isPushed = false;
            return label;
        }
        
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
    
    // Method to accept a job
    private void acceptJob(String clientId) {
        try (Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE task_owners SET status = 'Accepted' WHERE client_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, clientId);
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    logMessage("Job " + clientId + " has been accepted");
                    refreshDatabaseView();
                } else {
                    logMessage("No job found with ID: " + clientId);
                }
            }
        } catch (SQLException e) {
            logMessage("Error accepting job: " + e.getMessage());
        }
    }
    
    // Method to complete and remove a job
    private void completeJob(String clientId) {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to complete and remove this job?", 
            "Confirm Completion", JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD)) {
            // First update completion time
            String updateSql = "UPDATE task_owners SET status = 'Completed', completion_time = CURRENT_TIMESTAMP WHERE client_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setString(1, clientId);
                pstmt.executeUpdate();
            }
            
            // Then remove from database
            String deleteSql = "DELETE FROM task_owners WHERE client_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                pstmt.setString(1, clientId);
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    logMessage("Job " + clientId + " has been completed and removed");
                    refreshDatabaseView();
                } else {
                    logMessage("No job found with ID: " + clientId);
                }
            }
        } catch (SQLException e) {
            logMessage("Error completing job: " + e.getMessage());
        }
    }
    
    private void submitVehicleRequest() {
        if (!validateVehicleForm()) return;
        
        String ownerId = ownerIdField.getText();
        String ownerName = ownerNameField.getText();
        String make = vehicleMakeField.getText();
        String model = vehicleModelField.getText();
        int year = Integer.parseInt(vehicleYearField.getText());
        int residency = Integer.parseInt(residencyTimeField.getText());
        
        // Business rules for acceptance/rejection
        boolean accept = true;
        String reason = "";
        
        // Rule 1: Residency time <= 60 days
        if (residency > 60) {
            accept = false;
            reason = "Residency time exceeds 60 days";
        }
        
        // Rule 2: Vehicle not older than 20 years
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (currentYear - year > 20) {
            accept = false;
            reason = "Vehicle is too old (>20 years)";
        }
        
        String status = accept ? "Accepted" : "Rejected";
        
        try (Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD)) {
            // Check if ID exists
            if (idExists(conn, "vehicle_owners", "owner_id", ownerId)) {
                JOptionPane.showMessageDialog(this, "Owner ID already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Insert record
            String sql = "INSERT INTO vehicle_owners (owner_id, owner_name, vehicle_make, vehicle_model, vehicle_year, residency_time, status) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, ownerId);
                pstmt.setString(2, ownerName);
                pstmt.setString(3, make);
                pstmt.setString(4, model);
                pstmt.setInt(5, year);
                pstmt.setInt(6, residency);
                pstmt.setString(7, status);
                
                pstmt.executeUpdate();
                
                String message = accept ? "Vehicle request ACCEPTED for " + ownerId : 
                                        "Vehicle request REJECTED for " + ownerId + ". Reason: " + reason;
                logMessage(message);
                JOptionPane.showMessageDialog(this, message, accept ? "Success" : "Rejected", 
                    accept ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
                
                refreshDatabaseView();
                clearVehicleForm();
            }
        } catch (SQLException e) {
            logMessage("Database error: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Database error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void submitTaskRequest() {
        if (!validateTaskForm()) return;
        
        String clientId = clientIdField.getText();
        String clientName = clientNameField.getText();
        String description = jobDescriptionField.getText();
        int duration = Integer.parseInt(jobDurationField.getText());
        String deadlineStr = jobDeadlineField.getText();
        
        // Business rules for acceptance/rejection
        boolean accept = true;
        String reason = "";
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date deadline = sdf.parse(deadlineStr);
            Date today = new Date();
            
            // Rule 1: Job duration <= 14 days
            if (duration > 14) {
                accept = false;
                reason = "Job duration exceeds 14 days";
            }
            
            // Rule 2: Enough time between today and deadline
            long diffInDays = (deadline.getTime() - today.getTime()) / (1000 * 60 * 60 * 24);
            if (diffInDays < duration) {
                accept = false;
                reason = "Not enough time before deadline";
            }
            
            String status = accept ? "Accepted" : "Rejected";
            
            try (Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD)) {
                // Check if ID exists
                if (idExists(conn, "task_owners", "client_id", clientId)) {
                    JOptionPane.showMessageDialog(this, "Client ID already exists", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Insert record
                String sql = "INSERT INTO task_owners (client_id, client_name, job_description, job_duration, job_deadline, status) " +
                             "VALUES (?, ?, ?, ?, ?, ?)";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, clientId);
                    pstmt.setString(2, clientName);
                    pstmt.setString(3, description);
                    pstmt.setInt(4, duration);
                    pstmt.setDate(5, new java.sql.Date(deadline.getTime()));
                    pstmt.setString(6, status);
                    
                    pstmt.executeUpdate();
                    
                    String message = accept ? "Task request ACCEPTED for " + clientId : 
                                            "Task request REJECTED for " + clientId + ". Reason: " + reason;
                    logMessage(message);
                    JOptionPane.showMessageDialog(this, message, accept ? "Success" : "Rejected", 
                        accept ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
                    
                    refreshDatabaseView();
                    clearTaskForm();
                }
            }
        } catch (ParseException e) {
            logMessage("Invalid date format: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Invalid date format (use YYYY-MM-DD)", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            logMessage("Database error: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Database error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void calculateCompletionTime() {
        if (!validateTaskForm(true)) return;
        
        try (Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD)) {
            // First get the current job's duration from the form
            int currentJobDuration = Integer.parseInt(jobDurationField.getText());
            
            // Get all jobs from the database (or just for this client if you prefer)
            String sql = "SELECT job_duration FROM task_owners";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int completionTime = currentJobDuration; // Start with current job
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int duration = rs.getInt("job_duration");
                        completionTime += duration;
                        logMessage("Job Duration: " + duration + " days, Cumulative Completion Time: " + completionTime + " hours");
                    }
                }
                
                String message = "Total cumulative completion time for all jobs: " + completionTime + " hours";
                logMessage(message);
                JOptionPane.showMessageDialog(this, message, "Completion Time Calculation", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            logMessage("Database error calculating completion time: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error calculating completion time", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            logMessage("Invalid duration value: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Please enter a valid job duration", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean idExists(Connection conn, String table, String idColumn, String id) throws SQLException {
        String sql = "SELECT 1 FROM " + table + " WHERE " + idColumn + " = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    private boolean validateVehicleForm() {
        if (ownerIdField.getText().isEmpty() || ownerNameField.getText().isEmpty() || 
            vehicleMakeField.getText().isEmpty() || vehicleModelField.getText().isEmpty() || 
            vehicleYearField.getText().isEmpty() || residencyTimeField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            Integer.parseInt(vehicleYearField.getText());
            Integer.parseInt(residencyTimeField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Year and residency time must be numbers", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private boolean validateTaskForm() {
        return validateTaskForm(false);
    }
    
    private boolean validateTaskForm(boolean skipClientFields) {
        if ((!skipClientFields && (clientIdField.getText().isEmpty() || clientNameField.getText().isEmpty())) || 
            jobDescriptionField.getText().isEmpty() || jobDurationField.getText().isEmpty() || 
            jobDeadlineField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            Integer.parseInt(jobDurationField.getText());
            new SimpleDateFormat("yyyy-MM-dd").parse(jobDeadlineField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Duration must be a number", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format (use YYYY-MM-DD)", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void refreshDatabaseView() {
        try (Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD)) {
            // Refresh Vehicle Owners
            vehicleOwnersModel.setRowCount(0);
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM vehicle_owners")) {
                while (rs.next()) {
                    vehicleOwnersModel.addRow(new Object[]{
                        rs.getString("owner_id"),
                        rs.getString("owner_name"),
                        rs.getString("vehicle_make"),
                        rs.getString("vehicle_model"),
                        rs.getInt("vehicle_year"),
                        rs.getInt("residency_time"),
                        rs.getString("status"),
                        rs.getTimestamp("entry_date")
                    });
                }
            }
            
            // Refresh Task Owners
            taskOwnersModel.setRowCount(0);
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM task_owners")) {
                while (rs.next()) {
                    String status = rs.getString("status");
                    String actionButtonText = "Pending".equals(status) ? "Accept" : "Complete";
                    
                    taskOwnersModel.addRow(new Object[]{
                        rs.getString("client_id"),
                        rs.getString("client_name"),
                        rs.getString("job_description"),
                        rs.getInt("job_duration"),
                        rs.getDate("job_deadline"),
                        status,
                        rs.getTimestamp("completion_time"),
                        rs.getTimestamp("entry_date"),
                        actionButtonText
                    });
                }
            }
            
            logMessage("Database view refreshed");
        } catch (SQLException e) {
            logMessage("Error refreshing database: " + e.getMessage());
        }
    }
    
    private void clearVehicleForm() {
        ownerIdField.setText("");
        ownerNameField.setText("");
        vehicleMakeField.setText("");
        vehicleModelField.setText("");
        vehicleYearField.setText("");
        residencyTimeField.setText("");
    }
    
    private void clearTaskForm() {
        clientIdField.setText("");
        clientNameField.setText("");
        jobDescriptionField.setText("");
        jobDurationField.setText("");
        jobDeadlineField.setText("");
    }
    
    private void logMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            consoleLog.append(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - " + message + "\n");
            consoleLog.setCaretPosition(consoleLog.getDocument().getLength());
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            showCoverPage();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new VehicleControlSystem();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

