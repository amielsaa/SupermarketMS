package DeliveryModule.BusinessLayer;

public class Truck {
    private int plateNum;
    private String model;
    private int maxWeight;

    public Truck(int plateNum, String model, int maxWeight) throws Exception {
        validateTruckPlateID(plateNum);
        this.plateNum = plateNum;
        this.model = model;
        this.maxWeight = maxWeight;
    }

    public int getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(int plateNum) throws Exception {
        validateTruckPlateID(plateNum);
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
        if(maxWeight>=0)
        this.maxWeight = maxWeight;
    }

    @Override
    public String toString(){
        return String.format("Plate number: %d\n\t* Model: %s\n\t* Max Weight: %d\n",plateNum,model,maxWeight);
    }

    protected void validateTruckPlateID(int id)throws Exception{
        if (id < 1000000 || id > 99999999)
            throw new Exception("truck number can be only a number with 7 or 8 digits");
    }
}
