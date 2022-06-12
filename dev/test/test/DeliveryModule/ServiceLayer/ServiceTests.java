package DeliveryModule.ServiceLayer;

import Delivery.ServiceLayer.DeliveryService;
import Employee.BusinessLayer.BankAccountDetails;
import Employee.BusinessLayer.Employee;
import Employee.BusinessLayer.ShiftTime;
import Employee.ServiceLayer.Gateway;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ServiceTests {

    private Gateway EmployeeMod;
    private DeliveryService DeliveryMod;
    private final int ADMIN_UID = 580083434;
    private BankAccountDetails defaultBankAccountDetails;
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
        DeliveryMod = EmployeeMod.getDeliveryService().getData();
        EmployeeMod.clearDatabases();
        DeliveryMod.clearDatabases();
        defaultBankAccountDetails = new BankAccountDetails(0, 1, 0, "Bank", "Branch", "Bob");

    }

    @Test
    public void siteRemovalFailOnBeingAppliedToDeliveryTest()
    {
        EmployeeMod.addEmployee(100100100, "driver 1", defaultBankAccountDetails, 1000, LocalDateTime.now(), "");
        EmployeeMod.driverAddQualification(100100100, "C");
        EmployeeMod.addEmployee(2, "driver 1", defaultBankAccountDetails, 1000, LocalDateTime.now(), "");
        EmployeeMod.employeeAddQualification(2, EmployeeMod.addQualification("WarehouseWorker").getData());

        Map<Integer, List<String>> hashmap  = new HashMap<Integer, List<String>>() {{
            EmployeeMod.addEmployee(2, "driver 1", defaultBankAccountDetails, 1000, LocalDateTime.now(), "");
            EmployeeMod.employeeAddQualification(2, EmployeeMod.addQualification("WarehouseWorker").getData());
            put(100100100, Arrays.asList("Driver"));
            put(2, Arrays.asList("WarehouseWorker"));
        }};

        addShift(hashmap, 1000, LocalDateTime.of(2024, Month.MAY, 20, 1, 1), ShiftTime.DAY);
        DeliveryMod.addTruck(1234567, "a", 10000);
        DeliveryMod.addSupplierWarehouse("a", 0, "a","a");
        DeliveryMod.addBranch("b", 0, "b","B");
        DeliveryMod.addDelivery(LocalDateTime.of(2024, Month.MAY, 20, 1, 2), LocalDateTime.of(2024, Month.MAY, 20, 1, 3), 1234567, 100100100, 1, 2).isSuccess();
        assertFalse(DeliveryMod.deleteSite(1).isSuccess());
    }

    @Test
    public void truckRemovalFailOnBeingAppliedToDeliveryTest()
    {
        EmployeeMod.addEmployee(100100100, "driver 1", defaultBankAccountDetails, 1000, LocalDateTime.now(), "");
        EmployeeMod.driverAddQualification(100100100, "C");
        EmployeeMod.addEmployee(2, "driver 1", defaultBankAccountDetails, 1000, LocalDateTime.now(), "");
        EmployeeMod.employeeAddQualification(2, EmployeeMod.addQualification("WarehouseWorker").getData());

        Map<Integer, List<String>> hashmap  = new HashMap<Integer, List<String>>() {{
            EmployeeMod.addEmployee(2, "driver 1", defaultBankAccountDetails, 1000, LocalDateTime.now(), "");
            EmployeeMod.employeeAddQualification(2, EmployeeMod.addQualification("WarehouseWorker").getData());
            put(100100100, Arrays.asList("Driver"));
            put(2, Arrays.asList("WarehouseWorker"));
        }};

        addShift(hashmap, 1000, LocalDateTime.of(2024, Month.MAY, 20, 1, 1), ShiftTime.DAY);
        DeliveryMod.addTruck(1234567, "a", 10000);
        DeliveryMod.addSupplierWarehouse("a", 0, "a","a");
        DeliveryMod.addBranch("b", 0, "b","B");
        DeliveryMod.addDelivery(LocalDateTime.of(2024, Month.MAY, 20, 1, 2), LocalDateTime.of(2024, Month.MAY, 20, 1, 3), 1234567, 100100100, 1, 2).isSuccess();
        assertFalse(DeliveryMod.deleteTruck(1234567).isSuccess());
    }

    @Test
    public void changeLicenseTypeTest()
    {
        EmployeeMod.addEmployee(100100100, "driver 1", defaultBankAccountDetails, 1000, LocalDateTime.now(), "");
        EmployeeMod.driverAddQualification(100100100, "C");
        DeliveryMod.editDriverLicenseType(100100100, "C1");
        assertTrue(DeliveryMod.getDriver(100100100).getData().toString().contains("C1"));
    }

    private void addShift(Map<Integer, List<String>> workers, int ID_MANAGER, LocalDateTime time, ShiftTime shiftTime)
    {
        //Employee manager = EmployeeMod.addEmployee(ID_MANAGER, "Manager", bankAccountDetailsGenerator(ID_MANAGER), 42000, LocalDateTime.now(), "Branch 1 manager").getData();
        //EmployeeMod.addQualification("HR");
        //EmployeeMod.logout();

        EmployeeMod.login(5);
        Employee manager = EmployeeMod.getEmployee(1).getData();
        EmployeeMod.addShift(1, time, manager, workers, shiftTime);
        EmployeeMod.logout();
        EmployeeMod.login(ADMIN_UID);
    }

    private BankAccountDetails bankAccountDetailsGenerator(int seed) {
        Random generator = new Random(seed);
        int bankId = generator.nextInt();
        int branchId = generator.nextInt();
        int accountId = generator.nextInt();
        String bankName = "BANK_NAMED_" + seed;
        String branchName = "BRANCH_NAMED_" + seed;
        String accountOwner = "ACCOUNT_NAMED_" + seed;
        return new BankAccountDetails(bankId, branchId, accountId, bankName, branchName, accountOwner);
    }
}
