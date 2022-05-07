package Inventory.DataAccessLayer.DAO;

import Inventory.DataAccessLayer.DalController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ReportDAO extends DalController {

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
    public void SelectDefectiveProducts() {
        throw new NotImplementedException();
    }

    public void SelectExpiredProducts() {
        throw new NotImplementedException();
    }

    public void InsertDefectiveProducts(List<Integer> productIDs) {
        throw new NotImplementedException();
    }

    public void InsertExpiredProducts(List<Integer> productIDs) {
        throw new NotImplementedException();
    }

}
