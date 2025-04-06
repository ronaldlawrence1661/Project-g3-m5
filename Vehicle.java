import java.io.Serializable;

public class Vehicle implements Serializable {
    private int carID;
    private int ownerID;
    private String carMake;
    private String carModel;
    private String carResidencyTime;
    private Boolean inUse = false;
    private int jobID;

    public Vehicle(int carID, int ownerID, String carMake, String carModel, String carResidencyTime) {
        this.carMake = carMake;
        this.carModel = carModel;
        this.ownerID = ownerID;
        this.carResidencyTime = carResidencyTime;
        this.carID = carID;
        this.jobID = 0; // Set job ID to 0 to indicate that the car is currently available for hire
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public boolean isStarted() {
        System.out.println("Checking if vehicle is started.");
        return false; // Placeholder return value
    }

    public boolean isCompleted() {
        System.out.println("Checking if vehicle has completed.");
        return false; // Placeholder return value
    }
}