package DeliveryModule.DataAccessLayer;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.*;
import BusinessLayer.*;
import BusinessLayer.Driver;
import javafx.util.Pair;

import javax.print.attribute.standard.Destination;


public class DeliveryDAO extends DataAccessObject {

    private HashMap<Integer, Delivery> deliveryCache;


    public DeliveryDAO(String tableName) {
        super(tableName);
        deliveryCache = new HashMap<>();
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
            pstmt.setInt(7, delivery.getTruck().getPlateNum());
            pstmt.setInt(8, delivery.getDriver().getId());
            pstmt.setInt(9, delivery.getOrigin().getId());
            if (pstmt.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
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
            Truck truck = null;
            Driver driver = null;
            Site origin = null;
            Set<Branch> destinations = null;
            LinkedHashMap<Branch, HashMap<String,Integer>> products = null;
            while (rs.next()) {
                resID = rs.getInt("id");
                startTime = rs.getDate("startDate").toLocalDate().atTime(rs.getTime("startTime").toLocalTime());
                endTime = rs.getDate("endDate").toLocalDate().atTime(rs.getTime("endTime").toLocalTime());
                weight = rs.getInt("weight");
                TruckDAO truckDAO = new TruckDAO("Trucks");
                DriverDAO driverDAO = new DriverDAO("Drivers");
                SiteDAO siteDAO = new SiteDAO("Sites");
                truck = truckDAO.Read(rs.getInt("truckPlateNum"));
                driver = driverDAO.Read(rs.getInt("driverId"));
                origin = siteDAO.Read(rs.getInt("originId"));

                destinations = getDestinations(id);
                products = getProducts(id, destinations);
                //TODO insert products
                delivery = new Delivery(resID, startTime, endTime, weight, driver, truck, origin, destinations, products);

            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        deliveryCache.put(id, delivery);
        return delivery;
    }


    private Set<Branch> getDestinations(int deliveryId) {
        DestinationsDAO destinationsDAO = new DestinationsDAO("Destinations");
        SiteDAO siteDAO = new SiteDAO("Sites");
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
        DeliveredProductsDAO deliveredProductsDAO = new DeliveredProductsDAO("DeliveredProducts");
        for (Branch branch : destinations)
        {
            HashMap<String, Integer> items = new HashMap<>();
            Set<Pair<String,Integer>> readItems = deliveredProductsDAO.Read(branch.getId(), deliveryId);
            for (Pair<String,Integer> item : readItems)
                items.put(item.getKey(), item.getValue());
            res.put(branch, items);
        }
        return res;
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
            pstmt.setInt(6, delivery.getTruck().getPlateNum());
            pstmt.setInt(7, delivery.getDriver().getId());
            pstmt.setInt(8, delivery.getOrigin().getId());
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