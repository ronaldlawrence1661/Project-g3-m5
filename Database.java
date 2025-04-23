// Database.java
import java.sql.*;

public class Database {
    private static Connection connection = null;
    private static final String URL = "jdbc:mysql://localhost:3306/VC3?useTimezone=true&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "yourpassword";

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Client operations
    public static boolean registerClient(String username, String password) throws SQLException {
        String sql = "INSERT INTO clients (username, password) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            return stmt.executeUpdate() > 0;
        }
    }

    public static boolean validateClient(String username, String password) throws SQLException {
        String sql = "SELECT 1 FROM clients WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Vehicle Owner operations
    public static boolean registerVehicleOwner(String username, String password, String make, 
                                             String model, int residencyTime, String phone) throws SQLException {
        String sql = "INSERT INTO vehicle_owners (username, password, make, model, residency_time, phone) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, make);
            stmt.setString(4, model);
            stmt.setInt(5, residencyTime);
            stmt.setString(6, phone);
            return stmt.executeUpdate() > 0;
        }
    }

    public static boolean validateVehicleOwner(String username, String password) throws SQLException {
        String sql = "SELECT 1 FROM vehicle_owners WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Job operations
    public static boolean submitJob(String username, String jobDate, String jobDesc, int duration) throws SQLException {
        String sql = "INSERT INTO jobs (client_id, job_date, job_desc, duration) " +
                     "VALUES ((SELECT client_id FROM clients WHERE username = ?), ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, jobDate);
            stmt.setString(3, jobDesc);
            stmt.setInt(4, duration);
            return stmt.executeUpdate() > 0;
        }
    }

    public static ResultSet getPendingJobs() throws SQLException {
        String sql = "SELECT j.job_id, c.username as client_username, j.job_date, j.job_desc, j.duration " +
                     "FROM jobs j JOIN clients c ON j.client_id = c.client_id " +
                     "WHERE j.status = 'PENDING'";
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    public static boolean updateJobStatus(int jobId, String status, Integer ownerId) throws SQLException {
        String sql = "UPDATE jobs SET status = ?, assigned_owner_id = ? WHERE job_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            if (ownerId != null) {
                stmt.setInt(2, ownerId);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, jobId);
            return stmt.executeUpdate() > 0;
        }
    }

    public static ResultSet getVehicleOwners() throws SQLException {
        String sql = "SELECT owner_id, username, make, model, residency_time, phone FROM vehicle_owners";
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    public static ResultSet getAcceptedJobsForOwner(int ownerId) throws SQLException {
        String sql = "SELECT j.job_id, c.username as client_username, j.job_date, j.job_desc, j.duration " +
                     "FROM jobs j JOIN clients c ON j.client_id = c.client_id " +
                     "WHERE j.assigned_owner_id = ? AND j.status = 'ACCEPTED'";
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, ownerId);
        return stmt.executeQuery();
    }
}