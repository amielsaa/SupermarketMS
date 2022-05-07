package BusinessLayer;

import DataAccessLayer.DALController;
import Utilities.ObjectAlreadyExistsException;
import Utilities.ObjectNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

public class EmployeeController
{
    private List<Employee> employees;

    public EmployeeController(DALController dalController) {
        dalController.execute();
        employees = new ArrayList<Employee>();
    }

    public List<Employee> getEmployees() {
        return Collections.unmodifiableList(employees);
    }

    public Permission checkPermission(int id, Permission permission) throws Exception
    {
        Employee e = findEmployeeById(id);
        if(e == null) {
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        List<Qualification> qList = e.getWorkingConditions().getQualifications();
        for (Qualification q : qList)
        {
            if(q.hasPermission(permission)) {
                return permission;
            }
        }
        throw new ObjectNotFoundException("Employee does not have the permission. ");
    }

    public Employee addEmployee(int id, String name, BankAccountDetails bankAccountDetails, double salary, LocalDateTime workStartingDate, String workingConditionsDescription) throws Exception {
        // TODO database integration
        for(Employee e : employees)
        {
            if(e.getId() == id)
            {
                throw new ObjectAlreadyExistsException("An employee with that id already exists. ");
            }
        }
        WorkingConditions workingConditions = new WorkingConditions(workingConditionsDescription);
        Employee newEmployee = new Employee(id, name, bankAccountDetails, salary, workStartingDate, workingConditions);
        Employee e = DALController.addEmployee(newEmployee);
        employees.add(e);
        return e;
    }

    public Employee removeEmployee(int id) throws Exception{
        // TODO database integration
        // removing first occurrence
        Employee removedE = findEmployeeById(id);
        if(removedE == null) {
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        Employee e = DALController.removeEmployee(removedE);
        employees.remove(removedE);
        return e;
    }

    public Employee getEmployee(int id) throws Exception{
        for(Employee e : employees) {
            if(e.getId() == id) {
                return e;
            }
        }
        throw new ObjectNotFoundException("No employee with this id found. ");
    }

    public String updateEmployeeName(int id, String newName) throws Exception {
        // TODO database integration
        // update first occurrence
        Employee updatedE = findEmployeeById(id);
        if(updatedE == null) {
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        String s = DALController.employeeUpdateName(updatedE, newName);
        updatedE.setName(s);
        return s;
    }

    public Double updateEmployeeSalary(int id, double newSalary) throws Exception {
        // TODO database integration
        // update first occurrence
        Employee updatedE = findEmployeeById(id);
        if(updatedE == null) {
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        Double d = DALController.employeeUpdateSalary(updatedE, newSalary);
        updatedE.setSalary(d);
        return d;
    }

    public BankAccountDetails updateEmployeeBankAccountDetails(int id, BankAccountDetails newBankAccountDetails) throws Exception {
        // TODO database integration
        // update first occurrence
        Employee updatedE = findEmployeeById(id);
        if(updatedE == null) {
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        BankAccountDetails b = DALController.employeeUpdateBankAccountDetails(updatedE, newBankAccountDetails);
        updatedE.setBankAccountDetails(b);
        return b;
    }

    public TimeInterval employeeAddWorkingHour(int id, LocalDateTime start, LocalDateTime end) throws Exception
    {
        // TODO database integration
        // update first occurrence
        Employee updatedE = findEmployeeById(id);

        if(updatedE == null) {
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        TimeInterval newWorkingHour = updatedE.getWorkingConditions().addWorkingHour(start, end);
        TimeInterval ti = DALController.employeeAddWorkingHour(updatedE, newWorkingHour);
        return ti;
    }

    public TimeInterval employeeRemoveWorkingHour(int id, LocalDateTime start) throws Exception {
        // TODO database integration
        // update first occurrence
        Employee updatedE = findEmployeeById(id);

        if(updatedE == null) {
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        TimeInterval removedWorkingHour = updatedE.getWorkingConditions().removeWorkingHour(start);
        TimeInterval ti = DALController.employeeRemoveWorkingHour(updatedE, removedWorkingHour);
        return ti;
    }

    public Qualification employeeAddQualification(int id, Qualification qualification) throws Exception {
        // from service layer call the QualificationController and fetch it by name.
        // then call this with that qualification
        // TODO database integration
        // update first occurrence
        Employee updatedE = findEmployeeById(id);

        if(updatedE == null) {
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        // Check if employee already has that qualification
        try {
            Qualification q = updatedE.getWorkingConditions().getQualification(qualification.getName());
            throw new ObjectAlreadyExistsException("Employee already has a qualification with that name. ");
        } catch (Exception e){
            // all good
        }

        Qualification newQualification = updatedE.getWorkingConditions().addQualification(qualification);

        Qualification q = DALController.employeeAddQualification(updatedE, newQualification);
        return q;
    }

    public Qualification employeeRemoveQualification(int id, String name) throws Exception {
        // TODO database integration
        // update first occurrence
        Employee updatedE = findEmployeeById(id);

        if(updatedE == null) {
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        Qualification removedQualification = updatedE.getWorkingConditions().removeQualification(name);
        Qualification q = DALController.employeeRemoveQualification(updatedE, removedQualification);
        return q;
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
