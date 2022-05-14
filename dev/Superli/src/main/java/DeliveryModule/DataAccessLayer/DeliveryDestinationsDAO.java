package DeliveryModule.DataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DeliveryDestinationsDAO extends DataAccessObject {
    LinkedHashMap<Integer, LinkedList<Integer>> deliveryDestinationCache;


    public DeliveryDestinationsDAO() {
        super("DeliveredProducts");
        this.deliveryDestinationCache = new LinkedHashMap<>();
    }

    public void addDeliveryDestination(int deliveryId,int siteId) {
        String sql = "INSERT INTO Destinations(siteId, deliveryId) VALUES(?,?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, siteId);
            pstmt.setInt(2, deliveryId);
            if (pstmt.executeUpdate() != 1) {
                return;
            }
        } catch (SQLException e) {
            return;
        }
        if (!deliveryDestinationCache.containsKey(deliveryId))
             deliveryDestinationCache.put(deliveryId, new LinkedList());
        deliveryDestinationCache.get(deliveryId).add(siteId);
    }

    public void removeDeliveryDestination(int deliveryId,int siteId) {
        String sql = "DELETE FROM Destinations WHERE siteId = (?) and deliveryId = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, siteId);
            pstmt.setInt(2, deliveryId);
            if (pstmt.executeUpdate() != 1) {
                return;
            }
        } catch (SQLException e) {
            return; //TODO:send an Exception of the correct kind
        }
        deliveryDestinationCache.get(deliveryId).remove((Integer) siteId);
        if (deliveryDestinationCache.get(deliveryId).size() == 0)
            deliveryDestinationCache.remove(deliveryId);
    }

    public LinkedList<Integer> getDeliveryDestinations(int deliveryId){
        String sql = "SELECT * FROM Destinations WHERE deliveryId = (?)";
        LinkedList<Integer> sites = new LinkedList<>();
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
}
