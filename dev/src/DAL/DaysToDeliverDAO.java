package DAL;


import misc.Days;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DaysToDeliverDAO extends DalController{

    HashMap<Integer,Integer> BN_to_routineOrder;

    public DaysToDeliverDAO() {
        super("DaysToDeliver");
        BN_to_routineOrder = new HashMap<>(); //HM <BN, List<OrderID>>
        //todo
    }


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
    public Collection<Days> selectAllDays(int bn, int orderID){
        String sql = "select * from DaysToDeliver where bn = ?, orderID = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,bn);
            pstmt.setInt(2,orderID);
            ResultSet rs = pstmt.executeQuery();
            Collection<Days> cc = new LinkedList<>();
            while (rs.next()) {
                cc.add(dayConvertor(rs.getInt("day")));
            }
            return cc;

        } catch (SQLException e) {
            return null; //todo
        }

    }

    public void loadAllRoutineDays(){
        //todo
        throw new NotImplementedException();
    }

    private Days dayConvertor(int day){
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



}
