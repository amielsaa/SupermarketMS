package Delivery.DataAccessLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.*;
import Delivery.BusinessLayer.*;
import Utilities.DataAccessObject;


public class SiteDAO extends DataAccessObject {

    private HashMap<Integer, Site> siteCache;
    private int maxId;


    public SiteDAO() {
        super("Sites");
        siteCache = new HashMap<>();
        maxId = -1;
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
        if(site!=null) {
            siteCache.put(site.getId(), site);
            maxId = Math.max(site.getId(), maxId);
        }
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
            int resID;
            String address;
            DeliveryZone deliveryZone;
            String phoneNumber;
            String contactName;
            while (rs.next()) {
                resID = rs.getInt("id");
                address = rs.getString("address");
                deliveryZone = DeliveryZone.valueOf(rs.getString("deliveryZone"));
                phoneNumber = rs.getString("phoneNumber");
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
        String sql = "UPDATE Sites SET address = (?), deliveryZone = (?), phoneNumber = (?), contactName = (?) WHERE id = (?)";
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
        String sql = "DELETE FROM Sites WHERE id = (?)";
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

    public Site getSite(String address){
        for (Site site : siteCache.values()) {
            if (site!=null && site.getAddress().equals(address))
                return site;
        }
        String sql = "SELECT id FROM Sites WHERE address = (?)";
        Site site = null;
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                site = Read(rs.getInt("id"));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return site;
    }

    public ArrayList<Site> getAllSites(){
        String sql = "SELECT * FROM Sites";
        ArrayList<Site> sites = new ArrayList<>();
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            Site site;
            while (rs.next()) {
                int resID = rs.getInt("id");
                String address = rs.getString("address");
                DeliveryZone deliveryZone = DeliveryZone.valueOf(rs.getString("deliveryZone"));
                String phoneNumber = rs.getString("phoneNumber");
                String contactName = rs.getString("contactName");
                if (rs.getString("Type").equals("Branch"))
                    site = new Branch(resID, address, deliveryZone, phoneNumber, contactName);
                else
                    site = new SupplierWarehouse(resID, address, deliveryZone, phoneNumber, contactName);
                sites.add(site);
                if (!siteCache.containsKey(resID))
                    siteCache.put(resID, site);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sites;
    }

    public int getMaxId()
    {
        if (maxId != -1)
            return maxId;
        int result = 0;
        String sql = "SELECT Max(id) as max FROM Sites";
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

