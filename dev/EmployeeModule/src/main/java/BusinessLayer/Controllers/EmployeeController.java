package BusinessLayer.Controllers;

import BusinessLayer.Objects.BankAccountDetails;
import BusinessLayer.Objects.Employee;
import Utilities.Response;

import java.time.LocalDate;
import java.util.List;

public class EmployeeController
{
    private List<Employee> employees;

    public Response<Employee> addEmployee(int id, String name, BankAccountDetails bankAccountDetails, double salary, LocalDate workStartingDate, String workingConditionsDescription) {
        // TODO IMPLEMENT
    }

    public Response<Employee> removeEmployee(int id) {
        // TODO IMPLEMENT
    }

    public Response<Employee> getEmployee(int id) {
        
    }

    // TODO IMPLEMENT
}
