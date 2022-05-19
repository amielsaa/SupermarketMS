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

    public List<Integer> SelectDefectiveProducts(int storeId) {
        String sql = "SELECT productid FROM Reports "+
                "WHERE reported = 0 "+
                "AND storeid = "+storeId;
        List<Integer> products = new ArrayList<>();
        try{
            Connection conn = this.makeConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                products.add(rs.getInt("productid"));
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
