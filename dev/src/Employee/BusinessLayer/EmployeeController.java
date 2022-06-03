package Employee.BusinessLayer;

import Employee.DataAccessLayer.EmployeeDAO;
import Utilities.Exceptions.DatabaseAccessException;
import Utilities.Exceptions.ObjectAlreadyExistsException;
import Utilities.Exceptions.ObjectNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

public class EmployeeController
{
    private List<Employee> employees;
    private EmployeeDAO eDao;

    public EmployeeController() {
        eDao = new EmployeeDAO("Employees");
        employees = new ArrayList<Employee>();
    }

    public void clearCache(){
        eDao.clearCache();
    }

    public List<Employee> getEmployees() throws DatabaseAccessException {
        List<Employee> employeeList = eDao.ReadAllEmployees();
        if(employeeList == null){
            throw new DatabaseAccessException("Failed to load all employees from database");
        }
        return Collections.unmodifiableList(employeeList);
    }
    public List<Employee> getEmployeesWithQualification(String qualName) throws DatabaseAccessException {
        List<Employee> employees = getEmployees();
        Iterator<Employee> i = employees.iterator();
        while(i.hasNext()) {
            Employee e = i.next();
            if(!e.getWorkingConditions().hasQualification(qualName)) {
                i.remove();
            }
        }
        return employees;
    }
    //TODO: make a method for checking if a qualification grants this permission in qualification controller
    public Permission checkPermission(int id, Permission permission) throws Exception
    {   /*
        Employee e = eDao.Read(id);
        if(e == null) {
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        for (String qualification : e.getWorkingConditions().getQualifications()) {

        }
        */
        /*
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
        */
         return null;
    }

    public Employee addEmployee(int id, String name, BankAccountDetails bankAccountDetails,
                                double salary, LocalDateTime workStartingDate, String workingConditionsDescription) throws Exception {
        Employee exists = eDao.Read(id);
        if(exists != null){
            throw new ObjectAlreadyExistsException("An employee with this id already exists");
        }
        WorkingConditions workingConditions = new WorkingConditions(workingConditionsDescription);
        Employee newEmployee = new Employee(id, name, bankAccountDetails, salary, workStartingDate, workingConditions);
        if(!eDao.Create(newEmployee)){
            throw new DatabaseAccessException("Failed to add an employee into the database");
        }
        return newEmployee;
    }

    public Employee removeEmployee(int id) throws Exception{
        Employee removedE = eDao.Read(id);
        if(removedE == null) {
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        if(!eDao.DeleteEmployee(id)){
            throw new DatabaseAccessException("Failed to remove an employee from the database");
        }
        return removedE;
    }

    public Employee getEmployee(int id) throws Exception{
        Employee employee = eDao.Read(id);
        if(employee == null){
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        return employee;
    }

    public String updateEmployeeName(int id, String newName) throws Exception {
        Employee employee = eDao.Read(id);
        if(employee == null){
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        if(!eDao.UpdateEmployeeName(id, newName)){
            throw new DatabaseAccessException("Failed to update an employee name in the database");
        }
        employee.setName(newName);
        return newName;
    }

    public Double updateEmployeeSalary(int id, double newSalary) throws Exception {
        Employee employee = eDao.Read(id);
        if(employee == null){
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        if(!eDao.UpdateEmployeeSalary(id, newSalary)){
            throw new DatabaseAccessException("Failed to update an employee salary in the database");
        }
        employee.setSalary(newSalary);
        return newSalary;
    }

    public BankAccountDetails updateEmployeeBankAccountDetails(int id, BankAccountDetails newBankAccountDetails) throws Exception {
        Employee employee = eDao.Read(id);
        if(employee == null){
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        if(!eDao.UpdateBankAccountDetails(id, newBankAccountDetails)){
            throw new DatabaseAccessException("Failed to update an employee bank details in the database");
        }
        employee.setBankAccountDetails(newBankAccountDetails);
        return newBankAccountDetails;
    }

    public TimeInterval employeeAddWorkingHour(int id, LocalDateTime start, LocalDateTime end) throws Exception
    {
        TimeInterval toAdd = new TimeInterval(start, end);
        Employee employee = eDao.Read(id);
        if(employee == null){
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        employee.getWorkingConditions().addWorkingHour(start, end);
        if(!eDao.UpdateWorkingHours(id, toAdd)){
            employee.getWorkingConditions().removeWorkingHour(toAdd.getStart());
            throw new DatabaseAccessException("Failed to update an employee working hours in the database");
        }
        return toAdd;
    }

    public TimeInterval employeeRemoveWorkingHour(int id, LocalDateTime start) throws Exception {
        Employee employee = eDao.Read(id);
        if(employee == null){
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        if(!eDao.DeleteWorkingHours(id, start)){
            throw new DatabaseAccessException("Failed to update an employee working hours in the database");
        }
        TimeInterval ti = employee.getWorkingConditions().removeWorkingHour(start);
        return ti;
    }

    public Qualification employeeAddQualification(int id, Qualification qualification) throws Exception {
        Employee employee = eDao.Read(id);
        if(employee == null){
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        employee.getWorkingConditions().addQualification(qualification.getName());
        if(!eDao.UpdateEmployeeQualifications(id)){
            employee.getWorkingConditions().removeQualification(qualification.getName());
            throw new DatabaseAccessException("Failed to update qualifications list");
        }
        return qualification;

        /*
        // from service layer call the QualificationController and fetch it by name.
        // then call this with that qualification
        // TODO database integration
        // update first occurrence
        Employee updatedE = findEmployeeById(id);

        if(updatedE == null) {
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        // Check if employee already has that qualification
        if(updatedE.getWorkingConditions().hasQualification(qualification.getName())){
            throw new ObjectAlreadyExistsException("Employee already has a qualification with that name. ");
        }
        /*
        try {
            String q = updatedE.getWorkingConditions().getQualification(qualification.getName());
            throw new ObjectAlreadyExistsException("Employee already has a qualification with that name. ");
        } catch (Exception e){
            // all good
        }



        Boolean newQualification = updatedE.getWorkingConditions().addQualification(qualification.getName());

        //Qualification q = DALController.employeeAddQualification(updatedE, newQualification);
        return null;
        */
    }

    public String employeeRemoveQualification(int id, String qualName) throws Exception {
        Employee employee = eDao.Read(id);
        if(employee == null){
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        employee.getWorkingConditions().removeQualification(qualName);
        if(!eDao.UpdateEmployeeQualifications(id)){
            employee.getWorkingConditions().addQualification(qualName);
            throw new DatabaseAccessException("Failed to update qualifications list");
        }
        return qualName;
    }

    public String removeQualificationForAll(String name) throws Exception {
        List<Employee> employeeList = eDao.ReadAllEmployees();
        for (Employee employee : employeeList) {
            employeeRemoveQualification(employee.getId(), name);
            }
        return name;
    }

    public void clearDatabases(){
        eDao.DeleteAll();
    }



        /*
        // update first occurrence
        Employee updatedE = findEmployeeById(id);

        if(updatedE == null) {
            throw new ObjectNotFoundException("No employee with this id found. ");
        }
        Boolean removedQualification = updatedE.getWorkingConditions().removeQualification(name);
        //Qualification q = DALController.employeeRemoveQualification(updatedE, removedQualification);
        return null;
                 */

    /*
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
    */

}
