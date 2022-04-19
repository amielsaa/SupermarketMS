package ServiceLayer;

import BusinessLayer.*;
import DataAccessLayer.DALController;
import Utilities.Response;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

public class Gateway
{
    private int loggedEmployeeId;
    private EmployeeController employeeController;
    private ShiftController shiftController;
    private QualificationController qualificationController;
    private DALController dalController;

    public Gateway() {
        this.loggedEmployeeId = -1;

        this.dalController = new DALController();
        this.employeeController = new EmployeeController(dalController);
        this.shiftController = new ShiftController(dalController);
        this.qualificationController = new QualificationController(dalController);

        // TODO DAL make this run once on database init
        // init default data
        String[] permissions = {"ViewEmployees", "ManageEmployees", "ManageBranch1", "ManageBranch2", "ManageQualifications", "ManagePermissions" };
        for (String p : permissions)
        {
            qualificationController.addPermission(p);
        }

        // TODO init HR

    }
    // TODO change to user-pass authentication
    private Response<Employee> login(int id) {
        Response<Employee> r = employeeController.getEmployee(id);
        if (!r.isSuccess()) {
            return r;
        }

        this.loggedEmployeeId = id;

        return r;
    }

    private void logout() {
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

    public Response<Shift> addShift(int branchId, @NotNull LocalDateTime date, @NotNull Employee shiftManager,
                                    @NotNull Map<Employee, List<Qualification>> workers, @NotNull ShiftTime shiftTime) {
        Response<String> r = checkAuth("ManageBranch" + branchId);
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return shiftController.addShift(branchId, date, shiftManager, workers, shiftTime);
    }

    public Response<Shift> removeShift(@NotNull ShiftId shiftId){
        Response<String> r = checkAuth("ManageBranch" + shiftId.getBranchId());
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return shiftController.removeShift(shiftId);
    }

    public Response<Employee> addWorker(@NotNull ShiftId shiftId, @NotNull Employee worker, @NotNull List<Qualification> qualifications){
        Response<String> r = checkAuth("ManageBranch" + shiftId.getBranchId());
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return shiftController.addWorker(shiftId, worker, qualifications);
    }

    public Response<Employee> removeWorker(@NotNull ShiftId shiftId, @NotNull Employee worker){
        Response<String> r = checkAuth("ManageBranch" + shiftId.getBranchId());
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return shiftController.removeWorker(shiftId, worker);
    }

    // -----------------------------

    // SHIFTS FUNCTIONS

    public Response<List<Qualification>> getQualifications() {
        Response<String> r = checkAuth("ManageQualifications");
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

    public Response<Qualification> renameQualification(@NotNull String name, @NotNull String newName){
        Response<String> r = checkAuth("ManageQualifications");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return qualificationController.renameQualification(name, newName);
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


    // TODO this ^^^ for all functions in BusinessLayer.

}
