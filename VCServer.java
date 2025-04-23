// VCServer.java
import java.io.*;
import java.net.*;
import java.sql.*;

public class VCServer {
    private ServerSocket serverSocket;
    private boolean running;

    public VCServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            running = true;
            
            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            DatabaseH.closeConnection();
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;
        private DataInputStream input;
        private DataOutputStream output;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                input = new DataInputStream(clientSocket.getInputStream());
                output = new DataOutputStream(clientSocket.getOutputStream());
                
                String requestType = input.readUTF();
                
                switch (requestType) {
                    case "CLIENT_REGISTER":
                        handleClientRegistration();
                        break;
                    case "CLIENT_LOGIN":
                        handleClientLogin();
                        break;
                    case "OWNER_REGISTER":
                        handleOwnerRegistration();
                        break;
                    case "OWNER_LOGIN":
                        handleOwnerLogin();
                        break;
                    case "SUBMIT_JOB":
                        handleJobSubmission();
                        break;
                    case "GET_PENDING_JOBS":
                        handleGetPendingJobs();
                        break;
                    case "GET_VEHICLE_OWNERS":
                        handleGetVehicleOwners();
                        break;
                    case "UPDATE_JOB":
                        handleJobUpdate();
                        break;
                    case "GET_OWNER_JOBS":
                        handleGetOwnerJobs();
                        break;
                    default:
                        output.writeUTF("UNKNOWN_REQUEST");
                }
                
            } catch (IOException e) {
                System.out.println("Client disconnected");
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleClientRegistration() throws IOException {
            String username = input.readUTF();
            String password = input.readUTF();
            
            try {
                boolean success = DatabaseH.registerClient(username, password);
                output.writeUTF(success ? "REGISTRATION_SUCCESS" : "REGISTRATION_FAILED");
            } catch (SQLException e) {
                output.writeUTF("DATABASE_ERROR: " + e.getMessage());
            }
        }

        private void handleClientLogin() throws IOException {
            String username = input.readUTF();
            String password = input.readUTF();
            
            try {
                boolean valid = DatabaseH.validateClient(username, password);
                output.writeUTF(valid ? "LOGIN_SUCCESS" : "LOGIN_FAILED");
            } catch (SQLException e) {
                output.writeUTF("DATABASE_ERROR: " + e.getMessage());
            }
        }

        private void handleOwnerRegistration() throws IOException {
            String username = input.readUTF();
            String password = input.readUTF();
            String make = input.readUTF();
            String model = input.readUTF();
            int residencyTime = input.readInt();
            String phone = input.readUTF();
            
            try {
                boolean success = DatabaseH.registerVehicleOwner(username, password, make, model, residencyTime, phone);
                output.writeUTF(success ? "REGISTRATION_SUCCESS" : "REGISTRATION_FAILED");
            } catch (SQLException e) {
                output.writeUTF("DATABASE_ERROR: " + e.getMessage());
            }
        }

        private void handleOwnerLogin() throws IOException {
            String username = input.readUTF();
            String password = input.readUTF();
            
            try {
                boolean valid = DatabaseH.validateVehicleOwner(username, password);
                output.writeUTF(valid ? "LOGIN_SUCCESS" : "LOGIN_FAILED");
            } catch (SQLException e) {
                output.writeUTF("DATABASE_ERROR: " + e.getMessage());
            }
        }

        private void handleJobSubmission() throws IOException {
         //   String username = input.readUTF();
            String jobDate = input.readUTF();
            String jobDesc = input.readUTF();
            int duration = input.readInt();
            
            try {
                boolean success = DatabaseH.submitJob(jobDate, jobDesc, duration);
                output.writeUTF(success ? "JOB_SUBMITTED" : "SUBMISSION_FAILED");
            } catch (SQLException e) {
                output.writeUTF("DATABASE_ERROR: " + e.getMessage());
            }
        }

        private void handleGetPendingJobs() throws IOException {
            try {
                ResultSet rs = DatabaseH.getPendingJobs();
                StringBuilder jobs = new StringBuilder();
                
                while (rs.next()) {
                    jobs.append(rs.getInt("job_id")).append(",");
                    jobs.append(rs.getString("client_username")).append(",");
                    jobs.append(rs.getString("job_date")).append(",");
                    jobs.append(rs.getString("job_desc")).append(",");
                    jobs.append(rs.getInt("duration")).append(";");
                }
                
                output.writeUTF(jobs.toString().isEmpty() ? "NO_JOBS" : jobs.toString());
            } catch (SQLException e) {
                output.writeUTF("DATABASE_ERROR: " + e.getMessage());
            }
        }

        private void handleGetVehicleOwners() throws IOException {
            try {
                ResultSet rs = DatabaseH.getVehicleOwners();
                StringBuilder owners = new StringBuilder();
                
                while (rs.next()) {
                    owners.append(rs.getInt("owner_id")).append(",");
                    owners.append(rs.getString("username")).append(",");
                    owners.append(rs.getString("make")).append(",");
                    owners.append(rs.getString("model")).append(",");
                    owners.append(rs.getInt("residency_time")).append(",");
                    owners.append(rs.getString("phone")).append(";");
                }
                
                output.writeUTF(owners.toString().isEmpty() ? "NO_OWNERS" : owners.toString());
            } catch (SQLException e) {
                output.writeUTF("DATABASE_ERROR: " + e.getMessage());
            }
        }

        private void handleJobUpdate() throws IOException {
            int jobId = input.readInt();
            String status = input.readUTF();
            int ownerId = input.readInt();
            
            try {
                boolean success = DatabaseH.updateJobStatus(jobId, status, ownerId);
                output.writeUTF(success ? "JOB_UPDATED" : "UPDATE_FAILED");
            } catch (SQLException e) {
                output.writeUTF("DATABASE_ERROR: " + e.getMessage());
            }
        }

        private void handleGetOwnerJobs() throws IOException {
            int ownerId = input.readInt();
            
            try {
                ResultSet rs = DatabaseH.getAcceptedJobsForOwner(ownerId);
                StringBuilder jobs = new StringBuilder();
                
                while (rs.next()) {
                    jobs.append(rs.getInt("job_id")).append(",");
                    jobs.append(rs.getString("client_username")).append(",");
                    jobs.append(rs.getString("job_date")).append(",");
                    jobs.append(rs.getString("job_desc")).append(",");
                    jobs.append(rs.getInt("duration")).append(";");
                }
                
                output.writeUTF(jobs.toString().isEmpty() ? "NO_JOBS" : jobs.toString());
            } catch (SQLException e) {
                output.writeUTF("DATABASE_ERROR: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new VCServer(9806);
    }
}