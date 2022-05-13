package DeliveryModule.ServiceLayer;

import EmployeeModule.ServiceLayer.Gateway;
import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import static org.junit.Assert.*;


public class ServiceTests {

    @Test
    public void initTest()
    {
        try{
            DeliveryService service = new DeliveryService(new Gateway());
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void deleteTruck() {
        DeliveryService service = new DeliveryService(new Gateway());
        assertTrue(service.deleteTruck(1000002).isSuccess());
        assertFalse(service.deleteTruck(1000001).isSuccess());
    }
    @Test
    public void deleteSite() {
        DeliveryService service = new DeliveryService(new Gateway());
        assertTrue(service.deleteSite(3).isSuccess());
        assertFalse(service.deleteSite(1).isSuccess());
        assertFalse(service.deleteSite(4).isSuccess());
    }
}
