package DeliveryModule.DataAccessLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.*;

import Utilities.DataAccessObject;
import javafx.util.Pair;

public class DeliveryArchiveDAO extends DataAccessObject {

    private HashMap<Integer, String> DeliveryArchiveCache;
    int maxId;

    public DeliveryArchiveDAO() {
        super("DeliveryArchive");
        DeliveryArchiveCache = new HashMap<>();
        maxId = -1;
    }

    public boolean Create(int id, String details) {
        String sql = "INSERT INTO DeliveryArchive(id, details) VALUES(?,?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, details);
            if (pstmt.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
        maxId = Math.max(id, maxId);
        DeliveryArchiveCache.put(id, details);
        return true;
    }

    public Pair<Integer, String> Read(int id) {
        if (DeliveryArchiveCache.containsKey(id)) {
            return new Pair<>(id, DeliveryArchiveCache.get(id));
        }
        String sql = "SELECT * FROM DeliveryArchive WHERE id = (?)";
        Pair<Integer, String> data = null;
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            int resID = 0;
            String details = null;
            while (rs.next()) {
                resID = rs.getInt("id");
                details = rs.getString("details");

                data = new Pair(resID, details);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DeliveryArchiveCache.put(id, data.getValue());
        return data;
    }


    /*public boolean Update(int id, String details) {

    }

    public boolean Delete(int id) {

    }*/


    public String getDeliveryRecord(int deliveryId){return Read(deliveryId).getValue();}

    //ToDo
    public ArrayList<String> getDeliveryArchive(){

        String sql = "SELECT * FROM DeliveryArchive";
        ArrayList<String> archive = new ArrayList<String>();
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int resID = rs.getInt("id");
                String details = rs.getString("details");
                archive.add(details);
                if (!DeliveryArchiveCache.containsKey(resID))
                    DeliveryArchiveCache.put(resID, details);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return archive;
    }

    public int getMaxId()
    {
        if (maxId != -1)
            return maxId;
        int result = 0;
        String sql = "SELECT Max(id) as max FROM DeliveryArchive";
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