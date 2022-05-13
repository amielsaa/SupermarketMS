package DataAccessLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public abstract class DataAccessObject {
    public final String tableName;
    private String path;

    public DataAccessObject(String tableName) {
        this.tableName = tableName;
    }

    public Connection makeConnection(){
        path =":src\\database.db";
        String connString = "jdbc:sqlite".concat(path);
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(connString);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }





}
