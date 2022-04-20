package BusinessLayer;

import java.util.HashMap;

public class DriversController {
    private HashMap<Integer,Driver> drivers;

    public DriversController() {
        drivers=new HashMap<>();
    }

    public void addDriver(int id,String name,String licenseType){
        drivers.put(id,new Driver(id,name,LicenseType.valueOf(name)));
    }
    public Driver getDriver(int driverId) throws Exception {
        if(!drivers.containsKey(driverId)){
            throw new Exception(String.format("Driver id %d does not exist",driverId));
        }
        return drivers.get(driverId);
    }
}
