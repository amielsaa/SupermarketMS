package ServiceLayer;

import BusinessLayer.*;
import DataAccessLayer.DALController;
import Utilities.Response;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.*;

public class Gateway
{
    private int loggedEmployeeId;
    private EmployeeController employeeController;
    private ShiftController shiftController;
    private QualificationController qualificationController;
    private DALController dalController;

    private final int ADMIN_UID = 580083434;

    public Gateway() {
        this.loggedEmployeeId = -1;

        this.dalController = new DALController();
        this.employeeController = new EmployeeController(dalController);
        this.shiftController = new ShiftController(dalController);
        this.qualificationController = new QualificationController(dalController);
    }

    public void initDefaultData() throws Exception {
        // TODO DAL make this run once on database init and NOT delete the entire database on load.
        employeeController.clearDatabases();
        shiftController.clearDatabases();
        qualificationController.clearDatabases();
        // INIT PERMISSIONS AND QUALIFICATIONS

        String[] permissions = {"ViewEmployees", "ManageEmployees", "ViewQualifications", "ManageQualifications", "ManageBranch1", "ManageBranch2", "ManageShift"};
        for (String p : permissions)
        {
            qualificationController.addPermission(p);
        }

        String[] permissionsHR = {"ViewEmployees", "ManageEmployees", "ViewQualifications", "ManageQualifications"};


        Qualification qualificationHR = qualificationController.addQualification("HR");
        String[] permissionsAssistant = {"ViewEmployees", "ViewQualifications"};
        for(String p : permissionsHR) {
            qualificationController.addPermissionToQualification(p, qualificationHR.getName());
        }

        Qualification qualificationBranch1Manager = qualificationController.addQualification("Branch1Manager");
        String[] permissionsBranch1Manager = {"ManageBranch1", "ManageShift", "ViewQualifications", "ViewEmployees"};
        for(String p : permissionsBranch1Manager) {
            qualificationController.addPermissionToQualification(p, qualificationBranch1Manager.getName());
        }

        Qualification qualificationBranch2Manager = qualificationController.addQualification("Branch2Manager");
        String[] permissionsBranch2Manager = {"ManageBranch2", "ManageShift", "ViewQualifications", "ViewEmployees"};
        for(String p : permissionsBranch2Manager) {
            qualificationController.addPermissionToQualification(p, qualificationBranch2Manager.getName());
        }


        Qualification qualificationShiftManager = qualificationController.addQualification("ShiftManager");
        Qualification qualificationCashier = qualificationController.addQualification("Cashier");
        Qualification qualificationWarehouse = qualificationController.addQualification("WarehouseWorker");
        Qualification qualificationStock = qualificationController.addQualification("StockClerk");
        Qualification qualificationTruck = qualificationController.addQualification("TruckDriver");
        Qualification qualificationCleaner = qualificationController.addQualification("Cleaner");
        Qualification qualificationInventoryManager = qualificationController.addQualification("InventoryManager");
        Qualification qualificationDriver = qualificationController.addQualification("Driver");

        qualificationController.addPermissionToQualification("ManageShift", "ShiftManager");

        // INIT EMPLOYEES
        BankAccountDetails defaultBankAccountDetails = new BankAccountDetails(0, 0, 0, "Bank", "Branch", "Bob");
        employeeController.addEmployee(ADMIN_UID, "Admin", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(123, "Admin2", defaultBankAccountDetails, 0, LocalDateTime.now(), "");

        employeeController.addEmployee(1, "Bob The Cashier", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(2, "Alice The Warehouse Worker", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(3, "Cat The Stock Clerk", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(4, "Dan The Truck Driver", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(5, "Manny The Manager", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(6, "InManny The InManager", defaultBankAccountDetails, 0, LocalDateTime.now(), "");

        employeeController.employeeAddQualification(1, qualificationCashier);
        employeeController.employeeAddQualification(2, qualificationWarehouse);
        employeeController.employeeAddQualification(3, qualificationStock);
        employeeController.employeeAddQualification(4, qualificationTruck);
        employeeController.employeeAddQualification(6, qualificationInventoryManager);

        employeeController.employeeAddQualification(1, qualificationShiftManager);
        employeeController.employeeAddQualification(2, qualificationShiftManager);
        employeeController.employeeAddQualification(3, qualificationShiftManager);
        employeeController.employeeAddQualification(4, qualificationShiftManager);

        employeeController.employeeAddQualification(5, qualificationBranch1Manager);
        employeeController.employeeAddQualification(6, qualificationShiftManager);



        employeeController.employeeAddQualification(ADMIN_UID, qualificationHR);
        employeeController.employeeAddQualification(123, qualificationHR);
    }

    // TODO change to user-pass authentication
    public Response<Employee> login(int id) {
        try
        {
            Employee e = employeeController.getEmployee(id);
            this.loggedEmployeeId = id;
            return Response.makeSuccess(e);
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }
    public Response<Employee> getLoggedUser() {
        if(loggedEmployeeId == -1) {
            return Response.makeFailure("No user is logged in. ");
        }
        try {
            Employee e = employeeController.getEmployee(loggedEmployeeId);
            return Response.makeSuccess(e);
        } catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }
    public void logout() {
        this.loggedEmployeeId = -1;
    }

    private void checkAuth(String permission) throws Exception
    {
        if (loggedEmployeeId == -1)
        {
            throw new Exception("Not logged in.");
        }
        List<String> qualNames = employeeController.getEmployee(loggedEmployeeId).getWorkingConditions().getQualifications();
        Permission p = qualificationController.getPermission(permission);
        Permission p2 = qualificationController.checkPermission(qualNames, p);
    }

    // EMPLOYEE FUNCTIONS

    public Response<Employee> getEmployee(int id) {
        try
        {
            //checkAuth("ViewEmployees");
            return Response.makeSuccess(employeeController.getEmployee(id));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<List<Employee>> getEmployees() {
        try
        {
            //checkAuth("ViewEmployees");

            return Response.makeSuccess(employeeController.getEmployees());
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Employee> addEmployee(int id, @NotNull String name, @NotNull BankAccountDetails bankAccountDetails,
                                          double salary, @NotNull LocalDateTime workStartingDate, @NotNull String workingConditionsDescription) {
        try
        {
            checkAuth("ManageEmployees");

            return Response.makeSuccess(employeeController.addEmployee(id, name, bankAccountDetails, salary, workStartingDate, workingConditionsDescription));
        } catch (Exception e)
        {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Employee> removeEmployee(int id) {
        try
        {
            checkAuth("ManageEmployees");

            return Response.makeSuccess(employeeController.removeEmployee(id));
        } catch (Exception e)
        {
            return Response.makeFailure(e.getMessage());
        }
    }



    public Response<String> updateEmployeeName(int id, @NotNull String newName) {
        // added so each employee can change their own name
        try {
            // check if not updating myself
            if(loggedEmployeeId != id) {
                checkAuth("ManageEmployees");
            }

            return Response.makeSuccess(employeeController.updateEmployeeName(id, newName));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Double> updateEmployeeSalary(int id, double newSalary) {
        try
        {
            checkAuth("ManageEmployees");

            return Response.makeSuccess(employeeController.updateEmployeeSalary(id, newSalary));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<BankAccountDetails> updateEmployeeBankAccountDetails(int id, @NotNull BankAccountDetails newBankAccountDetails) {
        try
        {
            checkAuth("ManageEmployees");

            return Response.makeSuccess(employeeController.updateEmployeeBankAccountDetails(id, newBankAccountDetails));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<TimeInterval> employeeAddWorkingHour(int id, @NotNull LocalDateTime start, @NotNull LocalDateTime end)
    {
        try
        {
            // added so each employee can add their hours
            if (loggedEmployeeId != id)
            {
                checkAuth("ManageEmployees");
            }
            return Response.makeSuccess(employeeController.employeeAddWorkingHour(id, start, end));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<TimeInterval> employeeRemoveWorkingHour(int id, @NotNull LocalDateTime start) {
        try
        {
            // added so each employee can remove their hours
            if (loggedEmployeeId != id)
            {
                checkAuth("ManageEmployees");
            }

            return Response.makeSuccess(employeeController.employeeRemoveWorkingHour(id, start));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Qualification> employeeAddQualification(int id, Qualification qualification)
    {
        try {
            checkAuth("ManageEmployees");

            return Response.makeSuccess(employeeController.employeeAddQualification(id, qualification));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Qualification> employeeRemoveQualification(int id, @NotNull String name) {
        try
        {
            checkAuth("ManageEmployees");
            Qualification toRemove = qualificationController.getQualification(name);
            employeeController.employeeRemoveQualification(id, name);
            return Response.makeSuccess(toRemove);
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    // -----------------------------

    // SHIFTS FUNCTIONS

    public Response<List<Shift>> getShifts(int branchId) {
        try
        {
            //checkAuth("ManageBranch" + branchId);

            return Response.makeSuccess(shiftController.getShifts(branchId));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Shift> addShift(int branchId, @NotNull LocalDateTime date, @NotNull Employee shiftManager,
                                    @NotNull Map<Integer, List<String>> workers, @NotNull ShiftTime shiftTime) {
        try
        {
            checkAuth("ManageBranch" + branchId);

            return Response.makeSuccess(shiftController.addShift(branchId, date, shiftManager, workers, shiftTime));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Shift> removeShift(@NotNull ShiftId shiftId){
        try
        {
            checkAuth("ManageBranch" + shiftId.getBranchId());

            return Response.makeSuccess(shiftController.removeShift(shiftId));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Employee> addWorker(@NotNull ShiftId shiftId, @NotNull Employee worker, @NotNull List<String> qualifications){
        try
        {
            checkAuth("ManageBranch" + shiftId.getBranchId());

            return Response.makeSuccess(shiftController.addWorker(shiftId, worker, qualifications));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Employee> removeWorker(@NotNull ShiftId shiftId, @NotNull Employee worker){
        try
        {
            checkAuth("ManageBranch" + shiftId.getBranchId());

            return Response.makeSuccess(shiftController.removeWorker(shiftId, worker));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    // -----------------------------

    // SHIFTS FUNCTIONS

    public Response<Qualification> getQualification(String name) {
        try
        {
            //checkAuth("ViewQualifications");

            return Response.makeSuccess(qualificationController.getQualification(name));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<List<Qualification>> getQualifications() {
        try
        {
            //checkAuth("ViewQualifications");

            return Response.makeSuccess(qualificationController.getQualifications());
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Qualification> addQualification(@NotNull String name){
        try
        {
            checkAuth("ManageQualifications");

            return Response.makeSuccess(qualificationController.addQualification(name));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

//    public Response<Qualification> renameQualification(@NotNull String name, @NotNull String newName){
//        Response<String> r = checkAuth("ManageQualifications");
//
//        return qualificationController.renameQualification(name, newName);
//    }
    public Response<Qualification> removeQualification(@NotNull String name) {
        try
        {
            checkAuth("ManageQualifications");

            Qualification removedQualification = qualificationController.removeQualification(name);
            List<Employee> employees = employeeController.getEmployees();
            employeeController.removeQualificationForAll(name);
            shiftController.removeQualification(name);
            return Response.makeSuccess(removedQualification);
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<List<Permission>> getPermissions() {
        try
        {
            //checkAuth("ViewQualifications");

            return Response.makeSuccess(qualificationController.getPermissions());
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Permission> addPermission(@NotNull String name){
        try
        {
            checkAuth("ManageQualifications");

            return Response.makeSuccess(qualificationController.addPermission(name));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Permission> removePermission(@NotNull String name)
    {
        try {
            checkAuth("ManageQualifications");

            return Response.makeSuccess(qualificationController.removePermission(name));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Qualification> addPermissionToQualification(String permName, String qualName){
        try
        {
            checkAuth("ManageQualifications");

            return Response.makeSuccess(qualificationController.addPermissionToQualification(permName, qualName));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Qualification> removePermissionFromQualification(String permName, String qualName){
        try
        {
            checkAuth("ManageQualifications");

            return Response.makeSuccess(qualificationController.removePermissionFromQualification(permName, qualName));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    // -----------------------------

    // HR Functions

    public Response<Map<Employee, int[]>> getEmployeesWithQualification(int branchId, Qualification qualification) {
        try
        {
            //checkAuth("ManageBranch" + branchId);

            Map<Employee, int[]> m = new HashMap<>();

            // using this to bypass authentication
            List<Employee> employees = employeeController.getEmployees();

            // init zeros
            for (Employee e : employees)
            {
                if (e.getWorkingConditions().getQualifications().contains(qualification.getName()))
                {
                    int[] a = {0, 0};
                    m.put(e, a);
                }
            }

            Response<List<Shift>> r3 = getShifts(branchId);
            if (!r3.isSuccess())
            {
                return Response.makeFailure(r3.getMessage());
            }

            for (Shift s : r3.getData())
            {
                for (Integer employeeId : s.getWorkers().keySet())
                {
                    Employee employee = employeeController.getEmployee(employeeId);
                    int[] a = m.get(employee);
                    if (s.getId().getShiftTime() == ShiftTime.DAY)
                    {
                        // DAY
                        a[0]++;
                    }
                    else
                    {
                        // NIGHT
                        a[1]++;
                    }
                }
            }

            // Uncomment to remove workers that didn't work yet.
//        Iterator<Employee> i = employees.iterator();
//        while(i.hasNext()) {
//            Employee e = i.next();
//            int[] a = m.get(e);
//            if(a[0] == 0 && a[1] == 0) {
//                m.remove(e);
//                i.remove();
//            }
//        }

            return Response.makeSuccess(m);
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    // -----------------------------
}
