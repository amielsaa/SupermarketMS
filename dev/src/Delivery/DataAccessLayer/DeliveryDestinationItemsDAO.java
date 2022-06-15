package Delivery.DataAccessLayer;

import Utilities.DataAccessObject;
import Utilities.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class DeliveryDestinationItemsDAO extends DataAccessObject {
    private LinkedHashMap<Pair<Integer,Integer>,HashMap<Pair<String, String>,Pair<Double, Integer>>> deliveryDestinationItemsCache;

    public DeliveryDestinationItemsDAO(){
        super("DeliveryDestinationItems");
        deliveryDestinationItemsCache = new LinkedHashMap<>();
    }

    public void Create(int deliveryId, int siteId, String item, String producer, double price, int quantity) {
        String sql = "INSERT INTO DeliveryDestinationItems(siteId, deliveryId, name, producerName, price, count) VALUES(?,?,?,?,?,?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, siteId);
            pstmt.setInt(2, deliveryId);
            pstmt.setString(3, item);
            pstmt.setString(4, producer);
            pstmt.setDouble(5, price);
            pstmt.setInt(4, quantity);
            if (pstmt.executeUpdate() != 1) {
                return;
            }
        } catch (SQLException e) {
            return;
        }
        insertItemToCache(deliveryId, siteId, item, producer, price, quantity);
    }

    public void removeItemFromDestination(int deliveryId, int siteId, String name, String producer) {

        String sql = "DELETE FROM DeliveryDestinationItems WHERE siteId = (?) and deliveryId = (?) and name = (?) and producerName = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, siteId);
            pstmt.setInt(2, deliveryId);
            pstmt.setString(3, name);
            pstmt.setString(4, producer);
            if (pstmt.executeUpdate() != 1) {
                return;
            }
        } catch (SQLException e) {
            return; //TODO:send an Exception of the correct kind
        }
        removeItemFromCache(deliveryId, siteId, name, producer);
    }

    public void removeItemsOfDestination(int deliveryId, int siteId) {
        String sql = "DELETE FROM DeliveryDestinationItems WHERE siteId = (?) and deliveryId = (?)";
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
        removeItemsFromCache(deliveryId, siteId);
    }

    public void editItemQuantity(int deliveryId, int siteId, String item, String producer, int quantity) {
        String sql = "UPDATE DeliveryDestinationItems SET count = (?) WHERE siteId = (?) and deliveryId = (?) and name = (?) and producerName = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, siteId);
            pstmt.setInt(3, deliveryId);
            pstmt.setString(4, item);
            if (pstmt.executeUpdate() != 1) {
                return;
            }
        } catch (SQLException e) {
            return; //TODO:send an Exception of the correct kind
        }
        editItemInCache(deliveryId,siteId,item,producer,quantity);
    }

    public HashMap<Pair<String, String>, Pair<Double, Integer>> getItemsOfDest(int deliveryId, int siteId) {
        Set<Pair<Integer,Integer>> keys = deliveryDestinationItemsCache.keySet();
        for (Pair<Integer,Integer> key : keys)
            if ((key.getKey().equals(deliveryId) && key.getValue().equals(siteId)))
                return deliveryDestinationItemsCache.get(key);
        String sql = "SELECT * FROM DeliveryDestinationItems WHERE siteId = (?) and deliveryId = (?)";
        HashMap<Pair<String, String>, Pair<Double, Integer>> products = new HashMap<>();
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, siteId);
            pstmt.setInt(2, deliveryId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                products.put(new Pair<>(rs.getString("name"), rs.getString("producerName")),
                             new Pair<>(rs.getDouble("price"),rs.getInt("count")));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    private void insertItemToCache(int deliveryId, int siteId, String item, String producer, double price, int quantity)
    {
        Pair<Integer, Integer> foundKey = null;
        Set<Pair<Integer,Integer>> keys = deliveryDestinationItemsCache.keySet();
        for (Pair<Integer,Integer> key : keys)
            if ((key.getKey().equals(deliveryId) && key.getValue().equals(siteId)))
                foundKey = key;
        if (foundKey == null) {
            foundKey = new Pair<>(deliveryId, siteId);
            deliveryDestinationItemsCache.put(foundKey, new HashMap<>());
        }
        deliveryDestinationItemsCache.get(foundKey).put(new Pair<>(item, producer), new Pair<>(price, quantity));
    }

    private void editItemInCache(int deliveryId, int siteId, String item, String producer, int quantity)
    {
        Pair<Integer, Integer> foundKey = null;
        Set<Pair<Integer,Integer>> keys = deliveryDestinationItemsCache.keySet();
        for (Pair<Integer,Integer> key : keys)
            if ((key.getKey().equals(deliveryId) && key.getValue().equals(siteId)))
                foundKey = key;
        if (foundKey == null)
            return;
        deliveryDestinationItemsCache.get(foundKey).get(new Pair<>(item, producer)).setValue(quantity);
    }

    private void removeItemFromCache(int deliveryId, int siteId, String item, String producer)
    {
        Pair<Integer, Integer> foundKey = null;
        Set<Pair<Integer,Integer>> keys = deliveryDestinationItemsCache.keySet();
        for (Pair<Integer,Integer> key : keys)
            if ((key.getKey().equals(deliveryId) && key.getValue().equals(siteId)))
                foundKey = key;
        if (foundKey == null)
            return;
        deliveryDestinationItemsCache.get(foundKey).remove(new Pair<>(item, producer));
        if (deliveryDestinationItemsCache.get(foundKey).size() == 0)
            deliveryDestinationItemsCache.remove(foundKey);
    }

    private void removeItemsFromCache(int deliveryId, int siteId)
    {
        Set<Pair<Integer,Integer>> keys = deliveryDestinationItemsCache.keySet();
        for (Pair<Integer,Integer> key : keys)
            if ((key.getKey().equals(deliveryId) && key.getValue().equals(siteId)))
                deliveryDestinationItemsCache.remove(key);
    }
}
