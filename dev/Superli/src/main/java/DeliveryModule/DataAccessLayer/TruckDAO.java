package DeliveryModule.DataAccessLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.sql.*;
import BusinessLayer.*;


public class TruckDAO extends DataAccessObject {

    private HashMap<Integer, Truck> truckCache;


    public TruckDAO(String tableName) {
        super(tableName);
        truckCache = new HashMap<>();
    }

    public boolean Create(Truck truck) {
        String sql = "INSERT INTO Trucks(plateNum, model, maxWeight) VALUES(?,?,?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, truck.getPlateNum());
            pstmt.setString(2, truck.getModel());
            pstmt.setInt(3, truck.getMaxWeight());
            if (pstmt.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
        truckCache.put(truck.getPlateNum(), truck);
        return true;
    }

    public Truck Read(int plateNum) {
        if (truckCache.containsKey(plateNum)) {
            return truckCache.get(plateNum);
        }
        String sql = "SELECT * FROM Trucks WHERE plateNum = (?)";
        Truck truck = null;
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, plateNum);
            ResultSet rs = pstmt.executeQuery();
            int resPlateNum = 0;
            String model = null;
            int maxWeight = 0;
            while (rs.next()) {
                resPlateNum = rs.getInt("plateNum");
                model = rs.getString("model");
                maxWeight = rs.getInt("maxWeight");

                truck = new Truck(resPlateNum, model, maxWeight);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        truckCache.put(plateNum, truck);
        return truck;
    }


    public boolean Update(Truck truck) {
        int plateNum = truck.getPlateNum();
        String sql = "UPDATE trucks SET model = (?), maxWeight = (?) WHERE plateNum = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, truck.getModel());
            pstmt.setInt(2, truck.getMaxWeight());
            pstmt.setInt(3, plateNum);
            if (pstmt.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            return false; //TODO:send an Exception of the correct kind
        }
        return true;
    }

    public boolean Delete(int plateNum) {
        String sql = "DELETE FROM trucks WHERE plateNum = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, plateNum);
            if (pstmt.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            return false; //TODO:send an Exception of the correct kind
        }
        truckCache.remove(plateNum);
        return true;
    }

}

