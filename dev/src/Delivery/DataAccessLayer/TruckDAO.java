package Delivery.DataAccessLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.*;
import Delivery.BusinessLayer.*;
import Utilities.DataAccessObject;


public class TruckDAO extends DataAccessObject {

    private HashMap<Integer, Truck> truckCache;

    public TruckDAO() {
        super("Trucks");
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
        if(truck!=null)
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
            int resPlateNum;
            String model;
            int maxWeight;
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

    public void setPlateNum(int oldPlateNum, int newPlateNum) throws Exception {
        Truck t = Read(oldPlateNum);
        if (t != null) {
            Delete(oldPlateNum);
            t.setPlateNum(newPlateNum);
            Create(t);
        }
    }

    public ArrayList<Truck> getAllTrucks(){
        String sql = "SELECT * FROM Trucks";
        ArrayList<Truck> trucks = new ArrayList<>();
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int resPlateNum;
            String model;
            int maxWeight;
            while (rs.next()) {
                resPlateNum = rs.getInt("plateNum");
                model = rs.getString("model");
                maxWeight = rs.getInt("maxWeight");

                Truck truck = new Truck(resPlateNum, model, maxWeight);
                trucks.add(truck);
                if (!truckCache.containsKey(resPlateNum))
                    truckCache.put(resPlateNum, truck);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trucks;
    }

}

