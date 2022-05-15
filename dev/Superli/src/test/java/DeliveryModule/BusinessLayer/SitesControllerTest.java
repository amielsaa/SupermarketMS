package DeliveryModule.BusinessLayer;

import DeliveryModule.DataAccessLayer.CreateTables;
import DeliveryModule.DataAccessLayer.SiteDAO;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

public class SitesControllerTest {
    private SitesController sitesController;

    @Before
    public void setUp() {
        sitesController=new SitesController();
        CreateTables.clearTables();
        CreateTables.createTables();
    }

    @Test
    public void addSupplierWarehouse() {
        try {
            sitesController.addSupplierWarehouse("address1",0,"000000","name");
            assertEquals(1,sitesController.getAllSites().size());
            assertNotNull(sitesController.getSite("address1"));
        }catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void addBranch() {
        try {
            sitesController.addBranch("address1",0,"000000","name");
            assertEquals(1,sitesController.getAllSites().size());
            assertNotNull(sitesController.getSite("address1"));
        }catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }

    }

    @Test
    public void editSiteAddress() {
        try {
            sitesController.addBranch("address1",0,"000000","name");
            sitesController.editSiteAddress(sitesController.getSiteId("address1"),"address2");
            assertNull(sitesController.getSite("address1"));
            assertNotNull(sitesController.getSite("address2"));
        }catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }
        try {
            sitesController.editSiteAddress(10,"nowhere");
            fail();
        }catch (Exception e){}
    }

    @Test
    public void editSiteDeliveryZone() {
        try {
            sitesController.addBranch("address1",0,"000000","name");
            assertEquals("North",sitesController.getSite("address1").getDeliveryZone().name());
            sitesController.editSiteDeliveryZone(sitesController.getSiteId("address1"),1);
            assertEquals("Center",sitesController.getSite("address1").getDeliveryZone().name());
        }catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void editSitePhoneNumber() {
        try {
            sitesController.addBranch("address1",0,"000000","name");
            assertEquals("000000",sitesController.getSite("address1").getPhoneNumber());
            sitesController.editSitePhoneNumber(sitesController.getSiteId("address1"),"11111");
            assertEquals("11111",sitesController.getSite("address1").getPhoneNumber());
        }catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void editSiteContactName() {
        try {
            sitesController.addBranch("address1",0,"000000","name1");
            assertEquals("name1",sitesController.getSite("address1").getContactName());
            sitesController.editSiteContactName(sitesController.getSiteId("address1"),"name2");
            assertEquals("name2",sitesController.getSite("address1").getContactName());
        }catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }
    }


    @Test
    public void viewSitesPerZone() {
        try {
            sitesController.addBranch(
                    "address1", 0, "000000", "name1");
            sitesController.addSupplierWarehouse(
                    "address2", 1, "000000", "name1");
            sitesController.addBranch(
                    "address3", 2, "000000", "name1");
            sitesController.addBranch(
                    "address4", 0, "000000", "name1");
            sitesController.addSupplierWarehouse(
                    "address5", 1, "000000", "name1");
            sitesController.addBranch(
                    "address6", 2, "000000", "name1");
            Collection<Site> zone0= sitesController.viewSitesPerZone(0);
            Collection<Site> zone1= sitesController.viewSitesPerZone(1);
            Collection<Site> zone2= sitesController.viewSitesPerZone(2);
            Iterator<Site> iterator;
            assertEquals(2,zone0.size());
            iterator=zone0.iterator();
            assertEquals("address1",iterator.next().getAddress());
            assertEquals("address4",iterator.next().getAddress());
            assertEquals(2,zone1.size());
            iterator=zone1.iterator();
            assertEquals("address2",iterator.next().getAddress());
            assertEquals("address5",iterator.next().getAddress());
            assertEquals(2,zone2.size());
            iterator=zone2.iterator();
            assertEquals("address3",iterator.next().getAddress());
            assertEquals("address6",iterator.next().getAddress());
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            fail();
        }
    }

    @Test
    public void deleteSite() {
        try {
            sitesController.addBranch("address1",0,"000000","name1");
            sitesController.addBranch("address2",0,"000000","name1");
            assertEquals(2,sitesController.getAllSites().size());
           sitesController.deleteSite(1);
            assertEquals(1,sitesController.getAllSites().size());
            sitesController.deleteSite(2);
            assertEquals(0,sitesController.getAllSites().size());
        }catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }
        try {
            sitesController.deleteSite(68);
            fail();
        }catch (Exception e){
        }
    }
}