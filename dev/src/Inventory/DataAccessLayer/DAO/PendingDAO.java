package Inventory.DataAccessLayer.DAO;

import Inventory.BuisnessLayer.Objects.Product;
import Inventory.DataAccessLayer.DalController;
import Inventory.DataAccessLayer.IdentityMap.PendingIdentityMap;
import misc.Pair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
//import Inventory.ServiceLayer.Objects.Pair;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PendingDAO extends DalController {


    private PendingIdentityMap pendingIdentityMap;

    public PendingDAO(String tableName) {
        super(tableName);
        pendingIdentityMap = new PendingIdentityMap();
    }

    public void InsertPending(String name, String producer, double price, int quantity) {
        String sql = "INSERT INTO Pending(name,producer," +
                "price,quantity) " +
                "VALUES(?,?,?,?)";

        try(Connection conn = this.makeConnection()){

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,name);
            pstmt.setString(2,producer);
            pstmt.setDouble(3, price);
            pstmt.setDouble(4, quantity);

            pstmt.executeUpdate();

            pendingIdentityMap.addPending(new Pair<String,String>(name,producer),new Pair<Double,Integer>(price,quantity));
            //return productIdentityMap.addProduct(new Product(Id,name,producer,buyingPrice,sellingPrice,categories,minQuantity));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Pending product insertion to database failed, please try again.");
        }
    }

    public Map<Pair<String,String>,Pair<Double,Integer>> SelectAll() {
        if(pendingIdentityMap.isPulled_all_data())
            return pendingIdentityMap.getPendings();
        String sql = "SELECT * FROM Pending";
        try(Connection conn = this.makeConnection()) {
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                String name = rs.getString("name");
                String producer = rs.getString("producer");
                if(!pendingIdentityMap.pendingExists(name,producer))
                    pendingIdentityMap.addPending(new Pair<>(name,producer),
                            new Pair<>(rs.getDouble("price"),rs.getInt("quantity") ));
            }
            pendingIdentityMap.setPulled_all_data(true);
            return pendingIdentityMap.getPendings();
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            throw new IllegalArgumentException("Pending products fetch failed.");
        }
    }

    public void DeleteAllPending() {
        if(pendingIdentityMap.isPulled_all_data())
            pendingIdentityMap = new PendingIdentityMap();

        String sql  = "DELETE FROM Pending;"+"VACUUM;";
        try(Connection conn = this.makeConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new IllegalArgumentException("Deleting pending products failed.");
        }
    }

    //reduces by one the quantity of the product
    public boolean DeleteDefectiveProduct(String name, String producer, int quantityToReduce) {
        if(!pendingIdentityMap.pendingExists(name, producer)) return false;
        int updatedQuantity = pendingIdentityMap.getPendingValue(name,producer).getSecond() - quantityToReduce;
        if(updatedQuantity < 0) return false;
        String sql = "UPDATE Pending" + " SET quantity" + "="+updatedQuantity+
                " WHERE name"+"='"+name+"'" +" AND producer = "+"'"+producer+"'";
        try(Connection conn = this.makeConnection()) {
            //Connection conn = this.makeConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            pendingIdentityMap.updateQuantity(name,producer,updatedQuantity);
            return true;
        }catch(SQLException e) {
            throw new IllegalArgumentException("Delete defective pending product failed.");
        }
    }


}
