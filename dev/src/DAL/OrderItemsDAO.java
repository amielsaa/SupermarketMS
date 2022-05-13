package DAL;

import BusinessLayer.OrderItem;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class OrderItemsDAO extends DalController{
    public OrderItemsDAO() {
        super("OrderItems");
    }


    public boolean insertOrderItem(int orderID, String itemname, String itemproducer, double itemprice, double itemoriginalprice, int itemamount) {
        String sql = "INSERT INTO OrderItems(orderID, itemname, itemproducer, itemprice,itemoriginalprice, itemamount) VALUES(?,?,?,?,?,?)";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderID);
            pstmt.setString(2, itemname);
            pstmt.setString(3, itemproducer);
            pstmt.setDouble(4, itemprice);
            pstmt.setDouble(5, itemoriginalprice);
            pstmt.setInt(6, itemamount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean deleteOrderItem(int orderID, String itemname, String itemproducer) {
        String sql = "DELETE FROM OrderItems WHERE orderID = ?, itemname = ?, itemproducer = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderID);
            pstmt.setString(2, itemname);
            pstmt.setString(3, itemproducer);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public Collection<OrderItem> selectAllOrderItems(int orderID){
        String sql = "select * from OrderItems where orderID = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,orderID);
            ResultSet rs = pstmt.executeQuery();
            Collection<OrderItem> cc = new LinkedList<>();
            OrderItem item;
            while (rs.next()) {
                item = new OrderItem(rs.getInt("orderID"),rs.getString("itemname"),rs.getString("itemproducer"),rs.getDouble("itemprice"),rs.getDouble("itemoriginalprice"),rs.getInt("itemamount"));
                cc.add(item);
            }
            return cc;

        } catch (SQLException e) {
            return null; //todo
        }

    }
    //todo:problem in syntax
    public boolean updateItem(int orderID, String itemname, String itemproducer, double itemprice, double itemoriginalprice, int itemamount){
        String sql = "update ? set itemprice = ?, itemoriginalprice = ?, itemamount = ? where orderID = ? and itemname = ? and itemproducer = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tableName);
            pstmt.setDouble(2, itemprice);
            pstmt.setDouble(3, itemoriginalprice);
            pstmt.setInt(4, itemamount);
            pstmt.setInt(5, orderID);
            pstmt.setString(6, itemname);
            pstmt.setString(7, itemproducer);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return true;//todo:change to false
        }
        return true;

    }






}
