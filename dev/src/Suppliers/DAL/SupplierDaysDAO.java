package Suppliers.DAL;

import misc.Days;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class SupplierDaysDAO extends DalController{

    public SupplierDaysDAO() {
        super("SupplierDays");
    }


    //-----------------------------------------------------------------------------------------------------------------------------------------------
    public boolean insertSupplierDays(int bn, int day)  {
        String sql = "INSERT INTO SupplierDays(bn, day) VALUES(?,?)";

        try(Connection conn = this.makeConnection()){
            //Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setInt(2, day);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean deleteSupplierDays(int bn, int day)  {
        String sql = "DELETE FROM SupplierDays WHERE bn = ? and day = ?";

        try(Connection conn = this.makeConnection()){
            //Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setInt(2, day);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private boolean deleteAllSupplierDays(int bn)  {
        String sql = "DELETE FROM SupplierDays WHERE bn = ?";

        try(Connection conn = this.makeConnection()){
            //Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }


    //remove old days, insert new ones
//    public boolean updateSupplierDays(int bn, int orderID, Set<Integer> days){
//        try{
//            deleteAllDaysToDeliver(bn);
//            for(int d : days){
//                insertDaysToDeliver(bn,orderID,d);
//            }
//            return true;
//        }
//        catch(Exception e){
//            return false;
//        }
//    }

    //returns days as integers
    public Collection<Days> selectAllSupplierDays(int bn){
        String sql = "select * from SupplierDays where bn = ?";

        try(Connection conn = this.makeConnection()){
            //Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,bn);
            ResultSet rs = pstmt.executeQuery();
            Collection<Days> cc = new LinkedList<>();
            while (rs.next()) {
                cc.add(dayConvertor(rs.getInt("day")));
            }
            return cc;

        } catch (SQLException e) {
            return null;
        }

    }

    public Days dayConvertor(int day){
        if(day==1)
            return Days.sunday;
        else if(day==2)
            return Days.monday;
        else if(day==3)
            return Days.tuesday;
        else if(day==4)
            return Days.wednesday;
        else if(day==5)
            return Days.thursday;
        else if(day==6)
            return Days.friday;
        else if(day==7)
            return Days.saturday;
        else
            throw new IllegalArgumentException("day is not valid");

    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------
}
