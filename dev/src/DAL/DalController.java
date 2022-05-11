package DAL;
import BusinessLayer.Supplier;

import java.io.File;
import java.sql.*;


public abstract class DalController {
    public final String tableName;
    private String connString;
    private String path;


    public DalController(String tableName) {
        this.tableName = tableName;
    }

    public Connection makeConnection(){

//        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        path =":database.db";
        connString = "jdbc:sqlite".concat(path);
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(connString);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return conn;

    }


    //generic 1 key update - key is int, arg is int
    public boolean update(String tableName, String keyname, String colname, int key1, int arg1){
        String sql = "update ? set ? = ? where ? = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tableName);
            pstmt.setString(2, colname);
            pstmt.setInt(3, arg1);
            pstmt.setString(4, keyname);
            pstmt.setInt(5, key1);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;

    }

    //generic 1 key update - key is int, arg is String
    public boolean update(String tableName, String keyname, String colname, int key1, String arg1){
        String sql = "update ? set ? = ? where ? = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tableName);
            pstmt.setString(2, colname);
            pstmt.setString(3, arg1);
            pstmt.setString(4, keyname);
            pstmt.setInt(5, key1);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;

    }

    //generic 2 key update - keys are int, arg is int
    public boolean update(String tableName, int keyname1, int keyname2, String colname, int key1, int key2, int arg1){
        String sql = "update ? set ? = ? where ? = ?, ? = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tableName);
            pstmt.setString(2, colname);
            pstmt.setInt(3, arg1);
            pstmt.setInt(4, keyname1);
            pstmt.setInt(5, key1);
            pstmt.setInt(6, keyname2);
            pstmt.setInt(7, key2);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;

    }

    //generic 2 key update - keys are int, arg is String
    public boolean update(String tableName, int keyname1, int keyname2, String colname, int key1, int key2, String arg1){
        String sql = "update ? set ? = ? where ? = ?, ? = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tableName);
            pstmt.setString(2, colname);
            pstmt.setString(3, arg1);
            pstmt.setInt(4, keyname1);
            pstmt.setInt(5, key1);
            pstmt.setInt(6, keyname2);
            pstmt.setInt(7, key2);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;

    }





}
