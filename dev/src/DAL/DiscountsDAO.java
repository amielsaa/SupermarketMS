package DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class DiscountsDAO extends DalController{
    public DiscountsDAO() {
        super("Discounts");
    }


    //returns as object array - indexes 0 3 4 are Integers, 1 2 are Strings
    public Collection<Object[]> selectAllDiscounts(int bn){
        String sql = "select * from Discounts where bn = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,bn);
            ResultSet rs = pstmt.executeQuery();
            Collection<Object[]> cc = new LinkedList<>();
            Object[] line;
            while (rs.next()) {
                line = new Object[5];
                line[0] = rs.getInt("bn");
                line[1] = rs.getString("itemname");
                line[2] = rs.getString("producer");
                line[3] = rs.getInt("itemamount");
                line[4] = rs.getInt("itemdiscount");
                cc.add(line);
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
