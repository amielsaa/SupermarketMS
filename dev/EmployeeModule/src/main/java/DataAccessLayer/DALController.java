package DataAccessLayer;

import BusinessLayer.BankAccountDetails;
import BusinessLayer.Employee;
import BusinessLayer.Qualification;
import BusinessLayer.TimeInterval;
import Utilities.Response;

public class DALController
{
    // TODO IMPLEMENT
    public static void execute()
    {
        // this is a placeholder method, call this when you need to access database.
        System.out.println("Executing database command.  ");
        // TODO IMPLEMENT THIS
    }
    public static Response<Employee> addEmployee(Employee employee)
    {
        // TODO IMPLEMENT
        return Response.makeSuccess(employee);
    }
    public static Response<Employee> removeEmployee(Employee employee) {
        // TODO IMPLEMENT
        return Response.makeSuccess(employee);
    }

    public static Response<String> employeeUpdateName(Employee employee, String newName)
    {
        // TODO IMPLEMENT
        return Response.makeSuccess(newName);
    }

    public static Response<Double> employeeUpdateSalary(Employee employee, double newSalary)
    {
        // TODO IMPLEMENT
        return Response.makeSuccess(newSalary);
    }

    public static Response<BankAccountDetails> employeeUpdateBankAccountDetails(Employee employee, BankAccountDetails newBankAccountDetails)
    {
        // TODO IMPLEMENT
        return Response.makeSuccess(newBankAccountDetails);
    }

    public static Response<TimeInterval> employeeAddWorkingHour(Employee employee, TimeInterval newWorkingHour)
    {
        // TODO IMPLEMENT
        return Response.makeSuccess(newWorkingHour);
    }

    public static Response<TimeInterval> employeeRemoveWorkingHour(Employee employee, TimeInterval workingHour)
    {
        // TODO IMPLEMENT
        return Response.makeSuccess(workingHour);
    }

    public static Response<Qualification> employeeAddQualification(Employee employee, Qualification newQualification)
    {
        // TODO IMPLEMENT
        return Response.makeSuccess(newQualification);
    }

    public static Response<Qualification> employeeRemoveQualification(Employee updatedE, Qualification qualification)
    {
        // TODO IMPLEMENT
        return Response.makeSuccess(qualification);
    }
}
