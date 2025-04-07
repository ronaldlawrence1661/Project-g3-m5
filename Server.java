import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.io.*;
public class Server extends Thread {
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    public Server(Socket socket, DataInputStream inputStream, DataOutputStream outputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            while (true) {
                try {
                    Object object = objectInputStream.readObject();

                    if (object instanceof Job) {
                        Job job = (Job) object;
                        System.out.println("Job received: " + job);
                        CreateAdminForm jobForm = new CreateAdminForm(job, objectOutputStream);
                        jobForm.setSize(400, 300);
                        jobForm.setVisible(true);
                    } else if (object instanceof Vehicle) {
                        Vehicle vehicle = (Vehicle) object;
                        System.out.println("Vehicle received: " + vehicle);
                        CreateAdminForm vehicleForm = new CreateAdminForm(vehicle, objectOutputStream);
                        vehicleForm.setSize(400, 300);
                        vehicleForm.setVisible(true);
                    }
                } catch (EOFException e) {
                    System.out.println("EOFException: Client disconnected.");
                    break;  // Handle client disconnection
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();  // Log other exceptions
                    break;  // Exit loop on other exceptions
                }
            }

            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
