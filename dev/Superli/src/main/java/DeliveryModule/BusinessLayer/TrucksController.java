package DeliveryModule.BusinessLayer;

import DeliveryModule.DataAccessLayer.TruckDAO;

import java.util.*;

public class TrucksController {
    private HashMap<Integer, Truck> trucks;
    private TruckDAO truckDAO;

    private HashMap<LicenseType,Integer> licenseMapper=new HashMap<LicenseType, Integer>(){{
        put(LicenseType.C,Integer.MAX_VALUE);
        put(LicenseType.C1,12000);
    }};

    public TrucksController(TruckDAO truckDAO) {
        this.truckDAO = truckDAO;
        trucks=new HashMap<>();
    }

    public void load() throws Exception{
        addTruck(1000001, "small truck", 9000);
        addTruck(1000002, "small truck", 9000);
        addTruck(1000003, "small truck", 9000);
        addTruck(1000004, "big truck", 14000);
        addTruck(1000005, "big truck", 14000);
    }

    public void addTruck(int plateNum, String model, int maxWeight)throws Exception{
        if(trucks.containsKey(plateNum)){
            throw new Exception(String.format("A truck with plate number %d already exists..",plateNum));
        }
        trucks.put(plateNum,new Truck(plateNum,model,maxWeight));
    }
    public void editPlateNum(int oldPlateNum, int newPlateNum) throws Exception {
        if(!trucks.containsKey(oldPlateNum)){
            throw new Exception(String.format("A truck with plate number %d does not exist..",oldPlateNum));
        }
        if(trucks.containsKey(newPlateNum)){
            throw new Exception(String.format("A truck with plate number %d already exists..",newPlateNum));
        }
        trucks.get(oldPlateNum).setPlateNum(newPlateNum);
        trucks.put(newPlateNum, trucks.get(oldPlateNum));
        trucks.remove(oldPlateNum);
    }

    public void editModel(int plateNum, String newModel) throws Exception {
        if(!trucks.containsKey(plateNum)){
            throw new Exception(String.format("A truck with plate number %d does not exist..",plateNum));
        }
        trucks.get(plateNum).setModel(newModel);
    }

    public void editMaxWeight(int plateNum, int maxWeight) throws Exception {
        if(!trucks.containsKey(plateNum)){
            throw new Exception(String.format("A truck with plate number %d does not exist..",plateNum));
        }
        if(maxWeight<0){
            throw new Exception("Weight of a truck cannot be negative..");
        }
        trucks.get(plateNum).setMaxWeight(maxWeight);
    }

    public void deleteTruck(int plateNum) throws Exception{
        if(!trucks.containsKey(plateNum)){
            throw new Exception(String.format("A truck with plate number %d does not exist..",plateNum));
        }
        trucks.remove(plateNum);
    }

    //change to protected before submitting
    protected boolean isAbleToDrive(LicenseType licenseType, int plateNum){
        if(trucks.containsKey(plateNum) && licenseMapper.containsKey(licenseType)){
            return trucks.get(plateNum).getMaxWeight()<=licenseMapper.get(licenseType);
        }
        return false;
    }

    public ArrayList<Truck> getTrucks(){
        ArrayList<Truck> list=new ArrayList<>(trucks.values());
        list.sort(Comparator.comparingInt(Truck::getMaxWeight));
        return list;
    }

    public Truck getTruck(int plateNum) throws Exception{
        if(!trucks.containsKey(plateNum)){
            throw new Exception(String.format("A truck with plate number %d does not exist..",plateNum));
        }
        return trucks.get(plateNum);
    }
}
