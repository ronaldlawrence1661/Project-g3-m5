import java.io.*;
import java.net.*;
import java.util.Date;

public class ClientApp {
    public static void main(String[] args) {
        try {
            // Create socket connection to the server at localhost, port 5000
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Connected to server.");

            // Set up input and output streams
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(dos);

            // Create a Job object to send to the server
            String jobDesc = "Fix the car";
            Date deadline = new Date();  // Set a real deadline
            int redundancy = 2;
            int duration = 5;
            Job job = new Job(jobDesc, deadline, redundancy, duration);

            // Send the Job object to the server
            objectOutputStream.writeObject(job);
            objectOutputStream.flush();
            System.out.println("Job sent to server.");

            // Close streams and socket
            objectOutputStream.close();
            dos.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
