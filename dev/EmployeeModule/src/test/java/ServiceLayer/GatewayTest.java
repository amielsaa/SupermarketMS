package ServiceLayer;

import BusinessLayer.BankAccountDetails;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Random;

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
        fail();
    }

    @Test
    public void removeShift()
    {
        fail();
    }

    @Test
    public void addWorker()
    {
        fail();
    }

    @Test
    public void removeWorker()
    {
        fail();
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