import java.io.Serializable;

public class Vehicle implements Serializable {
    private int carID;
    private int ownerID;

    public Vehicle(int carID, int ownerID, String carMake, String carModel, String carResidencyTime) {
        this.ownerID = ownerID;
        this.carID = carID;
    }

    // Getters
    public int getCarID() { return carID; }
    public int getOwnerID() { return ownerID; }
    
    public boolean isStarted() {
        System.out.println("Checking if vehicle is started.");
        return false; 
    }

    public boolean isCompleted() {
        System.out.println("Checking if vehicle has completed.");
        return false; 
    }
}