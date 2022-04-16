package BusinessLayer;

import BusinessLayer.*;
import DataAccessLayer.DALController;
import Utilities.Response;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EmployeeController
{
    private List<Employee> employees;

    public List<Employee> getEmployees() {
        return Collections.unmodifiableList(employees);
    }

    public Response<Employee> addEmployee(int id, String name, BankAccountDetails bankAccountDetails, double salary, LocalDateTime workStartingDate, String workingConditionsDescription) {
        // TODO database integration
        for(Employee e : employees)
        {
            if(e.getId() == id)
            {
                return Response.makeFailure("An employee with that id already exists. ");
            }
        }
        WorkingConditions workingConditions = new WorkingConditions(workingConditionsDescription);
        Employee newEmployee = new Employee(id, name, bankAccountDetails, salary, workStartingDate, workingConditions);
        Response<Employee> databaseResponse = DALController.addEmployee(newEmployee);
        if(databaseResponse.isSuccess())
        {
            employees.add(newEmployee);
        }
        return databaseResponse;
    }

    public Response<Employee> removeEmployee(int id) {
        // TODO database integration
        // removing first occurrence
        Employee removedE = null;
        Iterator<Employee> i = employees.iterator();
        while(i.hasNext())
        {
            Employee e = i.next();
            if(e.getId() == id)
            {
                removedE = e;
                break;
            }
        }
        Response<Employee> databaseResponse = DALController.removeEmployee(removedE);
        if(databaseResponse.isSuccess())
        {
            employees.remove(removedE);
        }
        return databaseResponse;
    }

    public Response<Employee> getEmployee(int id) {
        for(Employee e : employees) {
            if(e.getId() == id) {
                return Response.makeSuccess(e);
            }
        }
        return Response.makeFailure("No employee with this id found. ");
    }

    public Response<String> updateEmployeeName(int id, String newName) {
        // TODO IMPLEMENT
    }

    public Response<Double> updateEmployeeSalary(int id, double newSalary) {
        // TODO IMPLEMENT
    }

    public Response<BankAccountDetails> updateEmployeeBankAccountDetails(int id, BankAccountDetails newBankAccountDetails) {
        // TODO IMPLEMENT
    }

    public Response<TimeInterval> employeeAddWorkingHour(int id, LocalDateTime start, LocalDateTime end) {
        // TODO IMPLEMENT
    }

    public Response<TimeInterval> employeeRemoveWorkingHour(int id, LocalDateTime start) {
        // TODO IMPLEMENT
    }

    public Response<Qualification> employeeAddQualification(int id, Qualification qualification) {
        // TODO IMPLEMENT
        // from service layer call the QualificationController and fetch it by name.
        // then call this with that qualification
    }

    public Response<Qualification> employeeRemoveQualification(int id, String name) {
        // TODO IMPLEMENT
    }





    // TODO IMPLEMENT
}
