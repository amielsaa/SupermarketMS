package DeliveryModule.BusinessLayer;

import DeliveryModule.DataAccessLayer.TruckDAO;

import java.util.*;

public class TrucksController {
  //  private HashMap<Integer, Truck> trucks;
    private TruckDAO truckDAO;

    private HashMap<LicenseType,Integer> licenseMapper=new HashMap<LicenseType, Integer>(){{
        put(LicenseType.C,Integer.MAX_VALUE);
        put(LicenseType.C1,12000);
    }};

    public TrucksController() {
        this.truckDAO = new TruckDAO();
       // trucks=new HashMap<>();
    }

    public void load() throws Exception{
        addTruck(1000001, "small truck", 9000);
        addTruck(1000002, "small truck", 9000);
        addTruck(1000003, "small truck", 9000);
        addTruck(1000004, "big truck", 14000);
        addTruck(1000005, "big truck", 14000);
    }

    public void addTruck(int plateNum, String model, int maxWeight)throws Exception{
        if(truckDAO.Read(plateNum)!=null){
            throw new Exception(String.format("A truck with plate number %d already exists..",plateNum));
        }
        //trucks.put(plateNum,new Truck(plateNum,model,maxWeight));
        truckDAO.Create(new Truck(plateNum,model,maxWeight));
    }
    public void editPlateNum(int oldPlateNum, int newPlateNum) throws Exception {
        Truck truck=truckDAO.Read(oldPlateNum);
        if(truck==null){
            throw new Exception(String.format("A truck with plate number %d does not exist..",oldPlateNum));
        }
        if(truckDAO.Read(newPlateNum)!=null){
            throw new Exception(String.format("A truck with plate number %d already exists..",newPlateNum));
        }

        truck.setPlateNum(newPlateNum);
        truckDAO.setPlateNum(oldPlateNum,newPlateNum);

        /*
        trucks.get(oldPlateNum).setPlateNum(newPlateNum);
        trucks.put(newPlateNum, trucks.get(oldPlateNum));
        trucks.remove(oldPlateNum);

         */
    }

    public void editModel(int plateNum, String newModel) throws Exception {
        Truck truck=getTruck(plateNum);
        if(truck==null){
            throw new Exception(String.format("A truck with plate number %d does not exist..",plateNum));
        }
        truck.setModel(newModel);
        truckDAO.setModel(plateNum,newModel);
    }

    public void editMaxWeight(int plateNum, int maxWeight) throws Exception {
        Truck truck=getTruck(plateNum);
        if(truck==null){
            throw new Exception(String.format("A truck with plate number %d does not exist..",plateNum));
        }
        if(maxWeight<0){
            throw new Exception("Weight of a truck cannot be negative..");
        }
        truck.setMaxWeight(maxWeight);
        truckDAO.setMaxWeight(plateNum, maxWeight);
    }

    public void deleteTruck(int plateNum) throws Exception{
        Truck truck=getTruck(plateNum);
        if(truck==null){
            throw new Exception(String.format("A truck with plate number %d does not exist..",plateNum));
        }
        truckDAO.Delete(plateNum);
    }

    //change to protected before submitting
    protected boolean isAbleToDrive(LicenseType licenseType, int plateNum){
        Truck truck=truckDAO.Read(plateNum);
        if(truck!=null && licenseMapper.containsKey(licenseType)){
            return truck.getMaxWeight()<=licenseMapper.get(licenseType);
        }
        return false;
    }

    public ArrayList<Truck> getTrucks(){
        ArrayList<Truck> list=truckDAO.getAllTrucks();
        list.sort(Comparator.comparingInt(Truck::getMaxWeight));
        return list;
    }

    public Truck getTruck(int plateNum) throws Exception{
        Truck truck=truckDAO.Read(plateNum);
        if(truck==null){
            throw new Exception(String.format("A truck with plate number %d does not exist..",plateNum));
        }
        return truck;
    }
}
