


import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;



public class VCControllerServer extends Thread {
    String name;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    public VCControllerServer(Socket socket, DataInputStream inputStream, DataOutputStream outputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        
    }


    static VCControllerGUI gui;

    public static void setGUI(VCControllerGUI gui) {
        VCControllerServer.gui = gui;
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


   
        private void saveDataToFile(String requestType, String data) throws IOException {
            String filename = requestType.equals("CLIENT_REGISTRATION") ? "clients.txt" : 
                           requestType.equals("VEHICLE_OWNER_REGISTRATION") ? "vehicle_owners.txt" : 
                           "jobs.txt";
            
            try (FileWriter writer = new FileWriter(filename, true)) {
                writer.write(data + "\n");
            }
        }
    }
