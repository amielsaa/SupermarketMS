package ServiceLayer;

import BusinessLayer.*;
import org. junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.Assert.*;

public class GatewayTest
{

    private Gateway g;
    private final int ADMIN_UID = 580083434;

    private BankAccountDetails bankAccountDetailsGenerator(int seed) {
        Random generator = new Random(seed);
        int bankId = generator.nextInt();
        int branchId = generator.nextInt();
        int accountId = generator.nextInt();
        String bankName = "BANK_NAMED_" + bankId;
        String branchName = "BRANCH_NAMED_" + branchId;
        String accountOwner = "ACCOUNT_NAMED_" + accountId;
        return new BankAccountDetails(bankId, branchId, accountId, bankName, branchName, accountOwner);
    }


    @Before
    public void setUp() throws Exception
    {
        g = new Gateway();
        g.initDefaultData();
        g.login(ADMIN_UID);
    }

    @After
    public void tearDown() throws Exception
    {
        g.logout();
    }
    @Test
    public void initDefaultData() {

    }
    @Test
    public void login() {
        var r = g.getLoggedUser();
        if(!r.isSuccess()) {
            fail(r.getMessage());
        }
        assertEquals(r.getData().getId(), ADMIN_UID);
    }

    @Test
    public void getEmployees()
    {
        assertTrue(g.getEmployees().getMessage(), g.getEmployees().isSuccess());
    }

    @Test
    public void addEmployee()
    {
        final int NEW_EMPLOYEE_ID = 420;
        var r = g.addEmployee(NEW_EMPLOYEE_ID, "Snoop Dog", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID), 42000, LocalDateTime.now(), "Works in the morning. ");
        assertTrue(r.getMessage(), r.isSuccess());

        var r2 = g.addEmployee(NEW_EMPLOYEE_ID, "Snoop Dog (Imposter)", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID), 420, LocalDateTime.now(), "Works in the morning. ");
        assertFalse("Employee should already exist. ", r2.isSuccess());

        final int NEW_EMPLOYEE_ID_2 = 7355608;
        var r3 = g.addEmployee(NEW_EMPLOYEE_ID_2, "Shroud", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID_2), 2000000, LocalDateTime.now(), "Work from home boi. ");
        assertTrue(r.getMessage(), r3.isSuccess());
    }

    @Test
    public void removeEmployee()
    {
        // CHECK TEST
        final int NEW_EMPLOYEE_ID = 420;
        g.addEmployee(NEW_EMPLOYEE_ID, "Snoop Dog", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID), 42000, LocalDateTime.now(), "Works in the morning. ");

        int employeeCount = g.getEmployees().getData().size();

        var r = g.removeEmployee(NEW_EMPLOYEE_ID);
        assertTrue(r.getMessage(), r.isSuccess());

        assertEquals("Employee count should drop by 1. ", g.getEmployees().getData().size(), employeeCount);

        var r2 = g.removeEmployee(NEW_EMPLOYEE_ID);
        assertFalse("Employee should already be removed. ", r2.isSuccess());

        var r3 = g.getEmployee();
        assertFalse("Shouldn't be able to get a removed employee. ", r3.isSuccess());
    }

    @Test
    public void updateEmployeeName()
    {
        // CHECK TEST
        final int NEW_EMPLOYEE_ID = 420;
        g.addEmployee(NEW_EMPLOYEE_ID, "Snoop Dog", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID), 42000, LocalDateTime.now(), "Works in the morning. ");

        Employee e = g.getEmployee(NEW_EMPLOYEE_ID).getData();
        assertEquals("Employee name not initiated correctly. ", e.getName(), "Snoop Dog");

        g.updateEmployeeName(NEW_EMPLOYEE_ID, "Doog Snop");

        assertEquals("Employee name should be updated. ", e.getName(), "Doog Snop");


    }

    @Test
    public void updateEmployeeSalary()
    {
        // CHECK TEST
        final int NEW_EMPLOYEE_ID = 420;
        g.addEmployee(NEW_EMPLOYEE_ID, "Snoop Dog", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID), 42000, LocalDateTime.now(), "Works in the morning. ");

        Employee e = g.getEmployee(NEW_EMPLOYEE_ID).getData();
        assertEquals("Employee salary not initiated correctly. ", e.getSalary(), 42000);

        g.updateEmployeeSalary(NEW_EMPLOYEE_ID, 420);

        assertEquals("Employee salary should be updated. ", e.getSalary(), 420);
    }

    @Test
    public void updateEmployeeBankAccountDetails()
    {
        // CHECK TEST
        final int NEW_EMPLOYEE_ID = 420;
        g.addEmployee(NEW_EMPLOYEE_ID, "Snoop Dog", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID), 42000, LocalDateTime.now(), "Works in the morning. ");

        Employee e = g.getEmployee(NEW_EMPLOYEE_ID).getData();
        assertEquals("Employee bank account details not initiated correctly. ", e.getBankAccountDetails().bankName(), "BANK_NAMED_420");

        BankAccountDetails newDetails = bankAccountDetailsGenerator(21);

        g.updateEmployeeBankAccountDetails(NEW_EMPLOYEE_ID, newDetails);

        assertEquals("Employee bank account details should be updated. ", e.getBankAccountDetails().bankName(), "BANK_NAMED_21");
    }

    @Test
    public void employeeAddWorkingHour()
    {
        // CHECK TEST
        final int NEW_EMPLOYEE_ID = 420;
        final int OTHER_EMPLOYEE_ID = 21;

        g.addEmployee(NEW_EMPLOYEE_ID, "Snoop Dog", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID), 42000, LocalDateTime.now(), "Works in the morning. ");
        g.addEmployee(OTHER_EMPLOYEE_ID, "Other Otherman", bankAccountDetailsGenerator(OTHER_EMPLOYEE_ID), 999, LocalDateTime.now(), "Works every other day. ");

        var r1 = g.employeeAddWorkingHour(NEW_EMPLOYEE_ID, LocalDateTime.now().plusMinutes(5), LocalDateTime.now().minusHours(2));
        assertFalse("Shouldn't be able to add an illegal TimeInterval as new working hour. ", r1.isSuccess());

        var r = g.employeeAddWorkingHour(NEW_EMPLOYEE_ID, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3));
        assertTrue(r.getMessage(), r.isSuccess());

        var r2 = g.employeeAddWorkingHour(NEW_EMPLOYEE_ID, LocalDateTime.now().plusDays(2).plusHours(5), LocalDateTime.now().plusDays(2).minusHours(1));
        assertFalse("Shouldn't be able to add overlapping hours. ", r2.isSuccess());

        g.logout();
        g.login(NEW_EMPLOYEE_ID);

        var r3 = g.employeeAddWorkingHour(OTHER_EMPLOYEE_ID, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3));
        asserFalse("Shouldn't be able to add to other employee as another non permitted employee. ", r3.isSuccess());

        var r4 = g.employeeAddWorkingHour(NEW_EMPLOYEE_ID, LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(4).plusHours(2));
        assertTrue("Should be able to add working hours for myself. " + r4.getMessage(), r4.isSuccess());
    }

    @Test
    public void employeeRemoveWorkingHour()
    {
        // CHECK TEST
        final int NEW_EMPLOYEE_ID = 420;
        final int OTHER_EMPLOYEE_ID = 21;

        g.addEmployee(NEW_EMPLOYEE_ID, "Snoop Dog", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID), 42000, LocalDateTime.now(), "Works in the morning. ");
        g.addEmployee(OTHER_EMPLOYEE_ID, "Other Otherman", bankAccountDetailsGenerator(OTHER_EMPLOYEE_ID), 999, LocalDateTime.now(), "Works every other day. ");



        LocalDateTime timeStamp = LocalDateTime.now();

        g.employeeAddWorkingHour(OTHER_EMPLOYEE_ID, timeStamp.plusDays(4), timeStamp.plusDays(4).plusHours(2));

        g.employeeAddWorkingHour(NEW_EMPLOYEE_ID, timeStamp.plusHours(1), timeStamp.plusHours(2));
        var r1 = g.employeeRemoveWorkingHour(NEW_EMPLOYEE_ID, timeStamp.plusHours(1));
        assertTrue(r1.getMessage(), r1.isSuccess());

        var r2 = g.employeeRemoveWorkingHour(NEW_EMPLOYEE_ID, timeStamp.plusHours(1));
        assertFalse("Shouldn't be able to remove the same hour twice. ", r1.isSuccess());

        g.employeeAddWorkingHour(NEW_EMPLOYEE_ID, timeStamp.plusHours(1), timeStamp.plusHours(2));

        g.logout();
        g.login(NEW_EMPLOYEE_ID);

        var r3 = g.employeeRemoveWorkingHour(NEW_EMPLOYEE_ID, timeStamp.plusHours(1));
        assertTrue(r1.getMessage(), r1.isSuccess());

        var r4 = g.employeeRemoveWorkingHour(OTHER_EMPLOYEE_ID, timeStamp.plusDays(4));
        assertFalse("Shouldn't be able to remove another employee's working hours. ", r4.isSuccess());
    }

    @Test
    public void employeeAddQualification()
    {
        // CHECK TEST
        final int NEW_EMPLOYEE_ID = 420;
        final int OTHER_EMPLOYEE_ID = 21;

        g.addEmployee(NEW_EMPLOYEE_ID, "Snoop Dog", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID), 42000, LocalDateTime.now(), "Works in the morning. ");
        g.addEmployee(OTHER_EMPLOYEE_ID, "Other Otherman", bankAccountDetailsGenerator(OTHER_EMPLOYEE_ID), 999, LocalDateTime.now(), "Works every other day. ");

        Qualification qCashier = g.getQualification("Cashier").getData();
        Qualification qHR = g.getQualification("HR").getData();

        var r = g.employeeAddQualification(NEW_EMPLOYEE_ID, qCashier);
        assertTrue(r.getMessage(), r.isSuccess());

        var r1 = g.employeeAddQualification(NEW_EMPLOYEE_ID, qCashier);
        assertFalse("Shouldn't be able to add the same qualification twice. ", r.isSuccess());

        var r2 = g.employeeAddQualification(NEW_EMPLOYEE_ID, qHR);
        assertTrue(r2.getMessage(), r2.isSuccess());

        g.logout();
        g.login(NEW_EMPLOYEE_ID);

        var r3 = g.employeeAddQualification(OTHER_EMPLOYEE_ID, qCashier);
        assertTrue("Snoop Dog should now be HR. ", r3.getMessage(), r3.isSuccess());


    }

    // TODO Check possible bug where qualification pointer is saved,
    //  the qualification is deleted and then that pointer is still used
    //  when adding that qualification to an employee.

    @Test
    public void employeeRemoveQualification()
    {
        // CHECK TEST
        final int NEW_EMPLOYEE_ID = 420;
        final int OTHER_EMPLOYEE_ID = 21;

        g.addEmployee(NEW_EMPLOYEE_ID, "Snoop Dog", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID), 42000, LocalDateTime.now(), "Works in the morning. ");
        g.addEmployee(OTHER_EMPLOYEE_ID, "Other Otherman", bankAccountDetailsGenerator(OTHER_EMPLOYEE_ID), 999, LocalDateTime.now(), "Works every other day. ");

        Qualification qCashier = g.getQualification("Cashier").getData();
        Qualification qHR = g.getQualification("HR").getData();

        var r1 = g.employeeRemoveQualification(NEW_EMPLOYEE_ID, "Cashier");
        assertFalse("Employee shouldn't have the qualification yet. ", r1.isSuccess());

        g.employeeAddQualification(NEW_EMPLOYEE_ID, qCashier);
        var r = g.employeeRemoveQualification(NEW_EMPLOYEE_ID, "Cashier");
        assertTrue(r.getMessage(), r.isSuccess());

        var r2 = g.employeeRemoveQualification(NEW_EMPLOYEE_ID, "Cashier");
        assertFalse("Qualification should be removed. ", r2.isSuccess());

        g.employeeAddQualification(NEW_EMPLOYEE_ID, qHR);

        g.logout();
        g.login(NEW_EMPLOYEE_ID);

        var r3 = g.employeeRemoveQualification(NEW_EMPLOYEE_ID, "HR");
        assertTrue(r3.getMessage(), r3.isSuccess());

        var r4 = g.employeeAddQualification(NEW_EMPLOYEE_ID, qHR);
        assertFalse("Employee shouldn't be HR anymore. ", r4.isSuccess());
    }

    @Test
    public void addShift()
    {
        final int ID_MANAGER = 1337;
        var manager = g.addEmployee(ID_MANAGER, "Manager", bankAccountDetailsGenerator(ID_MANAGER), 42000, LocalDateTime.now(), "Branch 1 manager").getData();
        var w1 = g.addEmployee(21, "W1", bankAccountDetailsGenerator(21), 42000, LocalDateTime.now(), "Worker 1").getData();
        var w2 = g.addEmployee(22, "W2", bankAccountDetailsGenerator(22), 42000, LocalDateTime.now(), "Worker 2").getData();

        g.employeeAddQualification(manager.getId(), g.getQualification("Branch1Manager").getData());
        g.employeeAddQualification(w1.getId(), g.getQualification("Cashier").getData());
        g.employeeAddQualification(w1.getId(), g.getQualification("Cleaner").getData());
        Map<Employee, List<Qualification>> workers  = new HashMap<Employee, List<Qualification>>() {{
            put(w1, List.of(g.getQualification("Cashier").getData()));
            put(w2, List.of(g.getQualification("Cleaner").getData()));
        }};
        g.logout();
        g.login(ID_MANAGER);
        var r1 = g.addShift(1, LocalDateTime.of(2024, Month.MAY, 20, 1, 1), manager, workers, ShiftTime.DAY);
        assertTrue(r1.getMessage(), r1.isSuccess());

        var r2 = g.addShift(1, LocalDateTime.of(2024, Month.MAY, 20, 2, 30), manager, workers, ShiftTime.DAY);
        assertFalse("Should not be able to create 2 shifts at the same time. ", r2.isSuccess());

        var r3 = g.addShift(1, LocalDateTime.of(2024, Month.MAY, 21, 1, 1), manager, workers, ShiftTime.NIGHT);
        assertTrue(r3.isSuccess());

        var r4 = g.addShift(1, LocalDateTime.of(1998, Month.MAY, 20, 2, 30), manager, workers, ShiftTime.NIGHT);
        assertFalse("Should not be able to create a shift in the past.", r4.isSuccess());


    }

    @Test
    public void removeShift()
    {
        final int ID_MANAGER = 1337;
        var manager = g.addEmployee(ID_MANAGER, "Manager", bankAccountDetailsGenerator(ID_MANAGER), 42000, LocalDateTime.now(), "Branch 1 manager").getData();
        var w1 = g.addEmployee(21, "W1", bankAccountDetailsGenerator(21), 42000, LocalDateTime.now(), "Worker 1").getData();
        var w2 = g.addEmployee(22, "W2", bankAccountDetailsGenerator(22), 42000, LocalDateTime.now(), "Worker 2").getData();

        g.employeeAddQualification(manager.getId(), g.getQualification("Branch1Manager").getData());
        g.employeeAddQualification(w1.getId(), g.getQualification("Cashier").getData());
        g.employeeAddQualification(w1.getId(), g.getQualification("Cleaner").getData());
        Map<Employee, List<Qualification>> workers  = new HashMap<Employee, List<Qualification>>() {{
            put(w1, List.of(g.getQualification("Cashier").getData()));
            put(w2, List.of(g.getQualification("Cleaner").getData()));
        }};
        g.logout();
        g.login(ID_MANAGER);
        Shift s1 = g.addShift(1, LocalDateTime.of(2024, Month.MAY, 20, 1, 1), manager, workers, ShiftTime.DAY).getData();

        var r1 = g.removeShift(s1.getId());
        assertTrue(r1.getMessage(), r1.isSuccess());

        var r2 = g.removeShift(s1.getId());
        assertFalse("Shouldn't be able to remove a non existing shift.  ", r2.isSuccess());

        var r3 = g.getShifts(1);
        assertTrue(r3.getMessage(), r3.isSuccess());
        assertEquals("The shift should be removed. ", r3.getData().size(), 0);
    }

    @Test
    public void addWorker()
    {
        final int ID_MANAGER = 1337;
        var manager = g.addEmployee(ID_MANAGER, "Manager", bankAccountDetailsGenerator(ID_MANAGER), 42000, LocalDateTime.now(), "Branch 1 manager").getData();
        var w1 = g.addEmployee(21, "W1", bankAccountDetailsGenerator(21), 42000, LocalDateTime.now(), "Worker 1").getData();
        var w2 = g.addEmployee(22, "W2", bankAccountDetailsGenerator(22), 42000, LocalDateTime.now(), "Worker 2").getData();

        g.employeeAddQualification(manager.getId(), g.getQualification("Branch1Manager").getData());
        g.employeeAddQualification(w1.getId(), g.getQualification("Cashier").getData());
        var qualificationCleaner = g.employeeAddQualification(w1.getId(), g.getQualification("Cleaner").getData()).getData();
        Map<Employee, List<Qualification>> workers  = new HashMap<Employee, List<Qualification>>() {{
            put(w1, List.of(g.getQualification("Cashier").getData()));
        }};
        g.logout();
        g.login(ID_MANAGER);
        Shift s1 = g.addShift(1, LocalDateTime.of(2024, Month.MAY, 20, 1, 1), manager, workers, ShiftTime.DAY).getData();

        var r1 = g.addWorker(s1.getId(), w2, List.of(qualificationCleaner));
        assertTrue(r1.getMessage(), r1.isSuccess());

        var r2 = s1.getWorkers().keySet();
        assertEquals("There should be 2 employees after this. ", r2.size(), 2);
    }

    @Test
    public void removeWorker()
    {
        final int ID_MANAGER = 1337;
        var manager = g.addEmployee(ID_MANAGER, "Manager", bankAccountDetailsGenerator(ID_MANAGER), 42000, LocalDateTime.now(), "Branch 1 manager").getData();
        var w1 = g.addEmployee(21, "W1", bankAccountDetailsGenerator(21), 42000, LocalDateTime.now(), "Worker 1").getData();
        var w2 = g.addEmployee(22, "W2", bankAccountDetailsGenerator(22), 42000, LocalDateTime.now(), "Worker 2").getData();

        g.employeeAddQualification(manager.getId(), g.getQualification("Branch1Manager").getData());
        g.employeeAddQualification(w1.getId(), g.getQualification("Cashier").getData());
        g.employeeAddQualification(w1.getId(), g.getQualification("Cleaner").getData());
        Map<Employee, List<Qualification>> workers  = new HashMap<Employee, List<Qualification>>() {{
            put(w1, List.of(g.getQualification("Cashier").getData()));
            put(w2, List.of(g.getQualification("Cleaner").getData()));
        }};
        g.logout();
        g.login(ID_MANAGER);
        Shift s1 = g.addShift(1, LocalDateTime.of(2024, Month.MAY, 20, 1, 1), manager, workers, ShiftTime.DAY).getData();

        var r1 = g.removeWorker(s1.getId(), w2);
        assertTrue(r1.getMessage(), r1.isSuccess());

        var r2 = g.removeWorker(s1.getId(), w2);
        assertFalse("Employee should already be removed. ", r2.isSuccess());

        var r3 = s1.getWorkers().keySet();
        assertEquals("There should be 1 employees after this. ", r3.size(), 1);
    }

    @Test
    public void getQualifications()
    {
        var r = g.getQualifications();
        assertTrue(r.getMessage(), r.isSuccess());
    }

    @Test
    public void addQualification()
    {
        final int GUEST_ID = 1800400;
        var guest = g.addEmployee(GUEST_ID, "guest", bankAccountDetailsGenerator(1800400), 1, LocalDateTime.now(), "guest with no permissions. ").getData();

        var r = g.addQualification("TempQualification");
        assertTrue(r.getMessage(), r.isSuccess());

        var r1 = g.getQualification("TempQualification");
        assertTrue(r1.getMessage(), r1.isSuccess());

        var r2 = g.addQualification("TempQualification");
        assertFalse("Shouldn't be able to add the same qualification twice. ", r2.isSuccess());

        g.logout();
        g.login(GUEST_ID);
        var r3 = g.addQualification("");
        assertFalse("Shouldn't be able to add a new qualification as guest user. ", r3.isSuccess());
    }

//    @Test
//    public void renameQualification()
//    {
//        // TODO this test is not checked and might not work!
//        final int GUEST_ID = 1800400;
//        var guest = g.addEmployee(GUEST_ID, "guest", bankAccountDetailsGenerator(1800400), 1, LocalDateTime.now(), "guest with no permissions. ").getData();
//
//        final String n1 = "TempQualification";
//        final String n2 = "TempQualification2";
//
//        g.addQualification(n1);
//        g.addQualification(n2);
//
//        var r = g.getQualification(n1);
//        assertTrue(r.getMessage(), r.isSuccess());
//
//        var r2 = g.renameQualification(n1, n2);
//        assertTrue(r.getMessage(), r.isSuccess());
//
//        var r3 = g.getQualification(n1);
//        assertFalse("qualification should be removed. ", r.isSuccess());
//
//        var r4 = g.getQualification(n2);
//        assertTrue(r.getMessage(), r.isSuccess());
//    }

    @Test
    public void addPermission()
    {
        final int GUEST_ID = 1800400;
        var guest = g.addEmployee(GUEST_ID, "guest", bankAccountDetailsGenerator(1800400), 1, LocalDateTime.now(), "guest with no permissions. ").getData();

        var r = g.addPermission("TempPermission");
        assertTrue(r.getMessage(), r.isSuccess());

        var r2 = g.addPermission("TempPermission");
        assertFalse("Shouldn't be able to add the same permission twice. ", r2.isSuccess());

        g.logout();
        g.login(GUEST_ID);
        var r3 = g.addPermission("");
        assertFalse("Shouldn't be able to add a new permission as guest user. ", r3.isSuccess());
    }

    @Test
    public void removePermission()
    {
        final int GUEST_ID = 1800400;
        var guest = g.addEmployee(GUEST_ID, "guest", bankAccountDetailsGenerator(1800400), 1, LocalDateTime.now(), "guest with no permissions. ").getData();

        g.addPermission("TempPermission");
        int permCount = g.getPermissions().getData().size();

        var r = g.removePermission("TempPermission");
        assertTrue(r.getMessage(), r.isSuccess());

        // check that the permission has indeed been removed.
        assertEquals("Expected the permission count to be smaller. ", g.getPermissions().getData().size(), (permCount - 1));

        var r2 = g.removePermission("TempPermission");
        assertFalse("Shouldn't be able to remove the same permission twice. ", r2.isSuccess());

        g.logout();
        g.login(GUEST_ID);
        var r3 = g.addPermission("");
        assertFalse("Shouldn't be able to remove a permission as guest user. ", r3.isSuccess());
    }

    @Test
    public void addPermissionToQualification()
    {
        g.addPermission("TempPermission");
        g.addQualification("TempQualification");

        var r = g.addPermissionToQualification("TempPermission", "TempQualification");
        assertTrue(r.getMessage(), r.isSuccess());

        var r2 = g.addPermissionToQualification("TempPermission", "TempQualification");
        assertFalse("Should not be able to add the same permission to a qualification twice. ", r2.isSuccess());
    }

    @Test
    public void removePermissionFromQualification()
    {
        g.addPermission("TempPermission");
        g.addQualification("TempQualification");

        g.addPermissionToQualification("TempPermission", "TempQualification");

        var r = g.removePermissionFromQualification("TempPermission", "TempQualification");
        assertTrue(r.getMessage(), r.isSuccess());

        var r2 = g.removePermissionFromQualification("TempPermission", "TempQualification");
        assertFalse("Should not be able to remove the same permission from a qualification twice. ", r2.isSuccess());
    }
}