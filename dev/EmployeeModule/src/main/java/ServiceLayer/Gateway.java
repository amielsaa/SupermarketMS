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

    public void initDefaultData() {
        // TODO DAL make this run once on database init

        // INIT PERMISSIONS AND QUALIFICATIONS

        String[] permissions = {"ViewEmployees", "ManageEmployees", "ViewQualifications", "ManageQualifications", "ManagePermissions", "ManageBranch1", "ManageBranch2"};
        for (String p : permissions)
        {
            qualificationController.addPermission(p);
        }

        String[] permissionsHR = {"ViewEmployees", "ManageEmployees", "ViewQualifications", "ManageQualifications", "ManagePermissions"};


        Qualification qualificationHR = qualificationController.addQualification("HR").getData();
        String[] permissionsAssistant = {"ViewEmployees", "ViewQualifications"};
        for(String p : permissionsHR) {
            Response<Qualification> r = qualificationController.addPermissionToQualification(p, qualificationHR.getName());

        }

        Qualification qualificationBranch1Manager = qualificationController.addQualification("Branch1Manager").getData();
        String[] permissionsBranch1Manager = {"ManageBranch1"};
        for(String p : permissionsBranch1Manager) {
            Response<Qualification> r = qualificationController.addPermissionToQualification(p, qualificationBranch1Manager.getName());


        }

        Qualification qualificationBranch2Manager = qualificationController.addQualification("Branch2Manager").getData();
        String[] permissionsBranch2Manager = {"ManageBranch2"};
        for(String p : permissionsBranch2Manager) {
            Response<Qualification> r = qualificationController.addPermissionToQualification(p, qualificationBranch2Manager.getName());


        }

        Qualification qualificationShiftManager = qualificationController.addQualification("ShiftManager").getData();
        Qualification qualificationCashier = qualificationController.addQualification("Cashier").getData();
        Qualification qualificationWarehouse = qualificationController.addQualification("WarehouseWorker").getData();
        Qualification qualificationStock = qualificationController.addQualification("StockClerk").getData();
        Qualification qualificationTruck = qualificationController.addQualification("TruckDriver").getData();
        Qualification qualificationCleaner = qualificationController.addQualification("Cleaner").getData();
        Qualification qualificationInventoryManager = qualificationController.addQualification("InventoryManager").getData();

        // INIT EMPLOYEES
        BankAccountDetails defaultBankAccountDetails = new BankAccountDetails(0, 0, 0, "", "", "");
        employeeController.addEmployee(ADMIN_UID, "Admin", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(123, "Admin2", defaultBankAccountDetails, 0, LocalDateTime.now(), "");

        employeeController.addEmployee(1, "Bob The Cashier", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(2, "Alice The Warehouse Worker", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(3, "Cat The Stock Clerk", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(4, "Dan The Truck Driver", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(5, "Manny The Manager", defaultBankAccountDetails, 0, LocalDateTime.now(), "");

        employeeController.employeeAddQualification(1, qualificationCashier);
        employeeController.employeeAddQualification(2, qualificationWarehouse);
        employeeController.employeeAddQualification(3, qualificationStock);
        employeeController.employeeAddQualification(4, qualificationTruck);

        employeeController.employeeAddQualification(1, qualificationShiftManager);
        employeeController.employeeAddQualification(2, qualificationShiftManager);
        employeeController.employeeAddQualification(3, qualificationShiftManager);
        employeeController.employeeAddQualification(4, qualificationShiftManager);

        employeeController.employeeAddQualification(5, qualificationBranch2Manager);
        employeeController.employeeAddQualification(6, qualificationShiftManager);



        employeeController.employeeAddQualification(ADMIN_UID, qualificationHR);
        employeeController.employeeAddQualification(123, qualificationHR);
    }

    // TODO change to user-pass authentication
    public Response<Employee> login(int id) {
        Response<Employee> r = employeeController.getEmployee(id);
        if (!r.isSuccess()) {
            return r;
        }

        this.loggedEmployeeId = id;

        return r;
    }
    public Response<Employee> getLoggedUser() {
        if(loggedEmployeeId == -1) {
            return Response.makeFailure("No user is logged in. ");
        }
        return employeeController.getEmployee(loggedEmployeeId);
    }
    public void logout() {
        this.loggedEmployeeId = -1;
    }

    private Response<String> checkAuth(String permission) {
        if(loggedEmployeeId == -1) {
            return Response.makeFailure("Not logged in.");
        }
        Response<Permission> permissionResponse = qualificationController.getPermission(permission);
        if(!permissionResponse.isSuccess()) {
            return Response.makeFailure(permissionResponse.getMessage());
        }
        Response<Permission> r = employeeController.checkPermission(loggedEmployeeId, permissionResponse.getData());
        if(r.isSuccess()) {
            return Response.makeSuccess("Authenticated for this permission. ");
        }
        return Response.makeFailure("Not authenticated for this permission. ");
    }

    // EMPLOYEE FUNCTIONS

    public Response<Employee> getEmployee(int id) {
        Response<String> r = checkAuth("ViewEmployees");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return employeeController.getEmployee(id);
    }

    public Response<List<Employee>> getEmployees() {
        Response<String> r = checkAuth("ViewEmployees");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return Response.makeSuccess(employeeController.getEmployees());
    }

    public Response<Employee> addEmployee(int id, @NotNull String name, @NotNull BankAccountDetails bankAccountDetails,
                                          double salary, @NotNull LocalDateTime workStartingDate, @NotNull String workingConditionsDescription) {
        Response<String> r = checkAuth("ManageEmployees");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return employeeController.addEmployee(id, name, bankAccountDetails, salary, workStartingDate, workingConditionsDescription);
    }

    public Response<Employee> removeEmployee(int id) {
        Response<String> r = checkAuth("ManageEmployees");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return employeeController.removeEmployee(id);
    }



    public Response<String> updateEmployeeName(int id, @NotNull String newName) {
        // added so each employee can change their own name
        if(loggedEmployeeId == id) {
            // all good.
        }
        else {
            Response<String> r = checkAuth("ManageEmployees");
            if(!r.isSuccess()) {
                return Response.makeFailure(r.getMessage());
            }
        }

        return employeeController.updateEmployeeName(id, newName);
    }

    public Response<Double> updateEmployeeSalary(int id, double newSalary) {
        Response<String> r = checkAuth("ManageEmployees");
        if(!r.isSuccess())
        {
            return Response.makeFailure(r.getMessage());
        }

        return employeeController.updateEmployeeSalary(id, newSalary);
    }

    public Response<BankAccountDetails> updateEmployeeBankAccountDetails(int id, @NotNull BankAccountDetails newBankAccountDetails) {
        Response<String> r = checkAuth("ManageEmployees");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return employeeController.updateEmployeeBankAccountDetails(id, newBankAccountDetails);
    }

    public Response<TimeInterval> employeeAddWorkingHour(int id, @NotNull LocalDateTime start, @NotNull LocalDateTime end)
    {
        // added so each employee can add their hours
        if(loggedEmployeeId == id) {
            // all good.
        }
        else {
            Response<String> r = checkAuth("ManageEmployees");
            if(!r.isSuccess()) {
                return Response.makeFailure(r.getMessage());
            }
        }

        return employeeController.employeeAddWorkingHour(id, start, end);
    }

    public Response<TimeInterval> employeeRemoveWorkingHour(int id, @NotNull LocalDateTime start) {
        // added so each employee can remove their hours
        if(loggedEmployeeId == id) {
            // all good.
        }
        else {
            Response<String> r = checkAuth("ManageEmployees");
            if(!r.isSuccess()) {
                return Response.makeFailure(r.getMessage());
            }
        }

        return employeeController.employeeRemoveWorkingHour(id, start);
    }

    public Response<Qualification> employeeAddQualification(int id, Qualification qualification) {
        Response<String> r = checkAuth("ManageEmployees");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return employeeController.employeeAddQualification(id, qualification);
    }

    public Response<Qualification> employeeRemoveQualification(int id, @NotNull String name) {
        Response<String> r = checkAuth("ManageEmployees");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return employeeController.employeeRemoveQualification(id, name);
    }

    // -----------------------------

    // SHIFTS FUNCTIONS

    public Response<List<Shift>> getShifts(int branchId) {
        Response<String> r = checkAuth("ManageBranch" + branchId);
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage() + "Not your branch. ");
        }

        return shiftController.getShifts(branchId);
    }

    public Response<Shift> addShift(int branchId, @NotNull LocalDateTime date, @NotNull Employee shiftManager,
                                    @NotNull Map<Employee, List<Qualification>> workers, @NotNull ShiftTime shiftTime) {
        Response<String> r = checkAuth("ManageBranch" + branchId);
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage() + "Not your branch. ");
        }

        return shiftController.addShift(branchId, date, shiftManager, workers, shiftTime);
    }

    public Response<Shift> removeShift(@NotNull ShiftId shiftId){
        Response<String> r = checkAuth("ManageBranch" + shiftId.getBranchId());
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage() + "Not your branch. ");
        }

        return shiftController.removeShift(shiftId);
    }

    public Response<Employee> addWorker(@NotNull ShiftId shiftId, @NotNull Employee worker, @NotNull List<Qualification> qualifications){
        Response<String> r = checkAuth("ManageBranch" + shiftId.getBranchId());
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage() + "Not your branch. ");
        }

        return shiftController.addWorker(shiftId, worker, qualifications);
    }

    public Response<Employee> removeWorker(@NotNull ShiftId shiftId, @NotNull Employee worker){
        Response<String> r = checkAuth("ManageBranch" + shiftId.getBranchId());
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage() + "Not your branch. ");
        }

        return shiftController.removeWorker(shiftId, worker);
    }

    // -----------------------------

    // SHIFTS FUNCTIONS

    public Response<Qualification> getQualification(String name) {
        Response<String> r = checkAuth("ViewQualifications");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return qualificationController.getQualification(name);
    }

    public Response<List<Qualification>> getQualifications() {
        Response<String> r = checkAuth("ViewQualifications");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return qualificationController.getQualifications();
    }

    public Response<Qualification> addQualification(@NotNull String name){
        Response<String> r = checkAuth("ManageQualifications");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return qualificationController.addQualification(name);
    }

//    public Response<Qualification> renameQualification(@NotNull String name, @NotNull String newName){
//        Response<String> r = checkAuth("ManageQualifications");
//        if(!r.isSuccess()) {
//            return Response.makeFailure(r.getMessage());
//        }
//
//        return qualificationController.renameQualification(name, newName);
//    }
    public Response<Qualification> removeQualification(@NotNull String name) {
        Response<String> r = checkAuth("ManageQualifications");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        Response<Qualification> r2 = qualificationController.removeQualification(name);
        if(!r2.isSuccess()) {
            return r2;
        }
        Qualification removedQualification = r2.getData();
        List<Employee> employees = employeeController.getEmployees();
        for(Employee e : employees)
        {
            employeeController.employeeRemoveQualification(e.getId(), removedQualification.getName());
        }
        return Response.makeSuccess(r2.getData());
    }

    public Response<List<Permission>> getPermissions() {
        Response<String> r = checkAuth("ViewQualifications");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return qualificationController.getPermissions();
    }

    public Response<Permission> addPermission(@NotNull String name){
        Response<String> r = checkAuth("ManagePermissions");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return qualificationController.addPermission(name);
    }

    public Response<Permission> removePermission(@NotNull String name) {
        Response<String> r = checkAuth("ManagePermissions");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return qualificationController.removePermission(name);
    }

    public Response<Qualification> addPermissionToQualification(String permName, String qualName){
        Response<String> r = checkAuth("ManageQualifications");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return qualificationController.addPermissionToQualification(permName, qualName);
    }

    public Response<Qualification> removePermissionFromQualification(String permName, String qualName){
        Response<String> r = checkAuth("ManageQualifications");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return qualificationController.removePermissionFromQualification(permName, qualName);
    }

    // -----------------------------

    // HR Functions

    public Response<Map<Employee, int[]>> getEmployeesWithQualification(int branchId, Qualification qualification) {
        Response<String> r = checkAuth("ManageBranch" + branchId);
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage() + "Not your branch. ");
        }

        Map<Employee, int[]> m = new HashMap<>();

        // using this to bypass authentication
        List<Employee> employees = employeeController.getEmployees();

        // init zeros
        for(Employee e : employees) {
            int[] a = {0, 0};
            m.put(e, a);
        }

        Response<List<Shift>> r3 = getShifts(branchId);
        if(!r3.isSuccess()) {
            return Response.makeFailure(r3.getMessage());
        }

        for(Shift s : r3.getData())
        {
            for(Employee e : s.getWorkers().keySet()) {
                int[] a = m.get(e);
                if(s.getId().getShiftTime() == ShiftTime.DAY) {
                    // DAY
                    a[0]++;
                } else {
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
    }

    // -----------------------------
}
