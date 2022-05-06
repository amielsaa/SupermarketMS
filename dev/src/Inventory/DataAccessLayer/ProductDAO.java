package Inventory.DataAccessLayer;

import Inventory.BuisnessLayer.Objects.Category;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProductDAO extends DalController {

    public ProductDAO(String tableName) {
        super(tableName);
    }


    public void insert(int Id, String name, String producer,double buyingPrice, double sellingPrice,double discount, String discountExpDate, List<String> categories,int minQuantity) {
        String sql = "INSERT INTO Products(id, name,producer," +
                "buyingprice, sellingprice, discount,discountDate," +
                "categories, minquantity) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";

        try{

            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Id);
            pstmt.setString(2,name);
            pstmt.setString(3,producer);
            pstmt.setDouble(4, buyingPrice);
            pstmt.setDouble(5, sellingPrice);
            pstmt.setDouble(6, discount);
            pstmt.setString(7,discountExpDate);
            pstmt.setString(8,categories.stream().reduce("",(acc,s)->acc.isEmpty() ? acc + s : acc + ","+s));
            pstmt.setInt(9, minQuantity);


            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
