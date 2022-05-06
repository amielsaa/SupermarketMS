package DAL;

import BusinessLayer.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class OrderDAO extends DalController {
    private HashMap<Integer, HashMap<Integer, Order>> BN_To_Orders;


    public OrderDAO(){
        super("Orders");
        BN_To_Orders = new HashMap<Integer,HashMap<Integer,Order>>();
    }

    public void select(){}
    //public void update(){}

    public void insertOrders(int bn, int orderID, int finalprice, String orderdate ) throws Exception {
        String sql = "INSERT INTO Orders(bn, orderID, finalprice, orderdate ) VALUES(?,?,?,?)";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setInt(2, orderID);
            pstmt.setInt(3, finalprice);
            pstmt.setString(4, orderdate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void deleteOrders(int bn, int orderID) throws Exception {
        String sql = "DELETE FROM Orders WHERE bn = ?, orderID = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setInt(2, orderID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void insertOrderItems(int orderID, int itemname, String itemproducer, int itemprice, int itemamount ) throws Exception {
        String sql = "INSERT INTO OrderItems(orderID, itemname, itemproducer, itemprice, itemamount) VALUES(?,?,?,?,?)";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderID);
            pstmt.setInt(2, itemname);
            pstmt.setString(3, itemproducer);
            pstmt.setInt(4, itemprice);
            pstmt.setInt(5, itemamount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void deleteOrderItems(int orderID, int itemname, String itemproducer) throws Exception {
        String sql = "DELETE FROM OrderItems WHERE orderID = ?, itemname = ?, itemproducer = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderID);
            pstmt.setInt(2, itemname);
            pstmt.setString(3, itemproducer);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }


}
