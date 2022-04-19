package Business;

import java.util.*;

public class TruckController {
    private HashMap<Integer, Truck> trucks;
    private HashMap<LicenseType, Integer> licenseMapper=new HashMap<>(){{
        put(LicenseType.C,Integer.MAX_VALUE);
        put(LicenseType.C1,12000);
    }};

    public TruckController() {
        trucks=new HashMap<>();
    }

   public void addTruck(int plateNum, String model, int maxWeight)throws Exception{
        if(trucks.containsKey(plateNum)){
            throw new Exception(String.format("A truck with plate number %d already exists..",plateNum));
        }
        trucks.put(plateNum,new Truck(plateNum,model,maxWeight));
    }
    public void editPlateNum(int oldPlateNum, int newPlateNum) throws Exception {
        if(trucks.containsKey(oldPlateNum)==false){
            throw new Exception(String.format("A truck with plate number %d does not exist..",oldPlateNum));
        }
        if(trucks.containsKey(newPlateNum)){
            throw new Exception(String.format("A truck with plate number %d already exists..",newPlateNum));
        }
        trucks.get(oldPlateNum).setPlateNum(newPlateNum);
    }

    public void editModel(int plateNum, String newModel) throws Exception {
        if(trucks.containsKey(plateNum)==false){
            throw new Exception(String.format("A truck with plate number %d does not exist..",plateNum));
        }
        trucks.get(plateNum).setModel(newModel);
    }

    public void editMaxWeight(int plateNum, int maxWeight) throws Exception {
        if(trucks.containsKey(plateNum)==false){
            throw new Exception(String.format("A truck with plate number %d does not exist..",plateNum));
        }
        trucks.get(plateNum).setMaxWeight(maxWeight);
    }

    public void deleteTruck(int plateNum) throws Exception{
        if(trucks.containsKey(plateNum)==false){
            throw new Exception(String.format("A truck with plate number %d does not exist..",plateNum));
        }
        trucks.remove(plateNum);
    }

    protected boolean isAbleToDrive(LicenseType licenseType , int plateNum){
        if(trucks.containsKey(plateNum) && licenseMapper.containsKey(licenseType)){
            return trucks.get(plateNum).getMaxWeight()<=licenseMapper.get(licenseType);
        }
        return false;
    }

    public ArrayList<Truck> getTrucks(){
        ArrayList<Truck> list=new ArrayList<>(trucks.values());
        Collections.sort(list, Comparator.comparingInt(Truck::getMaxWeight));
        return list;
    }
}
