package DataAccessLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.sql.*;
import BusinessLayer.*;
import BusinessLayer.Driver;


public class SiteDAO extends DataAccessObject {

    private HashMap<Integer, Site> siteCache;


    public SiteDAO(String tableName) {
        super(tableName);
        siteCache = new HashMap<>();
    }

    public boolean Create(Site site) {
        String sql = "INSERT INTO Sites(id, address, deliveryZone, phoneNumber, contactName, type) VALUES(?,?,?,?,?,?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, site.getId());
            pstmt.setString(2, site.getAddress());
            pstmt.setString(3, site.getDeliveryZone().toString());
            pstmt.setString(4, site.getPhoneNumber());
            pstmt.setString(5, site.getContactName());
            pstmt.setString(6, site.getType());
            if (pstmt.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
        siteCache.put(site.getId(), site);
        return true;
    }

    public Site Read(int id) {
        if (siteCache.containsKey(id)) {
            return siteCache.get(id);
        }
        String sql = "SELECT * FROM Sites WHERE id = (?)";
        Site site = null;
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            int resID = 0;
            String address = null;
            DeliveryZone deliveryZone = null;
            String phoneNumber = null;
            String contactName = null;
            while (rs.next()) {
                resID = rs.getInt("id");
                address = rs.getString("address");
                deliveryZone = DeliveryZone.valueOf(rs.getString("deliveryZone"));
                phoneNumber = rs.getString("phoneName");
                contactName = rs.getString("contactName");
                if (rs.getString("Type").equals("Branch"))
                    site = new Branch(resID, address, deliveryZone, phoneNumber, contactName);
                else
                    site = new SupplierWarehouse(resID, address, deliveryZone, phoneNumber, contactName);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        siteCache.put(id, site);
        return site;
    }


    public boolean Update(Site site) {
        int id = site.getId();
        String sql = "UPDATE Sites SET address = (?), deliveryZone = (?), phoneName = (?), contactName = (?) WHERE id = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, site.getAddress());
            pstmt.setString(2, site.getDeliveryZone().toString());
            pstmt.setString(3, site.getPhoneNumber());
            pstmt.setString(4, site.getContactName());
            pstmt.setInt(5, id);
            if (pstmt.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            return false; //TODO:send an Exception of the correct kind
        }
        return true;
    }

    public boolean Delete(int id) {
        String sql = "DELETE FROM Sites WHERE plateNum = (?)";
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
        siteCache.remove(id);
        return true;
    }

}

