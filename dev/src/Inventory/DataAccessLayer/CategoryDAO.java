package Inventory.DataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CategoryDAO extends DalController{

    public CategoryDAO(String tableName) {
        super(tableName);
    }

    public void insert(String name) {
        String sql = "INSERT INTO Category(name) " +
                "VALUES(?)";

        try{

            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,name);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
