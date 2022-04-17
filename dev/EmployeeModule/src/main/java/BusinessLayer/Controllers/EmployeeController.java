package BusinessLayer.Controllers;

import BusinessLayer.Objects.*;
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
        Employee removedE = findEmployeeById(id);
        if(removedE == null) {
            return Response.makeFailure("No employee with this id found. ");
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
        // TODO database integration
        // update first occurrence
        Employee updatedE = findEmployeeById(id);
        if(updatedE == null) {
            return Response.makeFailure("No employee with this id found. ");
        }
        Response<String> databaseResponse = DALController.employeeUpdateName(updatedE, newName);
        if(databaseResponse.isSuccess())
        {
            updatedE.setName(databaseResponse.getData());
        }
        return databaseResponse;
    }

    public Response<Double> updateEmployeeSalary(int id, double newSalary) {
        // TODO database integration
        // update first occurrence
        Employee updatedE = findEmployeeById(id);
        if(updatedE == null) {
            return Response.makeFailure("No employee with this id found. ");
        }
        Response<Double> databaseResponse = DALController.employeeUpdateSalary(updatedE, newSalary);
        if(databaseResponse.isSuccess())
        {
            updatedE.setSalary(databaseResponse.getData());
        }
        return databaseResponse;
    }

    public Response<BankAccountDetails> updateEmployeeBankAccountDetails(int id, BankAccountDetails newBankAccountDetails) {
        // TODO database integration
        // update first occurrence
        Employee updatedE = findEmployeeById(id);
        if(updatedE == null) {
            return Response.makeFailure("No employee with this id found. ");
        }
        Response<BankAccountDetails> databaseResponse = DALController.employeeUpdateBankAccountDetails(updatedE, newBankAccountDetails);
        if(databaseResponse.isSuccess())
        {
            updatedE.setBankAccountDetails(databaseResponse.getData());
        }
        return databaseResponse;
    }

    public Response<TimeInterval> employeeAddWorkingHour(int id, LocalDateTime start, LocalDateTime end) {
        // TODO database integration
        // update first occurrence
        Employee updatedE = findEmployeeById(id);

        if(updatedE == null) {
            return Response.makeFailure("No employee with this id found. ");
        }
        Response<TimeInterval> newWorkingHour = updatedE.getWorkingConditions().addWorkingHour(start, end);
        if(!newWorkingHour.isSuccess()) {
            return newWorkingHour;
        }
        Response<TimeInterval> databaseResponse = DALController.employeeAddWorkingHour(updatedE, newWorkingHour.getData());
        return databaseResponse;
    }

    public Response<TimeInterval> employeeRemoveWorkingHour(int id, LocalDateTime start) {
        // TODO database integration
        // update first occurrence
        Employee updatedE = findEmployeeById(id);

        if(updatedE == null) {
            return Response.makeFailure("No employee with this id found. ");
        }
        Response<TimeInterval> removedWorkingHour = updatedE.getWorkingConditions().removeWorkingHour(start);
        if(!removedWorkingHour.isSuccess()) {
            return removedWorkingHour;
        }
        Response<TimeInterval> databaseResponse = DALController.employeeRemoveWorkingHour(updatedE, removedWorkingHour.getData());
        return databaseResponse;
    }

    public Response<Qualification> employeeAddQualification(int id, Qualification qualification) {
        // from service layer call the QualificationController and fetch it by name.
        // then call this with that qualification
        // TODO database integration
        // update first occurrence
        Employee updatedE = findEmployeeById(id);

        if(updatedE == null) {
            return Response.makeFailure("No employee with this id found. ");
        }
        Response<Qualification> newQualification = updatedE.getWorkingConditions().addQualification(qualification);
        if(!newQualification.isSuccess()) {
            return newQualification;
        }
        Response<Qualification> databaseResponse = DALController.employeeAddQualification(updatedE, newQualification.getData());
        return databaseResponse;
    }

    public Response<Qualification> employeeRemoveQualification(int id, String name) {
        // TODO database integration
        // update first occurrence
        Employee updatedE = findEmployeeById(id);

        if(updatedE == null) {
            return Response.makeFailure("No employee with this id found. ");
        }
        Response<Qualification> removedQualification = updatedE.getWorkingConditions().removeQualification(name);
        if(!removedQualification.isSuccess()) {
            return removedQualification;
        }
        Response<Qualification> databaseResponse = DALController.employeeRemoveQualification(updatedE, removedQualification.getData());
        return databaseResponse;
    }

    private Employee findEmployeeById(int id) {
        Iterator<Employee> i = employees.iterator();
        while(i.hasNext())
        {
            Employee e = i.next();
            if(e.getId() == id)
            {
                return e;
            }
        }
        // if not found return null
        return null;
    }
}
