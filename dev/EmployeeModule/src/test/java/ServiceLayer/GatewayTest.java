package ServiceLayer;

import BusinessLayer.*;
import org.junit.After;
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
        fail();
    }

    @Test
    public void updateEmployeeName()
    {
        fail();
    }

    @Test
    public void updateEmployeeSalary()
    {
        fail();
    }

    @Test
    public void updateEmployeeBankAccountDetails()
    {
        fail();
    }

    @Test
    public void employeeAddWorkingHour()
    {
        fail();
    }

    @Test
    public void employeeRemoveWorkingHour()
    {
        fail();
    }

    @Test
    public void employeeAddQualification()
    {
        fail();
    }

    @Test
    public void employeeRemoveQualification()
    {
        fail();
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
        assertFalse("Employee should already be removed. ", r1.isSuccess());

        var r3 = s1.getWorkers().keySet();
        assertEquals("There should be 1 employees after this. ", r3.size(), 1);
    }

    @Test
    public void getQualifications()
    {
        fail();
    }

    @Test
    public void addQualification()
    {
        fail();
    }

    @Test
    public void renameQualification()
    {
        fail();
    }

    @Test
    public void addPermission()
    {
        fail();
    }

    @Test
    public void removePermission()
    {
        fail();
    }

    @Test
    public void addPermissionToQualification()
    {
        fail();
    }

    @Test
    public void removePermissionFromQualification()
    {
        fail();
    }
}