package DeliveryModule.DataAccessLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.sql.*;
import DeliveryModule.BusinessLayer.*;
import DeliveryModule.BusinessLayer.Driver;


public class DriverDAO extends DataAccessObject {

    private HashMap<Integer, Driver> driverCache;


    public DriverDAO() {
        super("Drivers");
        driverCache = new HashMap<>();
    }

    public boolean Create(Driver driver) {
        String sql = "INSERT INTO Drivers(id, name, licenseType) VALUES(?,?,?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, driver.getId());
            pstmt.setString(2, driver.getName());
            pstmt.setString(3, driver.getLicenseType().toString());
            if (pstmt.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
        driverCache.put(driver.getId(), driver);
        return true;
    }

    public Driver Read(int id) {
        if (driverCache.containsKey(id)) {
            return driverCache.get(id);
        }
        String sql = "SELECT * FROM Drivers WHERE id = (?)";
        Driver driver = null;
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            int resID = 0;
            String name = null;
            LicenseType licenseType = null;
            while (rs.next()) {
                resID = rs.getInt("id");
                name = rs.getString("name");
                licenseType = LicenseType.valueOf(rs.getString("licenseType"));

                driver = new Driver(resID, name, licenseType);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        driverCache.put(id, driver);
        return driver;
    }


    public boolean Update(Driver driver) {
        int id = driver.getId();
        String sql = "UPDATE Drivers SET name = (?), licenseType = (?), WHERE id = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, driver.getName());
            pstmt.setString(2, driver.getLicenseType().toString());
            pstmt.setInt(3, id);
            if (pstmt.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            return false; //TODO:send an Exception of the correct kind
        }
        return true;
    }

    public boolean Delete(int id) {
        String sql = "DELETE FROM Drivers WHERE plateNum = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            if (pstmt.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            return false; //TODO:send an Exception of the correct kind
        }
        driverCache.remove(id);
        return true;
    }

    //Todo
    public boolean updateLicenseType(int id, String newLicenseType){
        return Update(Read(id));
    }

    //ToDo
    public boolean updateName(int id, String newName){
        return Update(Read(id));
    }

    //ToDo
    public ArrayList<Driver> readAll(){

        String sql = "SELECT * FROM Drivers";
        ArrayList<Driver> drivers = new ArrayList();
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int resID = 0;
            String name = null;
            LicenseType licenseType = null;
            while (rs.next()) {
                resID = rs.getInt("id");
                name = rs.getString("name");
                licenseType = LicenseType.valueOf(rs.getString("licenseType"));

                Driver driver = new Driver(resID, name, licenseType);
                drivers.add(driver);
                if (!driverCache.containsKey(resID))
                    driverCache.put(resID, driver);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drivers;
    }

}

