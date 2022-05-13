package DeliveryModule.DataAccessLayer;

import java.util.*;
import java.sql.*;
import DeliveryModule.BusinessLayer.*;
import DeliveryModule.BusinessLayer.Driver;
import javafx.util.Pair;


public class DeliveredProductsDAO extends DataAccessObject {

    private HashMap<Pair<Integer, Integer>, Set<Pair<String, Integer>>> deliveredProductsCache;


    public DeliveredProductsDAO(String tableName) {
        super(tableName);
        deliveredProductsCache = new HashMap<>();
    }

    public boolean Create(int siteId, int deliveryId, String name, int count) {
        String sql = "INSERT INTO DeliveredProducts(siteId, deliveryId, name, count) VALUES(?,?,?,?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, siteId);
            pstmt.setInt(2, deliveryId);
            pstmt.setString(3, name);
            pstmt.setInt(4, count);
            if (pstmt.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
        //driverCache.put(driver.getId(), driver);
        return true;
    }

    public Set<Pair<String, Integer>> Read(int siteId, int deliveryId) {
        //if (driverCache.containsKey(id)) {
        //    return driverCache.get(id);
        //}
        String sql = "SELECT * FROM DeliveredProducts WHERE siteId = (?) and deliveryId = (?)";
        Set<Pair<String, Integer>> products = new HashSet<>();
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, siteId);
            pstmt.setInt(2, deliveryId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                products.add(new Pair<>(rs.getString("name"), rs.getInt("count")));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //driverCache.put(id, driver);
        return products;
    }


    public boolean EditItemCount(int siteId, int deliveryId, String name, int count) {
        String sql = "UPDATE DeliveredProducts SET count = (?) WHERE siteId = (?) and deliveryId = (?) and name = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, count);
            pstmt.setInt(2, siteId);
            pstmt.setInt(3, deliveryId);
            pstmt.setString(4, name);
            if (pstmt.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            return false; //TODO:send an Exception of the correct kind
        }
        return true;
    }

    public boolean Delete(int siteId, int deliveryId, String name) {
        String sql = "DELETE FROM DeliveredProducts WHERE siteId = (?) and deliveryId = (?) and name = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, siteId);
            pstmt.setInt(2, deliveryId);
            pstmt.setString(3, name);
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

