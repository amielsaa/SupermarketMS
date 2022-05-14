package DeliveryModule.DataAccessLayer;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.*;
import DeliveryModule.BusinessLayer.*;
import DeliveryModule.BusinessLayer.Driver;
import javafx.util.Pair;
//todo: take whatever you need to upcomingDeliveryDAO!

public class DeliveryDAO extends DataAccessObject {

    private HashMap<Integer, Delivery> deliveryCache;
    private int maxId;

    public DeliveryDAO()
    {
        super("Deliveries");
        deliveryCache = new HashMap<>();
        maxId = -1;
    }

    public boolean Create(Delivery delivery) {
        String sql = "INSERT INTO Deliveries(id, startDate ,startTime, endDate, endTime, weight, truckPlateNum, driverId, originId) VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, delivery.getId());
            pstmt.setDate(2, Date.valueOf(delivery.getStartTime().toLocalDate()));
            pstmt.setTime(3, Time.valueOf(delivery.getStartTime().toLocalTime()));
            pstmt.setDate(4, Date.valueOf(delivery.getEndTime().toLocalDate()));
            pstmt.setTime(5, Time.valueOf(delivery.getEndTime().toLocalTime()));
            pstmt.setInt(6, delivery.getWeight());
            pstmt.setInt(7, delivery.getTruckId());
            pstmt.setInt(8, delivery.getDriverId());
            pstmt.setInt(9, delivery.getOriginSiteId());
            if (pstmt.executeUpdate() != 1) {
                return false;
            }
            /*Set<Branch> destinations = delivery.getDestinations();
            for (Branch dest : destinations) {
                destinationsDAO.Create(dest.getId(), delivery.getId());
                HashMap<String,Integer> products = delivery.getProductsPerDestination(dest);
                for (Map.Entry pair:products.entrySet())
                    deliveredProductsDAO.Create(dest.getId(), delivery.getId(), (String) pair.getKey(), (Integer) pair.getValue());
            }*/
        } catch (SQLException e) {
            return false;
        }
        maxId = Math.max(delivery.getId(), maxId);
        deliveryCache.put(delivery.getId(), delivery);
        return true;
    }

    public Delivery Read(int id) {
        if (deliveryCache.containsKey(id)) {
            return deliveryCache.get(id);
        }
        String sql = "SELECT * FROM Deliveries WHERE id = (?)";
        Delivery delivery = null;
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            int resID = 0;
            LocalDateTime startTime = null;
            LocalDateTime endTime = null;
            int weight = 0;
            int truckId;
            int driverId;
            int originId;
            Set<Branch> destinations = null;
            LinkedHashMap<Branch, HashMap<String,Integer>> products = null;
            while (rs.next()) {
                resID = rs.getInt("id");
                startTime = rs.getDate("startDate").toLocalDate().atTime(rs.getTime("startTime").toLocalTime());
                endTime = rs.getDate("endDate").toLocalDate().atTime(rs.getTime("endTime").toLocalTime());
                weight = rs.getInt("weight");
                truckId = rs.getInt("truckPlateNum");
                driverId = rs.getInt("driverId");
                originId = rs.getInt("originId");

                //destinations = getDestinations(id);
                //products = getProducts(id, destinations);
                delivery = new Delivery(resID, weight, startTime, endTime, driverId, truckId, originId);

            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        deliveryCache.put(id, delivery);
        return delivery;
    }

    public int getMaxId()
    {
        if (maxId != -1)
            return maxId;
        int result = 0;
        String sql = "SELECT Max(id) FROM Deliveries";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt("id");
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /*private Set<Branch> getDestinations(int deliveryId) {
        Set<Integer> idlst = destinationsDAO.ReadSitesPerDeliveryId(deliveryId);
        Set<Branch> rslt = new HashSet<>();
        for (int siteId: idlst) {
            Site s = siteDAO.Read(siteId);
            if (s instanceof Branch)
                rslt.add((Branch) s);
        }
        return rslt;
    }

    private LinkedHashMap<Branch, HashMap<String, Integer>> getProducts(int deliveryId, Set<Branch> destinations) {
        LinkedHashMap<Branch, HashMap<String, Integer>> res = new LinkedHashMap<>();
        for (Branch branch : destinations)
        {
            HashMap<String, Integer> items = new HashMap<>();
            Set<Pair<String,Integer>> readItems = deliveredProductsDAO.Read(branch.getId(), deliveryId);
            for (Pair<String,Integer> item : readItems)
                items.put(item.getKey(), item.getValue());
            res.put(branch, items);
        }
        return res;
    }*/


    public boolean Update(Delivery delivery) {
        int id = delivery.getId();
        String sql = "UPDATE Deliveries SET startDate = (?) ,startTime = (?) , endDate = (?) , endTime = (?) , weight = (?) , truckPlateNum = (?) , driverId = (?) , originId = (?)  WHERE id = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(delivery.getStartTime().toLocalDate()));
            pstmt.setTime(2, Time.valueOf(delivery.getStartTime().toLocalTime()));
            pstmt.setDate(3, Date.valueOf(delivery.getEndTime().toLocalDate()));
            pstmt.setTime(4, Time.valueOf(delivery.getEndTime().toLocalTime()));
            pstmt.setInt(5, delivery.getWeight());
            pstmt.setInt(6, delivery.getTruckId());
            pstmt.setInt(7, delivery.getDriverId());
            pstmt.setInt(8, delivery.getOriginSiteId());
            pstmt.setInt(9, delivery.getId());
            if (pstmt.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            return false; //TODO:send an Exception of the correct kind
        }
        return true;
    }

    public boolean Delete(int id) {
        String sql = "DELETE FROM Deliveries WHERE plateNum = (?)";
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
        deliveryCache.remove(id);
        return true;
    }

}