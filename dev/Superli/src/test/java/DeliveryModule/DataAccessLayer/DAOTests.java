package DeliveryModule.DataAccessLayer;

import DeliveryModule.BusinessLayer.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DAOTests {
    //CHECK MANUALLY THE DB FOR SOME TEST RESULTS, NOT EVERYTHING CAN BE TESTED

    //TESTS MUST BE DONE IN ORDER

    @Before
    public void setup(){
        //CreateTables.createTables();
        //CreateTables.clearTables();
    }

    @Test
    public void TruckDAOTest(){
        CreateTables.clearTables();
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
            /*Delivery d1 = new Delivery(100100100, "a", LicenseType.C);
            Delivery d2 = new Delivery(200200200, "b", LicenseType.C1);
            Delivery d3 = new Delivery(300300300, "c", LicenseType.C);
            dao.Create(d1);
            dao.Create(d2);
            dao.Create(d3);
            dao.Delete(300300300);
            d2.setLicenseType(LicenseType.C);
            dao.Update(d2);
            assertEquals(LicenseType.C, dao.Read(200200200).getLicenseType());
            assertEquals(2, dao.readAll().size());
            assertEquals("a", new DriverDAO().Read(100100100).getName());*/
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void DeliveryDestinationsDAOTest(){
        TruckDAO dao = new TruckDAO();
    }

    @Test
    public void DeliveryDestinationsItemsDAOTest(){
        TruckDAO dao = new TruckDAO();
    }
    @Test
    public void DeliveryArchiveDAOTest(){
        TruckDAO dao = new TruckDAO();
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
}
