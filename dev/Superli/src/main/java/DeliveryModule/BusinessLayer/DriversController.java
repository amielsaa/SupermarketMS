package DeliveryModule.BusinessLayer;

import DeliveryModule.DataAccessLayer.DriverDAO;

import java.util.ArrayList;
import java.util.HashMap;

public class DriversController {
    //private HashMap<Integer,Driver> drivers;
    private DriverDAO driverDAO;

    public DriversController() {
        //drivers=new HashMap<>();
        this.driverDAO = new DriverDAO();
    }

    public void load() throws Exception {
        addDriver(200000001, "c1 driver1", "C1");
        addDriver(200000002, "c1 driver2", "C1");
        addDriver(200000003, "c1 driver3", "C1");
        addDriver(200000004, "c driver1", "C");
        addDriver(200000005, "c driver2", "C");
        addDriver(200000006, "c driver3", "C");
    }

    public void addDriver(int id,String name,String licenseType) throws Exception {
        validateLicenseType(licenseType);
        driverDAO.Create(new Driver(id,name,LicenseType.valueOf(licenseType)));
      //  drivers.put(id,new Driver(id,name,LicenseType.valueOf(licenseType)));
    }

    private void validateLicenseType(String licenseType) throws Exception {
        try{
            LicenseType.valueOf(licenseType);
        }
        catch (Exception e) {
            throw new Exception("license type can be only C or C1");
        }
    }

    public Driver getDriver(int driverId) throws Exception {
        /*
        if(!drivers.containsKey(driverId)){
            throw new Exception(String.format("Driver with id %d does not exist...",driverId));
        }

         */
        return driverDAO.Read(driverId);
    }

    public ArrayList<Driver> getAllDrivers(){
       // ArrayList<Driver> list=new ArrayList<>(drivers.values());
        //return list;
        return driverDAO.readAll();
    }


    public void deleteDriver(int id) throws Exception{
       // drivers.remove(getDriver(id));
        if(!driverDAO.Delete(id))
            throw new Exception(String.format("Driver with id %d does not exist..",id));
    }

    public void changeName(int id, String name) throws Exception {
        /*
        Driver d = getDriver(id);
        d.setName(name);
         */
        Driver driver=driverDAO.Read(id);
        driver.setName(name);
        driverDAO.updateName(id,name);
    }

    public void ChangeLicenseType(int id, String licenseType) throws Exception {
        /*
        Driver d = getDriver(id);
        validateLicenseType(licenseType);
        d.setLicenseType(LicenseType.valueOf(licenseType));
         */
        Driver driver=driverDAO.Read(id);
        validateLicenseType(licenseType);
        driver.setLicenseType(LicenseType.valueOf(licenseType));
        driverDAO.updateLicenseType(id,licenseType);
    }
}
