package DAL;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DalController {
    private final String tableName;
    private String connString;
    private String path;


    public DalController(String tableName) {
        this.tableName = tableName;
    }

    public Connection makeConnection(){
        path =":../src/database.db";
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

    public String getPath() {
        return path;
    }







}