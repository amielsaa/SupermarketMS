package DAL;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

public class DaysToDeliverDAO extends DalController{
    public DaysToDeliverDAO() {
        super("DaysToDeliver");
    }

    //todo: add orderID to DAO

    public boolean insertDaysToDeliver(int bn, int orderID, int day)  {
        String sql = "INSERT INTO DaysToDeliver(bn, orderID, day) VALUES(?,?,?)";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setInt(2, orderID);
            pstmt.setInt(3, day);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean deleteDaysToDeliver(int bn, int orderID, int day)  {
        String sql = "DELETE FROM DaysToDeliver WHERE bn = ?, orderID = ?, day = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setInt(2, orderID);
            pstmt.setInt(3, day);
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


    //remove old days, insert new ones
    public boolean updateDeliveryDays(int bn, int orderID, Set<Integer> days){
        try{
            deleteAllDaysToDeliver(bn);
            for(int d : days){
                insertDaysToDeliver(bn,orderID,d);
            }
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    //returns days as integers
    public Collection<Integer> selectAllDays(int bn, int orderID){
        String sql = "select * from DaysToDeliver where bn = ?, orderID = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,bn);
            pstmt.setInt(2,orderID);
            ResultSet rs = pstmt.executeQuery();
            Collection<Integer> cc = new LinkedList<>();
            while (rs.next()) {
                cc.add(rs.getInt("day"));
            }
            return cc;

        } catch (SQLException e) {
            return null; //todo
        }

    }




}
