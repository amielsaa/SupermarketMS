package Suppliers.DAL;

import Suppliers.BusinessLayer.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


public class OrderDAO extends DalController {
    private HashMap<Integer, HashMap<Integer, Order>> BN_To_Orders;



    public OrderDAO(){
        super("Orders");
        BN_To_Orders = new HashMap<Integer,HashMap<Integer,Order>>();
    }

    //put in priceBeforeDiscount as field
    public boolean insertOrders(int bn, int orderID, double finalprice, String orderdate, double originalprice ) {
        String sql = "INSERT INTO Orders(bn, orderID, finalprice, orderdate, originalprice ) VALUES(?,?,?,?,?)";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setInt(2, orderID);
            pstmt.setDouble(3, finalprice);
            pstmt.setString(4, orderdate);
            pstmt.setDouble(5, originalprice);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteOrders(int bn, int orderID)  {
        String sql = "DELETE FROM Orders WHERE bn = ? and orderID = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setInt(2, orderID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }


    public boolean containsOrder(int bn,int orderId){
        if(BN_To_Orders.containsKey(bn))
            if(BN_To_Orders.get(bn).containsKey(orderId))
                return true;
        return checkOrderInDB(bn, orderId);
    }

    private boolean checkOrderInDB(int bn, int orderId) {
        return selectOrder(bn, orderId) != null;
    }

    private Order selectOrder(int bn, int orderId) {
        String sql = "select * from Orders where bn = ? and  orderID = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,bn);
            pstmt.setInt(2,orderId);
            ResultSet rs = pstmt.executeQuery();
            Order order = null;
            while (rs.next()) {
                order = new Order(rs.getInt("bn"), rs.getInt("orderID"),rs.getDouble("finalprice"),rs.getString("orderdate"), rs.getDouble("originalprice"));
                insertOrderToHM(bn, order);
            }
            return getOrder(bn, orderId);

        } catch (SQLException e) {
            return null;
        }
    }

    public Order getOrder(int bn,int orderId){
      return BN_To_Orders.get(bn).get(orderId);
    }



    public boolean setAllOrders(int bn){
        // select orders with this bn, add orders to HM. return false if none are found.
        // check if each order isn't getting added twice to HM (wasn't there previously)


        String sql = "select * from Orders where bn = ?";
        boolean ans = false;
        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,bn);
            ResultSet rs = pstmt.executeQuery();
            Order order = null;
            while (rs.next()) {
                order = new Order(rs.getInt("bn"), rs.getInt("orderID"),rs.getDouble("finalprice"),rs.getString("orderdate"), rs.getDouble("originalprice"));
                if(insertOrderToHM(bn, order)) ans = true;
            }


        } catch (SQLException e) {
            return false;
        }

        return ans;
    }

    public Collection<Order> getAllOrders(int bn){
        if(BN_To_Orders.containsKey(bn))
            return BN_To_Orders.get(bn).values();
        return new ArrayList<>(); //if there are no orders in HM
    }

    public boolean insertOrderToHM(int bn, Order s){
        //returns true if added to HM, false otherwise

        if(!BN_To_Orders.containsKey(bn))
            BN_To_Orders.put(bn, new HashMap<Integer, Order>());

        if(!BN_To_Orders.get(bn).containsKey(s.getOrder_Id())) {
            BN_To_Orders.get(bn).put(s.getOrder_Id(), s);
            return true;
        }
        return false;
    }

    public void updateOrderInHM(int bn, Order o){
        BN_To_Orders.get(bn).put(o.getOrder_Id(),o);
    }

    public void addSupplier(int bn){
        BN_To_Orders.put(bn,new HashMap<>());
    }


    public boolean updateOrderPrice(int bn, int orderID, double originalPrice, double finalPrice){
//        String sql = "update ? set originalPrice = ?, finalPrice = ? where bn = ? and orderID = ?";
        String sql = "update " + tableName + " set originalPrice = " + originalPrice + ", finalPrice = " + finalPrice + " where bn = " + bn + " and orderID = " + orderID;
        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public int getMaxOrderId(){

        String sql = "select MAX(orderID) from Orders";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int maxOrder = 0;
            while (rs.next()) {
                maxOrder = rs.getInt(1) + 1;
            }
            return maxOrder;

        } catch (SQLException e) {
            return 0;
        }

    }
}
