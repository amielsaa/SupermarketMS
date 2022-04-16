package DataAccessLayer;

import BusinessLayer.Employee;
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
}
