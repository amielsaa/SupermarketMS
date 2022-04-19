package Business;

public class Truck {
    int plateNum;
    String model;
    int maxWeigt;

    public Truck(int plateNum, String model, int maxWeigt) {
        this.plateNum = plateNum;
        this.model = model;
        this.maxWeigt = maxWeigt;
    }

    public int getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(int plateNum) {
        this.plateNum = plateNum;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getMaxWeigt() {
        return maxWeigt;
    }

    public void setMaxWeigt(int maxWeigt) {
        this.maxWeigt = maxWeigt;
    }
}
