package Suppliers.DAL;

import misc.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class QuantityAgreementDAO extends DalController{
    public QuantityAgreementDAO() {
        super("QuantityAgreement");
    }

    public boolean insertQuantityAgreement(int bn, String itemname, String producer, double price)  {
        String sql = "INSERT INTO QuantityAgreement(bn, itemname, producer, price) VALUES(?,?,?,?)";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, itemname);
            pstmt.setString(3, producer);
            pstmt.setDouble(4, price);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean deleteQuantityAgreement(int bn, String itemname, String producer)  {
        String sql = "DELETE FROM QuantityAgreement WHERE bn = ?, itemname = ?, producer = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, itemname);
            pstmt.setString(3, producer);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public HashMap<Pair<String,String>, Double> selectAllItems(int bn){
        String sql = "select * from QuantityAgreement where bn = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,bn);
            ResultSet rs = pstmt.executeQuery();
            HashMap<Pair<String,String>, Double> cc = new HashMap<>();
            Pair item;
            while (rs.next()) {
                item = new Pair(rs.getString("itemname"), rs.getString("producer"));
                cc.put(item, rs.getDouble("price"));
            }
            return cc;

        } catch (SQLException e) {
            return null;
        }

    }



}
