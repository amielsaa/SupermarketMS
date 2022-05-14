package Inventory.DataAccessLayer.DAO;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.DataAccessLayer.DalController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO extends DalController{

    public ReportDAO(String tableName) {
        super(tableName);
    }

//    public void insert(String name) {
//        String sql = "INSERT INTO Report(name) " +
//                "VALUES(?)";
//
//        try{
//
//            Connection conn = this.makeConnection();
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1,name);
//
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
    //TODO: fix that it doesnt fetch data from Products table
    public List<Product> SelectDefectiveProducts() {
        String sql = "SELECT productid, storeid FROM  Reports "+
                "WHERE reported = 0 ";
        List<Product> products = new ArrayList<>();
        try{
            Connection conn = this.makeConnection();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            while (rs.next()) {
                Product p = new Product(rs.getInt("id"),rs.getString("name"),
                        rs.getString("producer"),rs.getDouble("buyingprice"),
                        rs.getDouble("sellingprice"),
                        stringToCategoryList(rs.getString("categories")),rs.getInt("minquantity"));
                p.setDiscount(rs.getInt("discount"),rs.getDate("discountDate"));
                products.add(p);
            }
            return products;

        } catch (SQLException e) {
            throw new IllegalArgumentException("Store products fetch failed.");
        }
    }

    public void SelectExpiredProducts() {
        throw new NotImplementedException();
    }

    public void InsertDefectiveProducts(Integer productID, Integer storeID) {
        String sql = "INSERT INTO Reports(productid,storeid,defective,reported)" +
                    "VALUES(?,?,?,?)";
        try{

            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,productID);
            pstmt.setInt(2,storeID);
            pstmt.setInt(3,1);
            pstmt.setInt(4,0);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private List<Category> stringToCategoryList(String categories) {
        String[] split = categories.split(",");
        List<Category> categoryList = new ArrayList<>();
        for(int i=0;i<split.length;i++){
            categoryList.add(new Category(split[i]));
        }
        return categoryList;
    }

}
