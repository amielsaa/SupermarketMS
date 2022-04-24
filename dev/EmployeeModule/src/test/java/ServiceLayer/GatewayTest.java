package ServiceLayer;

import BusinessLayer.*;
import Utilities.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
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
        String bankName = "BANK_NAMED_" + seed;
        String branchName = "BRANCH_NAMED_" + seed;
        String accountOwner = "ACCOUNT_NAMED_" + seed;
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
        Response<Employee> r = g.getLoggedUser();
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
        Response<Employee> r = g.addEmployee(NEW_EMPLOYEE_ID, "Snoop Dog", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID), 42000, LocalDateTime.now(), "Works in the morning. ");
        assertTrue(r.getMessage(), r.isSuccess());

        Response<Employee> r2 = g.addEmployee(NEW_EMPLOYEE_ID, "Snoop Dog (Imposter)", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID), 420, LocalDateTime.now(), "Works in the morning. ");
        assertFalse("Employee should already exist. ", r2.isSuccess());

        final int NEW_EMPLOYEE_ID_2 = 7355608;
        Response<Employee> r3 = g.addEmployee(NEW_EMPLOYEE_ID_2, "Shroud", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID_2), 2000000, LocalDateTime.now(), "Work from home boi. ");
        assertTrue(r.getMessage(), r3.isSuccess());
    }

    @Test
    public void removeEmployee()
    {
        // CHECK TEST
        final int NEW_EMPLOYEE_ID = 420;
        g.addEmployee(NEW_EMPLOYEE_ID, "Snoop Dog", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID), 42000, LocalDateTime.now(), "Works in the morning. ");

        int employeeCount = g.getEmployees().getData().size();

        Response<Employee> r = g.removeEmployee(NEW_EMPLOYEE_ID);
        assertTrue(r.getMessage(), r.isSuccess());

        assertEquals("Employee count should drop by 1. ", employeeCount - 1, g.getEmployees().getData().size());

        Response<Employee> r2 = g.removeEmployee(NEW_EMPLOYEE_ID);
        assertFalse("Employee should already be removed. ", r2.isSuccess());

        Response<Employee> r3 = g.getEmployee(NEW_EMPLOYEE_ID);
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
        assertEquals("Employee salary not initiated correctly. ", 42000.0, e.getSalary(), 0.01);
        g.updateEmployeeSalary(NEW_EMPLOYEE_ID, 420);

        assertEquals("Employee salary should be updated. ",  420.0, e.getSalary(), 0.01);
    }

    @Test
    public void updateEmployeeBankAccountDetails()
    {
        // CHECK TEST
        final int NEW_EMPLOYEE_ID = 420;
        g.addEmployee(NEW_EMPLOYEE_ID, "Snoop Dog", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID), 42000, LocalDateTime.now(), "Works in the morning. ");

        Employee e = g.getEmployee(NEW_EMPLOYEE_ID).getData();
        assertEquals("Employee bank account details not initiated correctly. ", "BANK_NAMED_420", e.getBankAccountDetails().bankName());

        BankAccountDetails newDetails = bankAccountDetailsGenerator(21);

        g.updateEmployeeBankAccountDetails(NEW_EMPLOYEE_ID, newDetails);

        assertEquals("Employee bank account details should be updated. ", "BANK_NAMED_21", e.getBankAccountDetails().bankName());
    }

    @Test
    public void employeeAddWorkingHour()
    {
        // CHECK TEST
        final int NEW_EMPLOYEE_ID = 420;
        final int OTHER_EMPLOYEE_ID = 21;

        g.addEmployee(NEW_EMPLOYEE_ID, "Snoop Dog", bankAccountDetailsGenerator(NEW_EMPLOYEE_ID), 42000, LocalDateTime.now(), "Works in the morning. ");
        g.addEmployee(OTHER_EMPLOYEE_ID, "Other Otherman", bankAccountDetailsGenerator(OTHER_EMPLOYEE_ID), 999, LocalDateTime.now(), "Works every other day. ");

        Response<TimeInterval> r1 = g.employeeAddWorkingHour(NEW_EMPLOYEE_ID, LocalDateTime.now().plusMinutes(5), LocalDateTime.now().minusHours(2));
        assertFalse("Shouldn't be able to add an illegal TimeInterval as new working hour. ", r1.isSuccess());

        Response<TimeInterval> r = g.employeeAddWorkingHour(NEW_EMPLOYEE_ID, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3));
        assertTrue(r.getMessage(), r.isSuccess());

        Response<TimeInterval> r2 = g.employeeAddWorkingHour(NEW_EMPLOYEE_ID, LocalDateTime.now().plusDays(2).plusHours(5), LocalDateTime.now().plusDays(2).minusHours(1));
        assertFalse("Shouldn't be able to add overlapping hours. ", r2.isSuccess());

        g.logout();
        g.login(NEW_EMPLOYEE_ID);

        Response<TimeInterval> r3 = g.employeeAddWorkingHour(OTHER_EMPLOYEE_ID, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3));
        assertFalse("Shouldn't be able to add to other employee as another non permitted employee. ", r3.isSuccess());

        Response<TimeInterval> r4 = g.employeeAddWorkingHour(NEW_EMPLOYEE_ID, LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(4).plusHours(2));
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
        Response<TimeInterval> r1 = g.employeeRemoveWorkingHour(NEW_EMPLOYEE_ID, timeStamp.plusHours(1));
        assertTrue(r1.getMessage(), r1.isSuccess());

        Response<TimeInterval> r2 = g.employeeRemoveWorkingHour(NEW_EMPLOYEE_ID, timeStamp.plusHours(1));
        assertFalse("Shouldn't be able to remove the same hour twice. ", r2.isSuccess());

        g.employeeAddWorkingHour(NEW_EMPLOYEE_ID, timeStamp.plusHours(1), timeStamp.plusHours(2));

        g.logout();
        g.login(NEW_EMPLOYEE_ID);

        Response<TimeInterval> r3 = g.employeeRemoveWorkingHour(NEW_EMPLOYEE_ID, timeStamp.plusHours(1));
        assertTrue(r1.getMessage(), r1.isSuccess());

        Response<TimeInterval> r4 = g.employeeRemoveWorkingHour(OTHER_EMPLOYEE_ID, timeStamp.plusDays(4));
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

        Response<Qualification> r = g.employeeAddQualification(NEW_EMPLOYEE_ID, qCashier);
        assertTrue(r.getMessage(), r.isSuccess());

        Response<Qualification> r1 = g.employeeAddQualification(NEW_EMPLOYEE_ID, qCashier);
        assertFalse("Shouldn't be able to add the same qualification twice. ", r1.isSuccess());

        Response<Qualification> r2 = g.employeeAddQualification(NEW_EMPLOYEE_ID, qHR);
        assertTrue(r2.getMessage(), r2.isSuccess());

        g.logout();
        g.login(NEW_EMPLOYEE_ID);

        Response<Qualification> r3 = g.employeeAddQualification(OTHER_EMPLOYEE_ID, qCashier);
        assertTrue("Snoop Dog should now be HR. ", r3.isSuccess());


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

        Response<Qualification> r1 = g.employeeRemoveQualification(NEW_EMPLOYEE_ID, "Cashier");
        assertFalse("Employee shouldn't have the qualification yet. ", r1.isSuccess());

        g.employeeAddQualification(NEW_EMPLOYEE_ID, qCashier);
        Response<Qualification> r = g.employeeRemoveQualification(NEW_EMPLOYEE_ID, "Cashier");
        assertTrue(r.getMessage(), r.isSuccess());

        Response<Qualification> r2 = g.employeeRemoveQualification(NEW_EMPLOYEE_ID, "Cashier");
        assertFalse("Qualification should be removed. ", r2.isSuccess());

        g.employeeAddQualification(NEW_EMPLOYEE_ID, qHR);

        g.logout();
        g.login(NEW_EMPLOYEE_ID);

        Response<Qualification> r3 = g.employeeRemoveQualification(NEW_EMPLOYEE_ID, "HR");
        assertTrue(r3.getMessage(), r3.isSuccess());

        Response<Qualification> r4 = g.employeeAddQualification(NEW_EMPLOYEE_ID, qHR);
        assertFalse("Employee shouldn't be HR anymore. ", r4.isSuccess());
    }

    @Test
    public void addShift()
    {
        final int ID_MANAGER = 1337;
        Employee manager = g.addEmployee(ID_MANAGER, "Manager", bankAccountDetailsGenerator(ID_MANAGER), 42000, LocalDateTime.now(), "Branch 1 manager").getData();
        Employee w1 = g.addEmployee(21, "W1", bankAccountDetailsGenerator(21), 42000, LocalDateTime.now(), "Worker 1").getData();
        Employee w2 = g.addEmployee(22, "W2", bankAccountDetailsGenerator(22), 42000, LocalDateTime.now(), "Worker 2").getData();

        g.employeeAddQualification(manager.getId(), g.getQualification("Branch1Manager").getData());
        g.employeeAddQualification(w1.getId(), g.getQualification("Cashier").getData());
        g.employeeAddQualification(w1.getId(), g.getQualification("Cleaner").getData());
        Map<Employee, List<Qualification>> workers  = new HashMap<Employee, List<Qualification>>() {{
            put(w1, Arrays.asList(g.getQualification("Cashier").getData()));
            put(w2, Arrays.asList(g.getQualification("Cleaner").getData()));
        }};
        g.logout();
        g.login(ID_MANAGER);
        Response<Shift> r1 = g.addShift(1, LocalDateTime.of(2024, Month.MAY, 20, 1, 1), manager, workers, ShiftTime.DAY);
        assertTrue(r1.getMessage(), r1.isSuccess());

        Response<Shift> r2 = g.addShift(1, LocalDateTime.of(2024, Month.MAY, 20, 2, 30), manager, workers, ShiftTime.DAY);
        assertFalse("Should not be able to create 2 shifts at the same time. ", r2.isSuccess());

        Response<Shift> r3 = g.addShift(1, LocalDateTime.of(2024, Month.MAY, 21, 1, 1), manager, workers, ShiftTime.NIGHT);
        assertTrue(r3.isSuccess());

        Response<Shift> r4 = g.addShift(1, LocalDateTime.of(1998, Month.MAY, 20, 2, 30), manager, workers, ShiftTime.NIGHT);
        assertFalse("Should not be able to create a shift in the past.", r4.isSuccess());


    }

    @Test
    public void removeShift()
    {
        final int ID_MANAGER = 1337;
        Employee manager = g.addEmployee(ID_MANAGER, "Manager", bankAccountDetailsGenerator(ID_MANAGER), 42000, LocalDateTime.now(), "Branch 1 manager").getData();
        Employee w1 = g.addEmployee(21, "W1", bankAccountDetailsGenerator(21), 42000, LocalDateTime.now(), "Worker 1").getData();
        Employee w2 = g.addEmployee(22, "W2", bankAccountDetailsGenerator(22), 42000, LocalDateTime.now(), "Worker 2").getData();

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

        Response<Shift> r1 = g.removeShift(s1.getId());
        assertTrue(r1.getMessage(), r1.isSuccess());

        Response<Shift> r2 = g.removeShift(s1.getId());
        assertFalse("Shouldn't be able to remove a non existing shift.  ", r2.isSuccess());

        Response<List<Shift>> r3 = g.getShifts(1);
        assertTrue(r3.getMessage(), r3.isSuccess());
        assertEquals("The shift should be removed. ", r3.getData().size(), 0);
    }

    @Test
    public void addWorker()
    {
        final int ID_MANAGER = 1337;
        Employee manager = g.addEmployee(ID_MANAGER, "Manager", bankAccountDetailsGenerator(ID_MANAGER), 42000, LocalDateTime.now(), "Branch 1 manager").getData();
        Employee w1 = g.addEmployee(21, "W1", bankAccountDetailsGenerator(21), 42000, LocalDateTime.now(), "Worker 1").getData();
        Employee w2 = g.addEmployee(22, "W2", bankAccountDetailsGenerator(22), 42000, LocalDateTime.now(), "Worker 2").getData();

        g.employeeAddQualification(manager.getId(), g.getQualification("Branch1Manager").getData());
        g.employeeAddQualification(w1.getId(), g.getQualification("Cashier").getData());
        Qualification qualificationCleaner = g.employeeAddQualification(w1.getId(), g.getQualification("Cleaner").getData()).getData();
        Map<Employee, List<Qualification>> workers  = new HashMap<Employee, List<Qualification>>() {{
            put(w1, List.of(g.getQualification("Cashier").getData()));
        }};
        g.logout();
        g.login(ID_MANAGER);
        Shift s1 = g.addShift(1, LocalDateTime.of(2024, Month.MAY, 20, 1, 1), manager, workers, ShiftTime.DAY).getData();

        Response<Employee> r1 = g.addWorker(s1.getId(), w2, Arrays.asList(qualificationCleaner));
        assertTrue(r1.getMessage(), r1.isSuccess());

        Set<Employee> r2 = s1.getWorkers().keySet();
        assertEquals("There should be 2 employees after this. ", r2.size(), 2);
    }

    @Test
    public void removeWorker()
    {
        final int ID_MANAGER = 1337;
        Employee manager = g.addEmployee(ID_MANAGER, "Manager", bankAccountDetailsGenerator(ID_MANAGER), 42000, LocalDateTime.now(), "Branch 1 manager").getData();
        Employee w1 = g.addEmployee(21, "W1", bankAccountDetailsGenerator(21), 42000, LocalDateTime.now(), "Worker 1").getData();
        Employee w2 = g.addEmployee(22, "W2", bankAccountDetailsGenerator(22), 42000, LocalDateTime.now(), "Worker 2").getData();

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

        Response<Employee> r1 = g.removeWorker(s1.getId(), w2);
        assertTrue(r1.getMessage(), r1.isSuccess());

        Response<Employee> r2 = g.removeWorker(s1.getId(), w2);
        assertFalse("Employee should already be removed. ", r2.isSuccess());

        Set<Employee> r3 = s1.getWorkers().keySet();
        assertEquals("There should be 1 employees after this. ", r3.size(), 1);
    }

    @Test
    public void getQualifications()
    {
        Response<List<Qualification>> r = g.getQualifications();
        assertTrue(r.getMessage(), r.isSuccess());
    }

    @Test
    public void addQualification()
    {
        final int GUEST_ID = 1800400;
        Employee guest = g.addEmployee(GUEST_ID, "guest", bankAccountDetailsGenerator(1800400), 1, LocalDateTime.now(), "guest with no permissions. ").getData();

        Response<Qualification> r = g.addQualification("TempQualification");
        assertTrue(r.getMessage(), r.isSuccess());

        Response<Qualification> r1 = g.getQualification("TempQualification");
        assertTrue(r1.getMessage(), r1.isSuccess());

        Response<Qualification> r2 = g.addQualification("TempQualification");
        assertFalse("Shouldn't be able to add the same qualification twice. ", r2.isSuccess());

        g.logout();
        g.login(GUEST_ID);
        Response<Qualification> r3 = g.addQualification("");
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
        Employee guest = g.addEmployee(GUEST_ID, "guest", bankAccountDetailsGenerator(1800400), 1, LocalDateTime.now(), "guest with no permissions. ").getData();

        Response<Permission> r = g.addPermission("TempPermission");
        assertTrue(r.getMessage(), r.isSuccess());

        Response<Permission> r2 = g.addPermission("TempPermission");
        assertFalse("Shouldn't be able to add the same permission twice. ", r2.isSuccess());

        g.logout();
        g.login(GUEST_ID);
        Response<Permission> r3 = g.addPermission("");
        assertFalse("Shouldn't be able to add a new permission as guest user. ", r3.isSuccess());
    }

    @Test
    public void removePermission()
    {
        final int GUEST_ID = 1800400;
        Employee guest = g.addEmployee(GUEST_ID, "guest", bankAccountDetailsGenerator(1800400), 1, LocalDateTime.now(), "guest with no permissions. ").getData();

        g.addPermission("TempPermission");
        int permCount = g.getPermissions().getData().size();

        Response<Permission> r = g.removePermission("TempPermission");
        assertTrue(r.getMessage(), r.isSuccess());

        // check that the permission has indeed been removed.
        assertEquals("Expected the permission count to be smaller. ", g.getPermissions().getData().size(), (permCount - 1));

        Response<Permission> r2 = g.removePermission("TempPermission");
        assertFalse("Shouldn't be able to remove the same permission twice. ", r2.isSuccess());

        g.logout();
        g.login(GUEST_ID);
        Response<Permission> r3 = g.addPermission("");
        assertFalse("Shouldn't be able to remove a permission as guest user. ", r3.isSuccess());
    }

    @Test
    public void addPermissionToQualification()
    {
        g.addPermission("TempPermission");
        g.addQualification("TempQualification");

        Response<Qualification> r = g.addPermissionToQualification("TempPermission", "TempQualification");
        assertTrue(r.getMessage(), r.isSuccess());

        Response<Qualification> r2 = g.addPermissionToQualification("TempPermission", "TempQualification");
        assertFalse("Should not be able to add the same permission to a qualification twice. ", r2.isSuccess());
    }

    @Test
    public void removePermissionFromQualification()
    {
        g.addPermission("TempPermission");
        g.addQualification("TempQualification");

        g.addPermissionToQualification("TempPermission", "TempQualification");

        Response<Qualification> r = g.removePermissionFromQualification("TempPermission", "TempQualification");
        assertTrue(r.getMessage(), r.isSuccess());

        Response<Qualification> r2 = g.removePermissionFromQualification("TempPermission", "TempQualification");
        assertFalse("Should not be able to remove the same permission from a qualification twice. ", r2.isSuccess());
    }
}