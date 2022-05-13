package DAL;


import misc.Days;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DaysToDeliverDAO extends DalController{

    HashMap<Integer,List<Integer>> BN_to_routineOrder;

    public DaysToDeliverDAO() {
        super("DaysToDeliver");
        BN_to_routineOrder = new HashMap<>(); //HM <BN, List<OrderID>>
        //todo
    }

    public HashMap<Integer, List<Integer>> getBN_to_routineOrder() {
        return BN_to_routineOrder;
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
    public Collection<Integer> selectAllDays(int bn, int orderID){
        String sql = "select * from DaysToDeliver where bn = ? and orderID = ?";

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

    public void loadAllRoutineDays(){
        //todo
        throw new NotImplementedException();
    }
    public boolean CheckIfOrderIsRoutineOrder(int bn,int orderId){
        if(BN_to_routineOrder.containsKey(bn)) {
            if (BN_to_routineOrder.get(bn).contains(orderId)) {
                return true;
            }
        }
        Collection<Integer> forcheck=selectAllDays(bn,orderId);
        if (forcheck==null)
            return false;
        if(!BN_to_routineOrder.containsKey(bn))
            BN_to_routineOrder.put(bn,new ArrayList<>());
        BN_to_routineOrder.get(bn).add(orderId);
        return true;
    }
    public boolean deleteAllDaysToDeliver(int bn,int orderId){
        return true;//todo: delete all the days of a specific order
    }

}
