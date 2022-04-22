package BusinessLayer;

import java.util.HashMap;

public class DriversController {
    private HashMap<Integer,Driver> drivers;

    public DriversController() {
        drivers=new HashMap<>();
    }

    public void load(){
        addDriver(200000001, "c1 driver1", "C1");
        addDriver(200000002, "c1 driver2", "C1");
        addDriver(200000003, "c1 driver3", "C1");
        addDriver(200000004, "c driver1", "C");
        addDriver(200000005, "c driver2", "C");
        addDriver(200000006, "c driver3", "C");
    }

    public void addDriver(int id,String name,String licenseType){
        drivers.put(id,new Driver(id,name,LicenseType.valueOf(licenseType)));
    }
    public Driver getDriver(int driverId) throws Exception {
        if(!drivers.containsKey(driverId)){
            throw new Exception(String.format("Driver id %d does not exist",driverId));
        }
        return drivers.get(driverId);
    }

}
