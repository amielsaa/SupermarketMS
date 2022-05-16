package Integration;

import DeliveryModule.BusinessLayer.Driver;
import DeliveryModule.ServiceLayer.DeliveryService;
import EmployeeModule.BusinessLayer.Employee;
import EmployeeModule.ServiceLayer.Gateway;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeliveryEmployeeIntegrationTests {
    private Gateway EmployeeMod;
    private DeliveryService DeliveryMod;
    private final int ADMIN_UID = 580083434;
    @Before
    public void setUp()
    {
        EmployeeMod = new Gateway();
        try
        {
            EmployeeMod.initDefaultData();
        } catch (Exception e)
        {
            System.out.println("WARNING! initialization of the gateway resulted in an error: " + e.getMessage());
        } finally
        {
            EmployeeMod.login(ADMIN_UID);
        }
        DeliveryMod = EmployeeMod.getDeliveryService();
        EmployeeMod.clearDatabases();
        DeliveryMod.clearDatabases();
    }

    @Test
    public void testDeleteDriver()
    {
        DeliveryMod.addDriver(100100100, "driver 1", "C");
        DeliveryMod.addDriver(100100101, "driver 2", "C");
        DeliveryMod.deleteDriver(100100101);
        assertTrue(DeliveryMod.getDriver(100100100).isSuccess());
        assertFalse(DeliveryMod.getDriver(100100101).isSuccess());
    }

    @Test
    public void testEditDriver()
    {
        DeliveryMod.addDriver(100100100, "driver 1", "C");
        DeliveryMod.editDriverName(100100100, "driver 2");
        DeliveryMod.editDriverLicenseType(100100100, "C1");
        assertEquals("Id: 100100100\n\t* Name: driver 2\n\t* License type: C1\n", DeliveryMod.getDriver(100100100).getData());
    }

    @Test
    public void test3()
    {

    }

    @Test
    public void test4()
    {

    }

    @Test
    public void test5()
    {

    }

    @Test
    public void test6()
    {

    }

    @Test
    public void test7()
    {

    }

    @Test
    public void test8()
    {

    }

    @Test
    public void test9()
    {

    }

    @Test
    public void test10()
    {

    }
}
