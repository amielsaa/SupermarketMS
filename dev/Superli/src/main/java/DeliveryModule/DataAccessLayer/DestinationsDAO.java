package DeliveryModule.DataAccessLayer;

import java.util.*;
import java.sql.*;
import DeliveryModule.BusinessLayer.*;
import DeliveryModule.BusinessLayer.Driver;
import javafx.util.Pair;


public class DestinationsDAO extends DataAccessObject {

    //private HashSet<Pair<Integer,Integer>> destinationsCache;


    public DestinationsDAO(String tableName) {
        super(tableName);
        //destinationsCache = new HashSet<>();
    }

    public boolean Create(int siteId, int deliveryId) {
        String sql = "INSERT INTO Destinations(siteId, deliveryId) VALUES(?,?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, siteId);
            pstmt.setInt(2, deliveryId);
            if (pstmt.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
        //destinationsCache.add(new Pair<>(siteId, deliveryId));
        return true;
    }

    public Set<Integer> ReadDeliveriesPerSiteId(int siteId) {
        //if (destinationsCache) {
        //    return driverCache.get(id);
        //}
        String sql = "SELECT * FROM Destinations WHERE siteId = (?)";
        Set<Integer> deliveries = new HashSet<Integer>();
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, siteId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                deliveries.add(rs.getInt("deliveryId"));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deliveries;
    }

    public Set<Integer> ReadSitesPerDeliveryId(int deliveryId) {
        //if (destinationsCache) {
        //    return driverCache.get(id);
        //}
        String sql = "SELECT * FROM Destinations WHERE deliveryId = (?)";
        Set<Integer> sites = new HashSet<Integer>();
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deliveryId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                sites.add(rs.getInt("siteId"));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sites;
    }


    /*public boolean Update() {

    }*/

    public boolean Delete(int siteId, int deliveryId) {
        String sql = "DELETE FROM Destinations WHERE siteId = (?) and deliveryId = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, siteId);
            pstmt.setInt(2, deliveryId);
            if (pstmt.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            return false; //TODO:send an Exception of the correct kind
        }
        //driverCache.remove(id);
        return true;
    }

}

