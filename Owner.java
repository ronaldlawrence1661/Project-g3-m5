public class Owner {
    public String OwnerID;
    public String CarInfo;
    public int residencyTime;
    public int Phone;

    public Owner(String ownerID, String carInfo, int residencyTime, int phone) {
        this.OwnerID = ownerID;
        this.CarInfo = carInfo;
        this.residencyTime = residencyTime;
        this.Phone = phone;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        this.OwnerID = ownerID;
    }

    public String getCarInfo() {
        return CarInfo;
    }

    public void setCarInfo(String carInfo) {
        this.CarInfo = carInfo;
    }

    public int getResidencyTime() {
        return residencyTime;
    }

    public void setResidencyTime(int residencyTime) {
        this.residencyTime = residencyTime;
    }

    public String getOwnerDetails() {
        return "OwnerID: " + OwnerID + "\nCar: " + CarInfo + "\nResidency Time: " + residencyTime + " years\nPhone: " + Phone;
    }
}