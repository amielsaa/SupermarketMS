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


    public DriverDAO(String tableName) {
        super(tableName);
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

}

