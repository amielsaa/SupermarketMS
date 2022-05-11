package DataAccessLayer;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class CreateTables {
    public static void main(String[] args)
    {
        createTable("CREATE TABLE IF NOT EXISTS Trucks ("+
                "plateNum INTEGER not null, "+
                "model VARCHAR(255) not null, "+
                "maxWeight INTEGER  not null, "+
                "PRIMARY KEY (plateNum) );");

        createTable("CREATE TABLE IF NOT EXISTS Drivers ("+
                "id INTEGER not null, "+
                "name VARCHAR(255) not null, "+
                "licenseType VARCHAR(16) not null, "+
                "PRIMARY KEY (id) );");

        createTable("CREATE TABLE IF NOT EXISTS Sites ("+
                "id INTEGER not null, "+
                "address VARCHAR(255) not null, "+
                "deliveryZone VARCHAR(16) not null, "+
                "phoneNumber VARCHAR(255) not null, "+
                "contactName VARCHAR(255) not null, "+
                "type VARCHAR(128) not null, "+
                "PRIMARY KEY (id) );");

        createTable("CREATE TABLE IF NOT EXISTS Deliveries ("+
                "id INTEGER not null, "+
                "startTime DATETIME not null, "+
                "endTime DATETIME not null, "+
                "weight INTEGER not null, "+
                "truckPlateNum INTEGER not null, "+
                "driverId INTEGER not null, "+
                "originId INTEGER not null, "+
                "PRIMARY KEY (id)," +
                "FOREIGN KEY (truckPlateNum) REFERENCES Trucks(PlateNum)," +
                "FOREIGN KEY (driverId) REFERENCES Drivers(id)," +
                "FOREIGN KEY (originId) REFERENCES Sites(id));");

        createTable("CREATE TABLE IF NOT EXISTS Destinations ("+
                "siteId INTEGER not null, "+
                "deliveryId INTEGER not null, "+
                "PRIMARY KEY (siteId, deliveryId)," +
                "FOREIGN KEY (siteId) REFERENCES Sites(id)," +
                "FOREIGN KEY (deliveryId) REFERENCES Deliveries(id));");

        createTable("CREATE TABLE IF NOT EXISTS DeliveredProducts ("+
                "siteId INTEGER not null, "+
                "deliveryId INTEGER not null, "+
                "name VARCHAR(255) not null, "+
                "count INTEGER not null, "+
                "PRIMARY KEY (siteId, deliveryId, name)," +
                "FOREIGN KEY (siteId, deliveryId) REFERENCES Destinations(siteId, deliveryId));");

        createTable("CREATE TABLE IF NOT EXISTS FinishedDeliveries ("+
                "id INTEGER not null, "+
                "details VARCHAR(65535) not null, "+
                "PRIMARY KEY (id) );");
    }


    public static void createTable(String sql)
    {
        try {
            String path =":dev\\src\\database.db";
            String connString = "jdbc:sqlite".concat(path);
            Connection conn = null;
            try{
                conn = DriverManager.getConnection(connString);
            }
            catch (SQLException e){
                System.out.println(e.getMessage());
            }
            conn.prepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
