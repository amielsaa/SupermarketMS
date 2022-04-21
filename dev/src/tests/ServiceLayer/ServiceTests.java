package ServiceLayer;

import org.junit.Assert;
import org.junit.internal.runners.statements.Fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class ServiceTests {

    @Test
    void initTest()
    {
        try{
            DeliveryService service = new DeliveryService();
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    void deleteTruck() {
        DeliveryService service = new DeliveryService();
        assertTrue(service.deleteTruck(1000002).isSuccess());
        assertFalse(service.deleteTruck(1000001).isSuccess());
    }

    @Test
    void deleteSite() {
        DeliveryService service = new DeliveryService();
        assertTrue(service.deleteSite(3).isSuccess());
        assertFalse(service.deleteSite(1).isSuccess());
        assertFalse(service.deleteSite(4).isSuccess());
    }
}
