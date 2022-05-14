package DeliveryModule.DataAccessLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.sql.*;
import DeliveryModule.BusinessLayer.*;
import DeliveryModule.BusinessLayer.Driver;
import javafx.util.Pair;

public class DeliveryArchiveDAO extends DataAccessObject {

    private HashMap<Integer, String> FinishedDeliveriesCache;
    int maxId;

    public DeliveryArchiveDAO() {
        super("DeliveryArchive");
        FinishedDeliveriesCache = new HashMap<>();
        maxId = -1;
    }

    public boolean addUpcomingDelivery(int id, String details) {
        String sql = "INSERT INTO FinishedDeliveries(id, details) VALUES(?,?)";
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
        FinishedDeliveriesCache.put(id, details);
        return true;
    }

    public Pair<Integer, String> Read(int id) {
        if (FinishedDeliveriesCache.containsKey(id)) {
            return new Pair<>(id, FinishedDeliveriesCache.get(id));
        }
        String sql = "SELECT * FROM FinishedDeliveries WHERE id = (?)";
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
        FinishedDeliveriesCache.put(id, data.getValue());
        return data;
    }


    /*public boolean Update(int id, String details) {

    }

    public boolean Delete(int id) {

    }*/


    public String getDeliveryRecord(int deliveryId){return Read(deliveryId).getValue();}

    //ToDo
    public ArrayList<String> getDeliveryArchive(){

        String sql = "SELECT * FROM FinishedDeliveries";
        ArrayList<String> archive = new ArrayList<String>();
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int resID = rs.getInt("id");
                String details = rs.getString("details");
                archive.add(details);
                if (!FinishedDeliveriesCache.containsKey(resID))
                    FinishedDeliveriesCache.put(resID, details);
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
        String sql = "SELECT Max(id) FROM FinishedDeliveries";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt("id");
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}