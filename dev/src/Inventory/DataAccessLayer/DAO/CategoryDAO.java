package Inventory.DataAccessLayer.DAO;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.DataAccessLayer.DalController;
import Inventory.DataAccessLayer.Mappers.CategoryMapper;

import java.sql.*;
import java.util.List;

public class CategoryDAO extends DalController {

    private CategoryMapper categoryMapper;

    public CategoryDAO(String tableName) {
        super(tableName);
        categoryMapper = new CategoryMapper();
    }

    public Category InsertCategory(String name) {
        Category c = categoryMapper.getCategoryByName(name);
        if(c != null)
            throw new IllegalArgumentException("Category already exists.");
        String sql = "INSERT INTO Category(name) " +
                "VALUES(?)";

        try{

            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,name);

            pstmt.executeUpdate();
            return categoryMapper.addCategory(new Category(name));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public Category SelectCategory(String category) {
        Category c = categoryMapper.getCategoryByName(category);
        if(c != null)
            return c;
        String sql = "SELECT * FROM Category WHERE name = ?";

        try {
            Connection conn = this.makeConnection();
            PreparedStatement stmt  = conn.prepareStatement(sql);
            stmt.setString(1,category);
            ResultSet rs = stmt.executeQuery(sql);
            return categoryMapper.addCategory(new Category(rs.getString("name")));

        } catch (SQLException e) {
            throw new IllegalArgumentException("Category not found.");
        }

    }

}
