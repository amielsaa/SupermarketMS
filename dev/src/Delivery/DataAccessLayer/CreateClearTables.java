package Delivery.DataAccessLayer;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class CreateClearTables {


    public static void main(String[] args)
    {
        //clearTables();
        createTables();
    }

    public static void createTables()
    {
        exeQuarry("CREATE TABLE IF NOT EXISTS Trucks ("+
                "plateNum INTEGER not null, "+
                "model VARCHAR(255) not null, "+
                "maxWeight INTEGER  not null, "+
                "PRIMARY KEY (plateNum) );");

        exeQuarry("CREATE TABLE IF NOT EXISTS Drivers ("+
                "id INTEGER not null, "+
                "name VARCHAR(255) not null, "+
                "licenseType VARCHAR(16) not null, "+
                "PRIMARY KEY (id) );");

        exeQuarry("CREATE TABLE IF NOT EXISTS Sites ("+
                "id INTEGER not null, "+
                "address VARCHAR(255) not null, "+
                "deliveryZone VARCHAR(16) not null, "+
                "phoneNumber VARCHAR(255) not null, "+
                "contactName VARCHAR(255) not null, "+
                "type VARCHAR(128) not null, "+
                "PRIMARY KEY (id) );");

        exeQuarry("CREATE TABLE IF NOT EXISTS UpcomingDeliveries ("+
                "id INTEGER not null, "+
                "startDate DATETIME not null, "+
                "startTime DATETIME not null, "+
                "endDate DATETIME not null, "+
                "endTime DATETIME not null, "+
                "weight INTEGER not null, "+
                "truckPlateNum INTEGER not null, "+
                "driverId INTEGER not null, "+
                "originId INTEGER not null, "+
                "PRIMARY KEY (id)," +
                "FOREIGN KEY (truckPlateNum) REFERENCES Trucks(PlateNum)," +
                "FOREIGN KEY (driverId) REFERENCES Drivers(id)," +
                "FOREIGN KEY (originId) REFERENCES Sites(id));");

        exeQuarry("CREATE TABLE IF NOT EXISTS DeliveryDestinations ("+
                "siteId INTEGER not null, "+
                "deliveryId INTEGER not null, "+
                "PRIMARY KEY (siteId, deliveryId)," +
                "FOREIGN KEY (siteId) REFERENCES Sites(id)," +
                "FOREIGN KEY (deliveryId) REFERENCES UpcomingDeliveries(id));");

        exeQuarry("CREATE TABLE DeliveryDestinationItems ("+
                "siteId INTEGER not null, "+
                "deliveryId INTEGER not null, "+
                "name VARCHAR(255) not null, "+
                "producerName VARCHAR(255) not null, "+
                "price DOUBLE(54) not null, "+
                "count INTEGER not null, "+
                "PRIMARY KEY (siteId, deliveryId, name, producerName)," +
                "FOREIGN KEY (siteId, deliveryId) REFERENCES DeliveryDestinations(siteId, deliveryId));");

        exeQuarry("CREATE TABLE IF NOT EXISTS DeliveryArchive ("+
                "id INTEGER not null, "+
                "details VARCHAR(65535) not null, "+
                "PRIMARY KEY (id) );");
    }

    public static void clearTables()
    {
        exeQuarry("DELETE FROM Trucks");
        exeQuarry("DELETE FROM Drivers");
        exeQuarry("DELETE FROM Sites");
        exeQuarry("DELETE FROM UpcomingDeliveries");
        exeQuarry("DELETE FROM DeliveryDestinations");
        exeQuarry("DELETE FROM DeliveryDestinationItems");
        exeQuarry("DELETE FROM DeliveryArchive");
    }

    public static void exeQuarry(String sql)
    {
        try {
            String path =":database.db";
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
