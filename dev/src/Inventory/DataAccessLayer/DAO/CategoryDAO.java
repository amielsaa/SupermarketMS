package Inventory.DataAccessLayer.DAO;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.DataAccessLayer.DalController;
import Inventory.DataAccessLayer.IdentityMap.CategoryIdentityMap;

import java.sql.*;

public class CategoryDAO extends DalController {

    private CategoryIdentityMap categoryIdentityMap;

    public CategoryDAO(String tableName) {
        super(tableName);
        categoryIdentityMap = new CategoryIdentityMap();
    }

    public void deleteStoredData() {
        categoryIdentityMap.deleteAll();
    }

    public Category InsertCategory(String name) {
        Category c = categoryIdentityMap.getCategoryByName(name);
        if(c != null)
            throw new IllegalArgumentException("Category already exists.");
        String sql = "INSERT INTO Category(name) " +
                "VALUES(?)";

        try(Connection conn = this.makeConnection()){

            //Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,name);

            pstmt.executeUpdate();
            return categoryIdentityMap.addCategory(new Category(name));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public Category SelectCategory(String category) {
        Category c = categoryIdentityMap.getCategoryByName(category);
        if(c != null)
            return c;
        String sql = "SELECT * FROM Category WHERE name = '" + category+"'";

        try(Connection conn = this.makeConnection()) {
            //Connection conn = this.makeConnection();
            Statement stmt  = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return categoryIdentityMap.addCategory(new Category(rs.getString("name")));

        } catch (SQLException e) {
            throw new IllegalArgumentException("Category not found.");
        }

    }

}
