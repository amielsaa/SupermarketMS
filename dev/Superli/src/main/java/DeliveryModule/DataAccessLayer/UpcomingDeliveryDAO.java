package DeliveryModule.DataAccessLayer;

import DeliveryModule.BusinessLayer.Branch;
import DeliveryModule.BusinessLayer.Delivery;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class UpcomingDeliveryDAO extends DataAccessObject {
    private HashMap<Integer, Delivery> upcomingDeliveryCache;
    private int maxId;

    public UpcomingDeliveryDAO()
    {
        super("UpcomingDeliveries");
        upcomingDeliveryCache = new HashMap<>();
        maxId = -1;
    }

    //ToDo
    public ArrayList<Delivery> getUpcomingDeliveries() {
        String sql = "SELECT * FROM Deliveries";
        ArrayList<Delivery> deliveries = new ArrayList<Delivery>();
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
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
                Delivery delivery = new Delivery(resID, weight, startTime, endTime, driverId, truckId, originId);
                deliveries.add(delivery);
                if (!upcomingDeliveryCache.containsKey(resID))
                    upcomingDeliveryCache.put(resID, delivery);

            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deliveries;
    }

    //ToDo
    public Delivery getUpcomingDelivery(int deliveryId) {

        if (upcomingDeliveryCache.containsKey(deliveryId)) {
            return upcomingDeliveryCache.get(deliveryId);
        }
        String sql = "SELECT * FROM Deliveries WHERE id = (?)";
        Delivery delivery = null;
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deliveryId);
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
        upcomingDeliveryCache.put(deliveryId, delivery);
        return delivery;
    }

    //ToDo: auto generate ids
    public Integer addUpcomingDelivery(Delivery delivery){
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
                return -1;
            }
            /*Set<Branch> destinations = delivery.getDestinations();
            for (Branch dest : destinations) {
                destinationsDAO.Create(dest.getId(), delivery.getId());
                HashMap<String,Integer> products = delivery.getProductsPerDestination(dest);
                for (Map.Entry pair:products.entrySet())
                    deliveredProductsDAO.Create(dest.getId(), delivery.getId(), (String) pair.getKey(), (Integer) pair.getValue());
            }*/
        } catch (SQLException e) {
            return -1;
        }
        maxId = Math.max(delivery.getId(), maxId);
        upcomingDeliveryCache.put(delivery.getId(), delivery);
        return delivery.getId();
    }

    public void setDriverId(int deliveryId, int newDriverId) {
        Update(getUpcomingDelivery(deliveryId));
    }

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

    public void setTruck(int deliveryId, int newTruckId) {
        Update(getUpcomingDelivery(deliveryId));
    }

    public void setWeight(int deliveryId, int weight) {
        Update(getUpcomingDelivery(deliveryId));
    }

    public void deleteUpcomingDelivery(int deliveryId) {
        String sql = "DELETE FROM Deliveries WHERE plateNum = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deliveryId);
            if (pstmt.executeUpdate() != 1) {
                return;
            }
        } catch (SQLException e) {
            return;
        }
        upcomingDeliveryCache.remove(deliveryId);
    }

    public int getMaxId()
    {
        if (maxId != -1)
            return maxId;
        int result = 0;
        String sql = "SELECT Max(id) as max FROM Deliveries";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt("max");
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
