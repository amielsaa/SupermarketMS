package Suppliers.DAL;

import misc.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DiscountsDAO extends DalController{
    public DiscountsDAO() {
        super("Discounts");
    }


    public HashMap<Pair<String,String>, HashMap<Integer,Integer>> selectAllDiscounts(int bn){
        String sql = "select * from Discounts where bn = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,bn);
            ResultSet rs = pstmt.executeQuery();
            HashMap<Pair<String,String>,HashMap<Integer,Integer>> cc = new HashMap<>();
            Pair item;
            while (rs.next()) {
                item = new Pair(rs.getString("itemname"), rs.getString("producer"));
                if(!cc.containsKey(item))
                    cc.put(item, new HashMap<Integer,Integer>());
                cc.get(item).put(rs.getInt("itemamount"), rs.getInt("itemdiscount"));
            }
            return cc;

        } catch (SQLException e) {
            return null; //todo
        }

    }


    public boolean insertDiscount(int bn, String itemname, String producer, int itemamount, int itemdiscount)  {
        String sql = "INSERT INTO Discounts(bn, itemname, producer, itemamount, itemdiscount) VALUES(?,?,?,?,?)";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, itemname);
            pstmt.setString(3, producer);
            pstmt.setInt(4, itemamount);
            pstmt.setInt(5, itemdiscount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean deleteDiscount(int bn, String itemname, String producer, int itemamount)  {
        String sql = "DELETE FROM Discounts WHERE bn = ?, itemname = ?, producer = ?, itemamount = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, itemname);
            pstmt.setString(3, producer);
            pstmt.setInt(4, itemamount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }





}
