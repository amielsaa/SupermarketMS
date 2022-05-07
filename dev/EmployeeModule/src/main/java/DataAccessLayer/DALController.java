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
        // TODO IMPLEMENT THIS
    }
    public static Employee addEmployee(Employee employee) throws Exception
    {
        // TODO IMPLEMENT
        return employee;
    }
    public static Employee removeEmployee(Employee employee) throws Exception
    {
        // TODO IMPLEMENT
        return employee;
    }

    public static String employeeUpdateName(Employee employee, String newName) throws Exception
    {
        // TODO IMPLEMENT
        return newName;
    }

    public static Double employeeUpdateSalary(Employee employee, double newSalary) throws Exception
    {
        // TODO IMPLEMENT
        return newSalary;
    }

    public static BankAccountDetails employeeUpdateBankAccountDetails(Employee employee, BankAccountDetails newBankAccountDetails) throws Exception
    {
        // TODO IMPLEMENT
        return newBankAccountDetails;
    }

    public static TimeInterval employeeAddWorkingHour(Employee employee, TimeInterval newWorkingHour) throws Exception
    {
        // TODO IMPLEMENT
        return newWorkingHour;
    }

    public static TimeInterval employeeRemoveWorkingHour(Employee employee, TimeInterval workingHour) throws Exception
    {
        // TODO IMPLEMENT
        return workingHour;
    }

    public static Qualification employeeAddQualification(Employee employee, Qualification newQualification) throws Exception
    {
        // TODO IMPLEMENT
        return newQualification;
    }

    public static Qualification employeeRemoveQualification(Employee updatedE, Qualification qualification) throws Exception
    {
        // TODO IMPLEMENT
        return qualification;
    }
}
