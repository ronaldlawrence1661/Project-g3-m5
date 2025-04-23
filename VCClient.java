// VCClient.java
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class VCClient {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public VCClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
    }

    // Client operations
    public boolean registerClient(String username, String password) throws IOException {
        output.writeUTF("CLIENT_REGISTER");
        output.writeUTF(username);
        output.writeUTF(password);
        return input.readUTF().equals("REGISTRATION_SUCCESS");
    }

    public boolean validateClient(String username, String password) throws IOException {
        output.writeUTF("CLIENT_LOGIN");
        output.writeUTF(username);
        output.writeUTF(password);
        return input.readUTF().equals("LOGIN_SUCCESS");
    }

    // Vehicle Owner operations
    public boolean registerVehicleOwner(String username, String password, String make, 
                                      String model, int residencyTime, String phone) throws IOException {
        output.writeUTF("OWNER_REGISTER");
        output.writeUTF(username);
        output.writeUTF(password);
        output.writeUTF(make);
        output.writeUTF(model);
        output.writeInt(residencyTime);
        output.writeUTF(phone);
        return input.readUTF().equals("REGISTRATION_SUCCESS");
    }

    public boolean validateVehicleOwner(String username, String password) throws IOException {
        output.writeUTF("OWNER_LOGIN");
        output.writeUTF(username);
        output.writeUTF(password);
        return input.readUTF().equals("LOGIN_SUCCESS");
    }

    // Job operations
    public boolean submitJob(String username, String jobDate, String jobDesc, int duration) throws IOException {
        output.writeUTF("SUBMIT_JOB");
        output.writeUTF(username);
        output.writeUTF(jobDate);
        output.writeUTF(jobDesc);
        output.writeInt(duration);
        return input.readUTF().equals("JOB_SUBMITTED");
    }

    public String getPendingJobs() throws IOException {
        output.writeUTF("GET_PENDING_JOBS");
        return input.readUTF();
    }

    public String getVehicleOwners() throws IOException {
        output.writeUTF("GET_VEHICLE_OWNERS");
        return input.readUTF();
    }

    public boolean updateJobStatus(int jobId, String status, int ownerId) throws IOException {
        output.writeUTF("UPDATE_JOB");
        output.writeInt(jobId);
        output.writeUTF(status);
        output.writeInt(ownerId);
        return input.readUTF().equals("JOB_UPDATED");
    }

    public String getOwnerJobs(int ownerId) throws IOException {
        output.writeUTF("GET_OWNER_JOBS");
        output.writeInt(ownerId);
        return input.readUTF();
    }

    public void close() throws IOException {
        socket.close();
    }

    public static void main(String[] args) {
        try {
            VCClient client = new VCClient("localhost", 9806);
            Scanner scanner = new Scanner(System.in);
            
            System.out.println("1. Register Client");
            System.out.println("2. Login Client");
            System.out.println("3. Register Vehicle Owner");
            System.out.println("4. Login Vehicle Owner");
            System.out.print("Choose option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (option) {
                case 1:
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    if (client.registerClient(username, password)) {
                        System.out.println("Registration successful!");
                    } else {
                        System.out.println("Registration failed!");
                    }
                    break;
                case 2:
                    System.out.print("Username: ");
                    username = scanner.nextLine();
                    System.out.print("Password: ");
                    password = scanner.nextLine();
                    if (client.validateClient(username, password)) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Login failed!");
                    }
                    break;
                case 3:
                    System.out.print("Username: ");
                    username = scanner.nextLine();
                    System.out.print("Password: ");
                    password = scanner.nextLine();
                    System.out.print("Make: ");
                    String make = scanner.nextLine();
                    System.out.print("Model: ");
                    String model = scanner.nextLine();
                    System.out.print("Residency Time (years): ");
                    int residencyTime = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    System.out.print("Phone: ");
                    String phone = scanner.nextLine();
                    if (client.registerVehicleOwner(username, password, make, model, residencyTime, phone)) {
                        System.out.println("Registration successful!");
                    } else {
                        System.out.println("Registration failed!");
                    }
                    break;
                case 4:
                    System.out.print("Username: ");
                    username = scanner.nextLine();
                    System.out.print("Password: ");
                    password = scanner.nextLine();
                    if (client.validateVehicleOwner(username, password)) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Login failed!");
                    }
                    break;
                default:
                    System.out.println("Invalid option!");
            }
            
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
