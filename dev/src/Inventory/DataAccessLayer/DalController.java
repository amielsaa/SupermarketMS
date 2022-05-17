package Inventory.DataAccessLayer;

import java.io.File;
import java.sql.*;

public abstract class DalController {
    private final String tableName;
    private String connString;
    private String path;


    public DalController(String tableName) {
        this.tableName = tableName;
    }

    public Connection makeConnection(){
//        path = ":../ADSS_Group_D/dev/src/Inventory/DataAccessLayer/database.db";
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

    public void Update(String idColName,int id,String colName, int value) {
        String sql = "UPDATE " + tableName + " SET "+ colName + "="+value+
                " WHERE "+idColName+"="+id;
        try {
            Connection conn = this.makeConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException e) {
            throw new IllegalArgumentException("Update failed.");
        }
    }

    public void Update(String idColName,int id, String colName, String value) {
        String sql = "UPDATE " + tableName + " SET "+ colName + "="+"'"+value+"'"+
                " WHERE "+idColName+"="+id;
        try {
            Connection conn = this.makeConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException e) {
            //System.out.println(e.getMessage());
            throw new IllegalArgumentException("Update failed.");
        }

    }

    public void Delete(String idColeName,int id) {
        String sql = "DELETE FROM "+tableName+" WHERE "+idColeName+"=" +id;
        try {
            Connection conn = this.makeConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException e) {
            //System.out.println(e.getMessage());
            throw new IllegalArgumentException("Update failed.");
        }
    }

    public String getPath() {
        return path;
    }
}
