

public class Owner {
    private String ownerID;
    private String carInfo;
    private int residencyTime;
    private int phone;

    public Owner(String ownerID, String carInfo, int residencyTime, int phone) {
        this.ownerID = ownerID;
        this.carInfo = carInfo;
        this.residencyTime = residencyTime;
        this.phone = phone;
    }

    // Getters
    public String getOwnerID() { return ownerID; }
    public String getCarInfo() { return carInfo; }
    public int getResidencyTime() { return residencyTime; }

    public String getOwnerDetails() {
        return "OwnerID: " + ownerID + "\nCar: " + carInfo + 
               "\nResidency Time: " + residencyTime + " years\nPhone: " + phone;
    }
}