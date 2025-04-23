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

class Job {
    private String jobId;
    private boolean completed;

    public Job(String jobId) {
        this.jobId = jobId;
        this.completed = false;
    }

    public String getJobId() {
        return jobId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void completeJob() {
        completed = true; // Mark job as completed
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