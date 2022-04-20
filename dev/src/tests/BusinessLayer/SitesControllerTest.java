package BusinessLayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SitesControllerTest {
    SitesController sitesController;

    @BeforeEach
    void setUp() {
        sitesController=new SitesController();
    }

    @Test
    void addSupplierWarehouse() {
        try {
            sitesController.addSupplierWarehouse("address1",1,"000000","name");
            assertEquals(1,sitesController.getAllSites().size());
        }catch (Exception e){
            fail();
        }

    }

    @Test
    void addBranch() {
        try {
            sitesController.addBranch("address1",1,"000000","name");
            assertEquals(1,sitesController.getAllSites().size());
        }catch (Exception e){
            fail();
        }

    }

    @Test
    void editSiteAddress() {
        try {
            sitesController.addBranch("address1",1,"000000","name");
            assertEquals(1,sitesController.getAllSites().size());
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void editSiteDeliveryZone() {
    }

    @Test
    void editSitePhoneNumber() {
    }

    @Test
    void editSiteContactName() {
    }

    @Test
    void getSite() {
    }

    @Test
    void getAllSites() {
    }

    @Test
    void viewSitesPerZone() {
    }

    @Test
    void deleteSite() {
    }


}