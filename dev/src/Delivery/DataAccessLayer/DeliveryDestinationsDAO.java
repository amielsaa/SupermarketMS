package DeliveryModule.DataAccessLayer;

import Utilities.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DeliveryDestinationsDAO extends DataAccessObject {
    LinkedHashMap<Integer, LinkedList<Integer>> deliveryDestinationCache;


    public DeliveryDestinationsDAO() {
        super("DeliveryDestinations");
        this.deliveryDestinationCache = new LinkedHashMap<>();
    }

    public void Create(int deliveryId, int siteId) {
        String sql = "INSERT INTO DeliveryDestinations(siteId, deliveryId) VALUES(?,?)";
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

    public void Delete(int deliveryId, int siteId) {
        String sql = "DELETE FROM DeliveryDestinations WHERE siteId = (?) and deliveryId = (?)";
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

    public LinkedList<Integer> Read(int deliveryId){
        String sql = "SELECT * FROM DeliveryDestinations WHERE deliveryId = (?)";
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
