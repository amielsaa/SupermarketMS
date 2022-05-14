package DeliveryModule.DataAccessLayer;


import DeliveryModule.BusinessLayer.*;

import java.time.LocalDateTime;

//move me to tests folder with junits!
public class DAL_RAW_TEST {
    /*
    public static void main(String[] args) throws Exception {

        CreateTables.main(new String[0]);
        SiteDAO siteDAO = new SiteDAO();
        TruckDAO truckDAO = new TruckDAO();
        DriverDAO driverDAO = new DriverDAO();
        DestinationsDAO destinationsDAO = new DestinationsDAO();
        DeliveredProductsDAO deliveredProductsDAO = new DeliveredProductsDAO();
        DeliveryDAO deliveryDAO = new DeliveryDAO(truckDAO, driverDAO, siteDAO, destinationsDAO, deliveredProductsDAO);
        DeliveryArchiveDAO deliveryArchiveDAO = new DeliveryArchiveDAO();

        SupplierWarehouse s1 = new SupplierWarehouse(1, "Haifa", 0, "054-0000001", "supplier1");
        Branch b1 = new Branch(3, "Tiberias", 0,"054-0000003","branch1");
        siteDAO.Create(s1);
        siteDAO.Create(new SupplierWarehouse(2, "Beer Sheva", 2, "054-0000002", "supplier2"));
        siteDAO.Create(b1);
        siteDAO.Create(new Branch(4, "Tel Aviv", 1,"054-0000004","branch2"));
        siteDAO.Create(new Branch(5, "Jerusalem", 1,"054-0000005","branch3"));
        siteDAO.Create(new Branch(6, "Dimona", 2,"054-0000006","branch4"));

        Truck t1 = new Truck(1000001, "small truck", 9000);
        truckDAO.Create(t1);
        truckDAO.Create(new Truck(1000002, "small truck", 9000));
        truckDAO.Create(new Truck(1000003, "small truck", 9000));
        truckDAO.Create(new Truck(1000004, "big truck", 14000));
        truckDAO.Create(new Truck(1000005, "big truck", 14000));


        Driver d1 = new Driver(200000001, "c1 driver1", LicenseType.C1);
        driverDAO.Create(d1);
        driverDAO.Create(new Driver(200000002, "c1 driver2", LicenseType.C1));
        driverDAO.Create(new Driver(200000003, "c1 driver3", LicenseType.C1));
        driverDAO.Create(new Driver(200000004, "c driver1", LicenseType.C));
        driverDAO.Create(new Driver(200000005, "c driver2", LicenseType.C));
        driverDAO.Create(new Driver(200000006, "c driver3", LicenseType.C));

        deliveryDAO.Create(new Delivery(1, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), d1, t1,s1, b1));
        destinationsDAO.Create(3, 1);
        deliveredProductsDAO.Create(3, 1, "a", 1);


        //after the cache is cleared
        SiteDAO siteDAO2 = new SiteDAO();
        TruckDAO truckDAO2 = new TruckDAO();
        DriverDAO driverDAO2 = new DriverDAO();
        DestinationsDAO destinationsDAO2 = new DestinationsDAO();
        DeliveredProductsDAO deliveredProductsDAO2 = new DeliveredProductsDAO();
        DeliveryDAO deliveryDAO2 = new DeliveryDAO(truckDAO2, driverDAO, siteDAO2, destinationsDAO2, deliveredProductsDAO2);
        DeliveryArchiveDAO deliveryArchiveDAO2 = new DeliveryArchiveDAO();

        System.out.println(deliveryDAO2.Read(1));
    }


    public static void createTable(String sql)
    {

    }

         */
}
