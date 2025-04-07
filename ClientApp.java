import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class ClientApp {
    public static void main(String[] args) {
        try {
            // Get inputs using dialog boxes
            String jobDesc = JOptionPane.showInputDialog("Enter job description:");
            String deadlineInput = JOptionPane.showInputDialog("Enter deadline (yyyy-MM-dd):");
            String redundancyInput = JOptionPane.showInputDialog("Enter redundancy:");
            String durationInput = JOptionPane.showInputDialog("Enter duration:");

            // Parse values
            Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(deadlineInput);
            int redundancy = Integer.parseInt(redundancyInput);
            int duration = Integer.parseInt(durationInput);

            // Create job object
            Job job = new Job(jobDesc, deadline, redundancy, duration);

            // Connect to server
            Socket socket = new Socket("localhost", 5000);
            JOptionPane.showMessageDialog(null, "Connected to server.");

            // Send object
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(dos);
            objectOutputStream.writeObject(job);
            objectOutputStream.flush();

            JOptionPane.showMessageDialog(null, "Job sent to server!");

            // Close everything
            objectOutputStream.close();
            dos.close();
            socket.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
