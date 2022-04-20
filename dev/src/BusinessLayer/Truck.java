package BusinessLayer;

public class Truck {
    private int plateNum;
    private String model;
    private int maxWeight;

    public Truck(int plateNum, String model, int maxWeight) {
        this.plateNum = plateNum;
        this.model = model;
        this.maxWeight = maxWeight;
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

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    @Override
    public String toString(){
        return String.format("Plate number: %d,  Model: %s,  Max Weight: %d",plateNum,model,maxWeight);
    }
}
