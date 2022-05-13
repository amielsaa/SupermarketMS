package DeliveryModule.DataAccessLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.sql.*;
import BusinessLayer.*;
import BusinessLayer.Driver;
import javafx.util.Pair;

public class FinishedDeliveriesDAO extends DataAccessObject {

    private HashMap<Integer, Pair<Integer, String>> FinishedDeliveriesCache;


    public FinishedDeliveriesDAO(String tableName) {
        super(tableName);
        FinishedDeliveriesCache = new HashMap<>();
    }

    public boolean Create(int id, String details) {
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
        FinishedDeliveriesCache.put(id, new Pair(id, details));
        return true;
    }

    public Pair<Integer, String> Read(int id) {
        if (FinishedDeliveriesCache.containsKey(id)) {
            return FinishedDeliveriesCache.get(id);
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
        FinishedDeliveriesCache.put(id, data);
        return data;
    }


    /*public boolean Update(int id, String details) {

    }

    public boolean Delete(int id) {

    }*/

}