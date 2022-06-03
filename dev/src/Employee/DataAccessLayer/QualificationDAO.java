package Employee.DataAccessLayer;

import java.util.*;
import java.sql.*;

import Employee.BusinessLayer.Permission;
import Employee.BusinessLayer.Qualification;
import Employee.BusinessLayer.*;


public class QualificationDAO extends DataAccessObject {

    private HashMap<String, Qualification> qualificationCache;
    private HashMap<String, Permission> permissionCache;


    public QualificationDAO(String tableName) {
        super(tableName);
        qualificationCache = new HashMap<>();
        permissionCache = new HashMap<>();
    }

    public void clearCache(){
        qualificationCache = new HashMap<>();
        permissionCache  =new HashMap<>();
    }

    /**
     * The new qualification is added into the cache and into database with default permission
     * @param qualification
     * @return
     */
    public boolean CreateQualification(Qualification qualification) {
        String sql = "INSERT INTO Qualifications(qualification) VALUES(?)";
        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1 , qualification.getName());
            if(pstmt.executeUpdate() != 1) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        qualificationCache.put(qualification.getName(), qualification);
        return true;
    }

    /**
     * The new permission is added into the cache and into database with default qualification
     * @param permission
     * @return
     */
    public boolean CreatePermission(Permission permission) {
        String sql = "INSERT INTO Qualifications(permission) VALUES(?)";
        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1 , permission.getName());
            if(pstmt.executeUpdate() != 1) {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        permissionCache.put(permission.getName(), permission);
        return true;
    }

    public Qualification ReadQualification(String name) {
        if(qualificationCache.containsKey(name)){
            return qualificationCache.get(name);
        }
        String sql = "SELECT * FROM Qualifications WHERE qualification  = (?)";
        List<Permission> permissions = new ArrayList<>();
        boolean found = false;
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1 ,name);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                found = true;
                String permName = rs.getString("permission");
                if(!permName.equals("default")){
                    Permission permission = new Permission(permName);
                    permissions.add(permission);
                    permissionCache.put(permName, permission);
                }
            }
            rs.close();
        }
        catch (SQLException e) {
            return null;
        }
        if(!found){
            return null;
        }
        Qualification qualification = new Qualification(name, permissions);
        qualificationCache.put(name, qualification);
        return qualification;
    }

    public Permission ReadPermission(String name) {
        if(permissionCache.containsKey(name)){
            return permissionCache.get(name);
        }
        String sql = "SELECT * FROM Qualifications WHERE permission  = (?)";
        Permission permission = null;
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1 ,name);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                permission = new Permission(name);
            }
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        permissionCache.put(name, permission);
        return permission;
    }


    public List<Qualification> ReadAllQualifications(){
        List<String> names = getAllQualificationNames();
        List<Qualification> qualifications = new ArrayList<>();
        if(names == null){
            return null;
        }
        for (String name : names) {
            Qualification q = ReadQualification(name);
            if(q == null){
                return null;
            }
            //qualificationCache.put(name, q);
            qualifications.add(q);
        }
        return qualifications;
    }

    public List<Permission> ReadAllPermissions(){
        String sql = "SELECT DISTINCT permission FROM Qualifications";
        List<Permission> permissions = new ArrayList<>();
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String permName = rs.getString("permission");
                if(!permName.equals("default")){
                    Permission toAdd = new Permission(permName);
                    permissions.add(toAdd);
                    permissionCache.put(permName, toAdd);
                }
            }
            rs.close();
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return permissions;
    }

    private List<String> getAllQualificationNames(){
        List<String> names = new ArrayList<>();
        String sql = "SELECT DISTINCT qualification FROM Qualifications";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String name  = rs.getString("qualification");
                if(!name.equals("default")){
                    names.add(name);
                }
            }
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return names;
    }

    public boolean UpdateAddPermission(String qualName, String permName){
        String sql = "INSERT INTO Qualifications(QUALIFICATION, PERMISSION) VALUES (?, ?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, qualName);
            pstmt.setString(2, permName);
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

    public boolean UpdateRemovePermission(String qualName, String permName){
        String sql = "DELETE FROM Qualifications WHERE qualification = (?) AND permission = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, qualName);
            pstmt.setString(2, permName);
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

    /**
     * Deletes the qualification from database and from cache, meaning all the instances of the qualification
     * should be removed in this method
     * @param name
     * @return
     */
    public boolean DeleteQualification(String name){
        String sql = "DELETE FROM Qualifications WHERE qualification = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            if(pstmt.executeUpdate() < 1) {
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        qualificationCache.remove(name);
        return true;
    }

    /**
     * Deletes the permission from database and from permission cache; the permissions inside the qualifications should
     * be removed in business layer
     * @param name
     * @return
     */
    public boolean DeletePermission(String name){
        String sql = "DELETE FROM Qualifications WHERE permission = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            if(pstmt.executeUpdate() < 1) {
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        permissionCache.remove(name);
        return true;
    }

    public void DeleteAll(){
        String sql = "DELETE FROM Qualifications";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
    }



    //TODO: remove these lines
    /*
    private static String permissionsIntoString(List<Permission> permissions){
        StringBuilder res = new StringBuilder();
        for (Permission permission : permissions) {
            res.append(permission.getName()).append("; ");
        }
        return res.toString();
    }

    private static List<Permission> stringIntoPermissions(String permString){
        String[] allPsArr = permString.split("; ");
        ArrayList<Permission> permissions = new ArrayList<>();
        for (String s : allPsArr) {
            permissions.add(new Permission(s));
        }
        return permissions;
    }

    public boolean Delete(String name){
        String sql = "DELETE FROM Qualifications WHERE NAME = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            if(pstmt.executeUpdate() != 1) {
                return false;
            }
        }
        catch (SQLException e) {
            return false;
        }
        qualificationCache.remove(name);
        return true;
    }

    public boolean Update(Qualification qualification){
        String qualName = qualification.getName();
        String sql = "UPDATE Qualifications SET PERMISSIONS = (?) WHERE NAME = (?)";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, permissionsIntoString(qualification.getPermissions()));
            pstmt.setString(2, qualName);
            if(pstmt.executeUpdate() != 1) {
                return false;
            }
        }
        catch (SQLException e) {
            return false;
        }
        return true;
    }

    private List<String> getAllQualificationNames(){
        List<String> names = new ArrayList<>();
        String sql = "SELECT name FROM Qualifications";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    public List<Qualification> ReadAll(){
        List<String> names = getAllQualificationNames();
        List<Qualification> qualifications = new ArrayList<>();
        for (String name : names) {
            Qualification q = Read(name);
            if(q == null){
                return null;
            }
            qualifications.add(Read(name));
        }
        return qualifications;
    }

    public Qualification Read(String name) {
        if(qualificationCache.containsKey(name)){
            return qualificationCache.get(name);
        }
        String sql = "SELECT * FROM Qualifications WHERE NAME = (?)";
        Qualification q = null;
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1 ,name);
            ResultSet rs = pstmt.executeQuery();
            String qualName = null;
            String allPerms = null;
            while (rs.next()) {
                qualName = rs.getString("name");
                allPerms = rs.getString("permissions");
                List<Permission> permissions = stringIntoPermissions(allPerms);
                q = new Qualification(qualName, permissions);
            }
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        qualificationCache.put(name, q);
        return q;
    }

    public boolean Create(Qualification qualification) {
        String sql = "INSERT INTO Qualifications(name, permissions) VALUES(?,?)";
        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, qualification.getName());
            String allPs = permissionsIntoString(qualification.getPermissions());
            pstmt.setString(2, allPs);
            if(pstmt.executeUpdate() != 1){
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
        qualificationCache.put(qualification.getName(), qualification);
        return true;
    }
    */




}
