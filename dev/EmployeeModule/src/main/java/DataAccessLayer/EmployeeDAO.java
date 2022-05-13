package DataAccessLayer;

import BusinessLayer.*;
import Utilities.LegalTimeException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class EmployeeDAO extends DataAccessObject {

    private HashMap<Integer, Employee> employeeCache;

    public EmployeeDAO(String tableName) {
        super(tableName);
        employeeCache = new HashMap<>();
    }

    public boolean Create(Employee employee) {
        String sql = "INSERT INTO Employees(id, name, salary, workStartingDate, workingConditions, qualifications) VALUES(?,?,?,?,?,?)";
        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt   (1, employee.getId());
            pstmt.setString(2, employee.getName());
            pstmt.setDouble(3, employee.getSalary());
            pstmt.setString(4, employee.getWorkStartingDate().toString());
            pstmt.setString(5, employee.getWorkingConditions().getDescription());
            pstmt.setString(6, qualificationsNamesIntoString(employee.getWorkingConditions().getQualifications()));
            if(pstmt.executeUpdate() != 1){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        if(!Create(employee.getId(), employee.getWorkingConditions())){
            return false;
        }
        if(!Create(employee.getBankAccountDetails(), employee.getId())){
            return false;
        }
        employeeCache.put(employee.getId(), employee);
        return true;
    }

    public boolean Create(int employeeId, WorkingConditions workingConditions){
        String sql = "INSERT INTO WorkingHours (employeeId, startingTime, endingTime) VALUES (?, ?, ?)";
        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (TimeInterval timeInterval : workingConditions.getWorkingHours()) {
                pstmt.setInt   (1 , employeeId);
                pstmt.setString(2 , timeInterval.getStart().toString());
                pstmt.setString(3 , timeInterval.getEnd().toString());
                pstmt.addBatch();
            }
            if(pstmt.executeBatch().length != workingConditions.getWorkingHours().size()){
                return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean Create(BankAccountDetails bad, int employeeId) {
        String sql =
                "INSERT INTO BankAccountDetails(employeeId, bankId, branchId, accountId, bankName, branchName, accountOwner)" +
                        " VALUES(?,?,?,?,?,?,?)";
        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            pstmt.setInt(2, bad.bankId());
            pstmt.setInt(3, bad.branchId());
            pstmt.setInt(4, bad.accountId());
            pstmt.setString(5, bad.bankName());
            pstmt.setString(6, bad.branchName());
            pstmt.setString(7, bad.accountOwner());
            if(pstmt.executeUpdate() != 1){
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param employeeId
     * @return Returns the employee with specified id; can return null if the employee doesn't exist in the database
     */
    public Employee Read(Integer employeeId){
        if(employeeCache.containsKey(employeeId)){
            return employeeCache.get(employeeId);
        }
        String sql = "SELECT * FROM Employees WHERE id = (?)";
        Employee employee = null;
        List<TimeInterval> workingHours = ReadWorkingHours(employeeId);
        BankAccountDetails bad = ReadBankAccountDetails(employeeId);
        boolean found = false;
        if(workingHours == null || bad == null){
            return null;
        }
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1 ,employeeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                found = true;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int salary = rs.getInt("salary");
                LocalDateTime workStartingDate = LocalDateTime.parse(rs.getString("workStartingDate"));
                String workingConditionsDesc = rs.getString("workingConditions");
                List<String> qualifications = stringIntoQualificationNames(rs.getString("qualifications"));
                WorkingConditions workingConditions = new WorkingConditions(workingConditionsDesc, workingHours, qualifications);
                employee = new Employee(id, name, bad, salary, workStartingDate, workingConditions);
            }
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        if(!found){
            return null;
        }
        employeeCache.put(employeeId, employee);
        return employee;
    }

    private List<TimeInterval> ReadWorkingHours(int employeeId){
        String sql = "SELECT * FROM WorkingHours WHERE employeeId = (?)";
        ArrayList<TimeInterval> workingHours = new ArrayList<>();
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1 ,employeeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                LocalDateTime start = LocalDateTime.parse(rs.getString("startingTime"));
                LocalDateTime end = LocalDateTime.parse(rs.getString("endingTime"));
                workingHours.add(new TimeInterval(start, end));
            }
            rs.close();
        }
        catch (SQLException | LegalTimeException e) {
            e.printStackTrace();
            return null;
        }
        return workingHours;
    }

    public BankAccountDetails ReadBankAccountDetails(Integer employeeId) {
        String sql = "SELECT * FROM BankAccountDetails WHERE employeeId = (?)";
        boolean found = false;
        BankAccountDetails bad = null;
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1 ,employeeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                found = true;
                int branchId = rs.getInt("branchId");
                int bankId = rs.getInt("bankId");
                int accountId = rs.getInt("accountId");
                String bankName = rs.getString("bankName");
                String branchName = rs.getString("branchName");
                String accountOwner = rs.getString("accountOwner");

                bad = new BankAccountDetails(bankId, branchId, accountId, bankName, branchName, accountOwner);
            }
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        if(!found){
            return null;
        }
        return bad;
    }

    public List<Employee> ReadAllEmployees(){
        List<Integer> eIds = ReadAllEmployeeIds();
        List<Employee> employees = new ArrayList<>();
        for (Integer eId : eIds) {
            employees.add(Read(eId));
        }
        return Collections.unmodifiableList(employees);
    }

    private List<Integer> ReadAllEmployeeIds(){
        String sql = "SELECT id FROM Employees";
        List<Integer> ids = new ArrayList<>();
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt(1));
            }
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public boolean UpdateBankAccountDetails(int employeeId, BankAccountDetails bad){
        String sql = "UPDATE BankAccountDetails SET bankId = (?), branchId = (?), accountOwner = (?), " +
                "branchId = (?), accountId = (?), branchName = (?) WHERE employeeId = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bad.branchId());
            pstmt.setInt(2, bad.accountId());
            pstmt.setInt(3, bad.bankId());
            pstmt.setInt(4, bad.accountId());
            pstmt.setString(5, bad.bankName());
            pstmt.setString(6, bad.accountOwner());
            pstmt.setInt(7, employeeId);
            if(pstmt.executeUpdate() != 1) {
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean UpdateEmployeeName(int employeeId, String name){
        String sql = "UPDATE Employees SET name = (?) WHERE id = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, employeeId);
            if(pstmt.executeUpdate() != 1) {
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean UpdateEmployeeQualifications(int employeeId){
        Employee employee = Read(employeeId);
        List<String> qualifications = employee.getWorkingConditions().getQualifications();
        String qualificationsString = qualificationsNamesIntoString(qualifications);
        String sql = "UPDATE Employees SET qualifications = (?) WHERE id = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, qualificationsString);
            pstmt.setInt(2, employeeId);
            if(pstmt.executeUpdate() != 1) {
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean UpdateEmployeeSalary(int employeeId, Double newSalary){
        Employee employee = Read(employeeId);
        List<String> qualifications = employee.getWorkingConditions().getQualifications();
        String qualificationsString = qualificationsNamesIntoString(qualifications);
        String sql = "UPDATE Employees SET salary = (?) WHERE id = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, newSalary);
            pstmt.setInt(2, employeeId);
            if(pstmt.executeUpdate() != 1) {
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean UpdateWorkingHours(int employeeId, TimeInterval timeInterval){
        String sql = "INSERT INTO WorkingHours(employeeId, startingTime, endingTime) VALUES  (?,?,?)";
        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt   (1 , employeeId);
            pstmt.setString(2 , timeInterval.getStart().toString());
            pstmt.setString(3 , timeInterval.getEnd().toString());
            if(pstmt.executeUpdate() != 1) {
                return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean DeleteWorkingHours(int employeeId, LocalDateTime start){
        String sql = "DELETE FROM WorkingHours WHERE employeeId = (?) AND startingTime = (?)";
        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt   (1 , employeeId);
            pstmt.setString(2 , start.toString());
            if(pstmt.executeUpdate() != 1) {
                return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean DeleteEmployee(int employeeId){
        String sql = "DELETE FROM Employees WHERE id = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
            //if(pstmt.executeUpdate() != 1) {
            //    return false;
            //}
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        employeeCache.remove(employeeId);
        return true;
    }

    private String qualificationsNamesIntoString(List<String> qualifications){
        StringBuilder res = new StringBuilder();
        for (String qualification : qualifications) {
            res.append(qualification).append("; ");
        }
        return res.toString();
    }

    private List<String> stringIntoQualificationNames(String quals){
        return new ArrayList<>(Arrays.asList(quals.split("; ")));
    }


    public void DeleteAll(){
        String sql = "DELETE FROM Employees";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        sql = "DELETE FROM WorkingHours";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        sql = "DELETE FROM BankAccountDetails";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();

        }

    }


}
