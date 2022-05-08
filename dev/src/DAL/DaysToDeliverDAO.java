package DAL;

import BusinessLayer.Supplier;
import misc.Days;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class DaysToDeliverDAO extends DalController{
    public DaysToDeliverDAO() {
        super("DaysToDeliver");
    }


    public boolean insertDaysToDeliver(int bn, int day)  {
        String sql = "INSERT INTO DaysToDeliver(bn, day) VALUES(?,?)";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setInt(2, day);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean deleteDaysToDeliver(int bn, int day)  {
        String sql = "DELETE FROM DaysToDeliver WHERE bn = ?, day = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setInt(2, day);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private boolean deleteAllDaysToDeliver(int bn)  {
        String sql = "DELETE FROM DaysToDeliver WHERE bn = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }


    public boolean updateSupplierDeliveryDays(int bn, Set<Integer> days){
        //remove old days, insert new ones
        try{
            deleteAllDaysToDeliver(bn);
            for(int d : days){
                insertDaysToDeliver(bn, d);
            }
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

}
