package ServiceLayer;

import BusinessLayer.*;
import DataAccessLayer.DALController;
import Utilities.Response;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class Gateway
{
    private int loggedEmployeeId;
    private EmployeeController employeeController;
    private ShiftController shiftController;
    private QualificationController qualificationController;
    private DALController dalController;

    public Gateway(int loginEmployeeId) {
        this.loggedEmployeeId = loginEmployeeId;

        this.dalController = new DALController();
        this.employeeController = new EmployeeController(dalController);
        this.shiftController = new ShiftController(dalController);
        this.qualificationController = new QualificationController(dalController);

        // init default data
        String[] permissions = {"ManageEmployees", ""};
        for (String p : permissions)
        {
            qualificationController.addPermission(p);
        }



    }

    private Response<String> checkAuth(String permission) {
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

    public Response<Employee> addEmployee(int id, @NotNull String name, @NotNull BankAccountDetails bankAccountDetails,
                                          double salary, @NotNull LocalDateTime workStartingDate, @NotNull String workingConditionsDescription) {
        Response<String> r = checkAuth("ManageEmployees");
        if(!r.isSuccess()) {
            return Response.makeFailure(r.getMessage());
        }

        return employeeController.addEmployee(id, name, bankAccountDetails, salary, workStartingDate, workingConditionsDescription);
    }
    // TODO this ^^^ for all functions in BusinessLayer.
    
}
