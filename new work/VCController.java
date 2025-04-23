import java.io.Serializable;
import java.util.*;

class Vehicle {
    private String vin;
    private boolean available;

    public Vehicle(String vin) {
        this.vin = vin;
        this.available = true;
    }

    public String getVIN() {
        return vin;
    }

    public boolean isAvailable() {
        return available;
    }

    public void startJob(Job job) {
        available = false; // Mark vehicle as busy
        // Simulate job processing
        System.out.println("Vehicle " + vin + " is processing job: " + job.getJobId());
        // After job processing, set available to true
        available = true; // Reset availability after job completion
    }

    public boolean canHandleJob(Job job) {
        // Logic to determine if the vehicle can handle the job
        return true; // Placeholder for actual logic
    }
}

 class Job implements Serializable {
    private static final long serialVersionUID = 1L;
    private String clientID;
    private String overview;
    private int redundancy;
    private int duration;
    private Date deadline;
    private String date;
    private String description;

    public Job(String clientID, String overview, int redundancy, int duration) {
        this.clientID = clientID;
        this.overview = overview;
        this.redundancy = redundancy;
        this.duration = duration;
    }

    public Job(String clientID, Date deadline, int redundancy, int duration) {
        this.clientID = clientID;
        this.overview = overview;
        this.redundancy = redundancy;
        this.duration = duration;
        this.deadline = deadline; // Initialize the deadline
    }

    // Getters and setters
    public String getClientID() {
        return clientID;
    }

    public int getDuration() {
        return duration;
    }

    public int getRedundancy() {
        return redundancy;
    }

    public void setRedundancy(int redundancy) {
        this.redundancy = redundancy;
    }
        // Getter for the date field
        public String getDate() {
            return date;
        }
    
        // Setter for the date field (optional)
        public void setDate(String date) {
            this.date = date;
        }
       
    
        // Setter for the date field (optional)
        public void setDescription(String description) {
            this.description = description;
        }
        public String getDescription() {
            return description;
        }
        
      
    @Override
    public String toString() {
        return "Job{" +
                "clientID='" + clientID + '\'' +
                ", overview='" + overview + '\'' +
                ", redundancy=" + redundancy +
                ", duration=" + duration +
                '}';
    }
}

public class VCController {
    private List<Vehicle> vehicles;
    private Queue<Job> jobQueue;
    private Map<Vehicle, List<Job>> vehicleJobs;

    public VCController() {
        vehicles = new ArrayList<>();
        jobQueue = new LinkedList<>();
        vehicleJobs = new HashMap<>();
    }

    public void registerVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        System.out.println("Vehicle registered: " + vehicle.getVIN());
    }

    public void submitJob(Job job) {
        jobQueue.add(job);
        System.out.println("Job submitted: " + job.getJobId());
    }

    public void allocateResources() {
        while (!jobQueue.isEmpty()) {
            Job job = jobQueue.poll();
            Vehicle availableVehicle = findAvailableVehicle(job);
            if (availableVehicle != null) {
                assignJobToVehicle(availableVehicle, job);
            } else {
                jobQueue.add(job);
                System.out.println("No available vehicle for job: " + job.getJobId());
                break;
            }
        }
    }

    private Vehicle findAvailableVehicle(Job job) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isAvailable() && vehicle.canHandleJob(job)) {
                return vehicle;
            }
        }
        return null;
    }

    private void assignJobToVehicle(Vehicle vehicle, Job job) {
        vehicleJobs.computeIfAbsent(vehicle, k -> new ArrayList<>()).add(job);
        vehicle.startJob(job);
        System.out.println("Job " + job.getJobId() + " assigned to vehicle: " + vehicle.getVIN());
    }

    public void monitorJobs() {
        for (Map.Entry<Vehicle, List<Job>> entry : vehicleJobs.entrySet()) {
            Vehicle vehicle = entry.getKey();
            List<Job> jobs = entry.getValue();
            for (Job job : new ArrayList<>(jobs)) {
                if (job.isCompleted()) {
                    System.out.println("Job " + job.getJobId() + " completed by vehicle: " + vehicle.getVIN());
                    jobs.remove(job);
                }
            }
        }
    }

    public static void main(String[] args) {
        VCController controller = new VCController();
        Vehicle vehicle1 = new Vehicle("VIN123");
        Vehicle vehicle2 = new Vehicle("VIN456");

        controller.registerVehicle(vehicle1);
        controller.registerVehicle(vehicle2);

        Job job1 = new Job("Job1");
        Job job2 = new Job("Job2");

        controller.submitJob(job1);
        controller.submitJob(job2);

        controller.allocateResources();
        job1.completeJob(); // Simulate job completion
        controller.monitorJobs();
    }
}