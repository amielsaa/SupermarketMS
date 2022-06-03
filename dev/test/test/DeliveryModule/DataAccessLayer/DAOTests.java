package DeliveryModule.DataAccessLayer;

import Delivery.BusinessLayer.DeliveryZone;
import Delivery.BusinessLayer.SupplierWarehouse;
import Delivery.BusinessLayer.Truck;
import Delivery.DataAccessLayer.CreateClearTables;
import Delivery.DataAccessLayer.*;
import Delivery.BusinessLayer.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class DAOTests {
    //CHECK MANUALLY THE DB FOR SOME TEST RESULTS, NOT EVERYTHING CAN BE TESTED


    @Before
    public void setup(){
        //CreateTables.createTables();
        CreateClearTables.clearTables();
    }

    @Test
    public void TruckDAOTest(){
        TruckDAO dao = new TruckDAO();
        try {
            Truck t1 = new Truck(1234567, "a", 1);
            Truck t2 = new Truck(12345678, "b", 2);
            Truck t3 = new Truck(32345678, "b", 3);
            dao.Create(t1);
            dao.Create(t2);
            dao.Create(t3);
            dao.Delete(32345678);
            t2.setModel("d");
            t2.setPlateNum(4424424);
            dao.setPlateNum(12345678, 4424424);
            dao.Update(t2);

            assertEquals("d", dao.Read(4424424).getModel());
            assertEquals(2, dao.getAllTrucks().size());
            assertEquals("d", new TruckDAO().Read(4424424).getModel());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void SiteDAOTest(){
        SiteDAO dao = new SiteDAO();
        try {
            Site s1 = new SupplierWarehouse(1, "a", DeliveryZone.North, "a", "b");
            Site s2 = new Branch(2, "b", DeliveryZone.Center, "b", "b");
            Site s3 = new Branch(3, "c", DeliveryZone.South, "c", "c");
            dao.Create(s1);
            dao.Create(s2);
            dao.Create(s3);
            dao.Delete(3);
            s2.setDeliveryZone(1);
            dao.Update(s2);
            assertEquals(DeliveryZone.Center, dao.Read(2).getDeliveryZone());
            assertEquals(2, dao.getAllSites().size());
            assertTrue(dao.Read(1) instanceof SupplierWarehouse);
            assertTrue(dao.Read(2) instanceof Branch);
            assertEquals("a", new SiteDAO().Read(1).getPhoneNumber());
            assertEquals(3, dao.getMaxId());
            assertEquals(2, new SiteDAO().getMaxId());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void DriverDAOTest(){
        DriverDAO dao = new DriverDAO();
        try {
            Driver d1 = new Driver(100100100, "a", LicenseType.C);
            Driver d2 = new Driver(200200200, "b", LicenseType.C1);
            Driver d3 = new Driver(300300300, "c", LicenseType.C);
            dao.Create(d1);
            dao.Create(d2);
            dao.Create(d3);
            dao.Delete(300300300);
            d2.setLicenseType(LicenseType.C);
            dao.Update(d2);
            assertEquals(LicenseType.C, dao.Read(200200200).getLicenseType());
            assertEquals(2, dao.readAll().size());
            assertEquals("a", new DriverDAO().Read(100100100).getName());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void UpcomingDeliveryDAOTest(){
        UpcomingDeliveryDAO dao = new UpcomingDeliveryDAO();
        try {
            createTruckData();
            createSiteData();
            createDriverData();
            LocalDateTime now = LocalDateTime.now();
            Delivery d1 = new Delivery(1, now.plusHours(1), now.plusHours(2),100100100, 1234567, 1, 0);
            Delivery d2 = new Delivery(2, now.plusHours(1), now.plusHours(2),200200200, 12345678, 2, 0);
            Delivery d3 = new Delivery(3, now.plusHours(1), now.plusHours(2),300300300, 32345678, 1, 0);
            dao.Create(d1);
            dao.Create(d2);
            dao.Create(d3);
            dao.Delete(3);
            d2.setEndTime(now.plusHours(100));
            dao.Update(d2);
            assertEquals(now.plusHours(100).toString().substring(0, 18), dao.Read(2).getEndTime().toString().substring(0, 18));
            assertEquals(2, dao.getUpcomingDeliveries().size());
            assertEquals(2, new UpcomingDeliveryDAO().Read(2).getOriginSiteId());
            assertEquals(3, dao.getMaxId());
            assertEquals(2, new UpcomingDeliveryDAO().getMaxId());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void DeliveryDestinationsDAOTest(){
        DeliveryDestinationsDAO dao1 = new DeliveryDestinationsDAO();
        DeliveryDestinationsDAO dao2 = new DeliveryDestinationsDAO();
        try {
            createUpcomingDeliveryData();
            dao1.Create(1,2);
            dao1.Create(2,3);
            dao1.Create(3,3);
            dao2.Create(1,3);
            dao2.Create(3,2);
            dao1.Delete(3, 2);
            assertEquals(2, dao1.Read(1).size());
            assertEquals(2, dao2.Read(1).size());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void DeliveryDestinationsItemsDAOTest(){
        DeliveryDestinationItemsDAO dao1 = new DeliveryDestinationItemsDAO();
        DeliveryDestinationItemsDAO dao2 = new DeliveryDestinationItemsDAO();
        try {
            createDeliveryDestinations();
            dao1.Create(1, 3, "a", 1);
            dao1.Create(1, 3, "b", 2);
            dao1.Create(1, 3, "c", 3);
            dao1.Create(3, 2, "a", 1);
            dao1.Create(3, 3, "b", 4);
            dao1.Create(3, 3, "e", 4);
            dao2.removeItemFromDestination(3,3,"b");
            dao2.removeItemFromDestination(3,3,"e");
            dao2.editItemQuantity(1, 3, "a", 10);
            dao2.editItemQuantity(1, 3, "b", 20);
            assertEquals(3, dao2.getItemsOfDest(1, 3).keySet().size());
            assertEquals(10, dao2.getItemsOfDest(1, 3).get("a").intValue());
            assertEquals(20, dao2.getItemsOfDest(1, 3).get("b").intValue());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }
    @Test
    public void DeliveryArchiveDAOTest(){
        DeliveryArchiveDAO dao = new DeliveryArchiveDAO();
        try {
            dao.Create(1,"a");
            dao.Create(2,"b");
            assertEquals(2, dao.getDeliveryArchive().size());
            assertEquals(dao.Read(1), "a");
            assertEquals(2, dao.getMaxId());
            assertEquals(2, new DeliveryArchiveDAO().getMaxId());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    private void createTruckData() throws Exception {
        TruckDAO dao = new TruckDAO();
        Truck t1 = new Truck(1234567, "a", 1);
        Truck t2 = new Truck(12345678, "b", 2);
        Truck t3 = new Truck(32345678, "b", 3);
        dao.Create(t1);
        dao.Create(t2);
        dao.Create(t3);
    }

    private void createDriverData() throws Exception {
        DriverDAO dao = new DriverDAO();
        Driver d1 = new Driver(100100100, "a", LicenseType.C);
        Driver d2 = new Driver(200200200, "b", LicenseType.C1);
        Driver d3 = new Driver(300300300, "c", LicenseType.C);
        dao.Create(d1);
        dao.Create(d2);
        dao.Create(d3);
    }

    private void createSiteData() throws Exception {
        SiteDAO dao = new SiteDAO();
        Site s1 = new SupplierWarehouse(1, "a", DeliveryZone.North, "a", "b");
        Site s2 = new Branch(2, "b", DeliveryZone.Center, "b", "b");
        Site s3 = new Branch(3, "c", DeliveryZone.South, "c", "c");
        dao.Create(s1);
        dao.Create(s2);
        dao.Create(s3);
    }

    private void createUpcomingDeliveryData() throws Exception {
        UpcomingDeliveryDAO dao = new UpcomingDeliveryDAO();
        createTruckData();
        createSiteData();
        createDriverData();
        LocalDateTime now = LocalDateTime.now();
        Delivery d1 = new Delivery(1, now.plusHours(1), now.plusHours(2),100100100, 1234567, 1, 0);
        Delivery d2 = new Delivery(2, now.plusHours(1), now.plusHours(2),200200200, 12345678, 2, 0);
        Delivery d3 = new Delivery(3, now.plusHours(1), now.plusHours(2),300300300, 32345678, 1, 0);
        dao.Create(d1);
        dao.Create(d2);
        dao.Create(d3);
    }

    private void createDeliveryDestinations() throws Exception {
        DeliveryDestinationsDAO dao = new DeliveryDestinationsDAO();
        createUpcomingDeliveryData();
        dao.Create(1,2);
        dao.Create(2,3);
        dao.Create(3,3);
        dao.Create(1,3);
        dao.Create(3,2);
    }
}
