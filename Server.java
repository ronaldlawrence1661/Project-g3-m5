import java.io.*;
import java.net.*;

public class Server extends Thread {
    final DataInputStream inputStream;
    final DataOutputStream outputStream;
    final Socket socket;

   
    public Server(Socket socket, DataInputStream inputStream, DataOutputStream outputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }   

    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
             // To send response to client
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream); 
            
            while (true) {
                try {
                     // Receive object from client
                    Object object = objectInputStream.readObject();

                    if (object instanceof Job) { 
                        System.out.println("Job request received...");
                        Job job = (Job) object;
                        System.out.println("Object converted to Job...");
                        
                        // Pass output stream to send response
                        CreateAdminForm jobForm = new CreateAdminForm(job, objectOutputStream);
                        jobForm.setSize(400, 300);
                        jobForm.setVisible(true);


                    } else if (object instanceof Vehicle) { 
                        System.out.println("Car request received...");
                        Vehicle vehicle = (Vehicle) object;
                        System.out.println("Object converted to Car...");
                        
                        // Pass output stream to send response
                        CreateAdminForm carForm = new CreateAdminForm(vehicle, objectOutputStream);
                        carForm.setSize(400, 300);
                        carForm.setVisible(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break; // Exit loop on exception, e.g., client disconnects
                }
            }
            // Close resources after loop exits
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
