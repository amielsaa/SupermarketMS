package EmployeeModule.DataAccessLayer;

import EmployeeModule.BusinessLayer.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftDAO extends DataAccessObject {

    private HashMap<ShiftId, Shift> shiftsCache;

    public ShiftDAO(String tableName) {
        super(tableName);
        shiftsCache = new HashMap<>();
    }

    public boolean Create(Shift shift){
        String sql = "INSERT INTO Shifts(branchId, date, time, employeeId, job) VALUES(?,?,?,?,?)";
        try{
            Connection conn = this.makeConnection();
            int counter = 0;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (Integer employeeId : shift.getWorkers().keySet()) {
                counter+=shift.getWorkers().get(employeeId).size();
                for (String job : shift.getWorkers().get(employeeId)) {
                    pstmt.setInt   (1 , shift.getId().getBranchId());
                    pstmt.setString(2 , shift.getId().getDate().toString());
                    pstmt.setString(3 , shift.getId().getShiftTime().name());
                    pstmt.setInt   (4 , employeeId);
                    pstmt.setString(5 , job);
                    pstmt.addBatch();
                }
            }
            if(pstmt.executeBatch().length != counter){
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
        shiftsCache.put(shift.getId(), shift);
        return true;
    }

    public Shift Read(ShiftId shiftId){
        if(shiftsCache.containsKey(shiftId)){
            return shiftsCache.get(shiftId);
        }
        String sql = "SELECT * FROM Shifts WHERE branchId = (?) AND date = (?) AND  time = (?)";
        Map<Integer, List<String>> workers = new HashMap<>();
        Integer managerId = -1;
        boolean found = false;
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1 ,shiftId.getBranchId());
            pstmt.setString(2 ,shiftId.getDate().toString());
            pstmt.setString(3 ,shiftId.getShiftTime().name());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                found = true;
                int employeeId = rs.getInt("employeeId");
                if(workers.containsKey(employeeId)){
                    workers.put(employeeId, new ArrayList<>());
                }
                String job = rs.getString("job");
                if(job.equals("ShiftManager")){
                    managerId = employeeId;
                }
                else{
                    workers.get(employeeId).add(job);
                }
            }
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        if(!found){
            return null;
        }
        if(workers.get(managerId).isEmpty()){
            workers.remove(managerId);
        }
        Shift shift = new Shift(shiftId, workers, managerId);
        shiftsCache.put(shiftId, shift);
        return shift;
    }

    public List<Shift> Read(int branchId){
        List<Shift> shifts = new ArrayList<>();
        List<ShiftId> shiftIds = getAllShiftIdsFromBranch(branchId);
        for (ShiftId shiftId : shiftIds) {
            Shift toAdd = Read(shiftId);
            if(toAdd == null){
                return null;
            }
            shifts.add(toAdd);
        }
        return shifts;
    }

    public List<Shift> ReadAll(){
        List<ShiftId> shiftIds = getAllShiftIds();
        List<Shift> shifts = new ArrayList<>();
        for (ShiftId shiftId : shiftIds) {
            Shift toAdd = Read(shiftId);
            if(toAdd == null){
                return null;
            }
            shifts.add(toAdd);
        }
        return shifts;
    }

    private List<ShiftId> getAllShiftIds(){
        String sql = "SELECT DISTINCT (branchId, date, time) FROM Shifts";
        List<ShiftId> shiftIds = new ArrayList<>();
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int branchId = rs.getInt("branchId");
                LocalDateTime date = LocalDateTime.parse(rs.getString("date"));
                ShiftTime time = ShiftTime.valueOf(rs.getString("time"));
                shiftIds.add(new ShiftId(branchId, date, time));
            }
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return shiftIds;
    }


    public boolean removeWorker(int employeeId, ShiftId shiftId){
        String sql = "DELETE FROM Shifts WHERE branchId = (?) AND date = (?) AND time = (?) AND employeeId = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, shiftId.getBranchId());
            pstmt.setString(2, shiftId.getDate().toString());
            pstmt.setString(3, shiftId.getShiftTime().name());
            pstmt.setInt(4, employeeId);
            pstmt.executeUpdate();
            //if(pstmt.executeUpdate() != 1) {
            //    return false;
            //}
        }
        catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean addWorker(ShiftId shiftId, int employeeId, List<String> qualifications){
        String sql = "INSERT INTO Shifts(branchId, date, time, employeeId, job) VALUES(?,?,?,?,?)";
        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (String job : qualifications) {
                pstmt.setInt   (1 , shiftId.getBranchId());
                pstmt.setString(2 , shiftId.getDate().toString());
                pstmt.setString(3 , shiftId.getShiftTime().name());
                pstmt.setInt   (4 , employeeId);
                pstmt.setString(5 , job);
                pstmt.addBatch();
            }
            if(pstmt.executeBatch().length != qualifications.size()){
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }



    public boolean Delete(ShiftId shiftId){
        String sql = "DELETE FROM Shifts WHERE branchId = (?) AND date = (?) AND time = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, shiftId.getBranchId());
            pstmt.setString(2, shiftId.getDate().toString());
            pstmt.setString(3, shiftId.getShiftTime().name());
            //if(pstmt.executeUpdate() != 1) {
            //    return false;
            //}
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            return false;
        }
        shiftsCache.remove(shiftId);
        return true;
    }

    public boolean DeleteQualification(String name){
        String sql = "DELETE FROM Shifts WHERE job = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            if(pstmt.executeUpdate() < 1) {
                return false;
            }
        }
        catch (SQLException e) {
            return false;
        }
        return true;

    }

    private List<ShiftId> getAllShiftIdsFromBranch(int branchId){
        String sql = "SELECT DISTINCT date, time FROM Shifts WHERE branchId = (?)";
        List<ShiftId> shiftIds = new ArrayList<>();
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1 ,branchId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                LocalDateTime date = LocalDateTime.parse(rs.getString("date"));
                ShiftTime time = ShiftTime.valueOf(rs.getString("time"));
                shiftIds.add(new ShiftId(branchId, date, time));
            }
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return shiftIds;

    }

    public void DeleteAll(){
        String sql = "DELETE FROM Shifts";
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
