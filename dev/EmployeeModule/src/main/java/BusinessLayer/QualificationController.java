package BusinessLayer;

import BusinessLayer.Permission;
import BusinessLayer.Qualification;
import DataAccessLayer.DALController;
import DataAccessLayer.QualificationDAO;
import Utilities.DatabaseAccessException;
import Utilities.ObjectAlreadyExistsException;
import Utilities.ObjectNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class QualificationController
{
    //general list of all qualifications; if an employee has it then it is in his list too
    private List<Qualification> qualifications;
    //general list of permissions; if a qualification gives such permission then it is in the qualification's list
    private List<Permission> permissions;
    private QualificationDAO qDao;

    public QualificationController(@NotNull DALController dalController){
        //TODO make a call to DAL to restore the state
        qDao = new QualificationDAO("Qualifications");
        //dalController.execute();
        qualifications = new ArrayList<>();
        permissions = new ArrayList<>();
    }

    public List<Qualification> getQualifications() throws DatabaseAccessException {
        List<Qualification> qualifications = qDao.ReadAllQualifications();
        if(qualifications == null){
            throw new DatabaseAccessException("failed to load all qualifications from database");
        }
        return qualifications;
    }

    public List<Permission> getPermissions() throws DatabaseAccessException {
        List<Permission> permissions = qDao.ReadAllPermissions();
        if(permissions == null){
            throw new DatabaseAccessException("failed to load all qualifications from database");
        }
        return permissions;
    }


    public Qualification getQualification(@NotNull String name) throws Exception {
        Qualification qualification = qDao.ReadQualification(name);
        if(qualification == null){
            throw new ObjectNotFoundException("failed to find qualification with that name");
        }
        return qualification;
    }

    public Qualification addQualification(@NotNull String name) throws Exception {
        Qualification toAdd = new Qualification(name, new ArrayList<>());
        Qualification alreadyExists = qDao.ReadQualification(name);
        if(alreadyExists != null){
            throw new ObjectAlreadyExistsException("a qualification with such name already exists");
        }
        boolean response = qDao.CreateQualification(toAdd);
        if(!response){
            throw new DatabaseAccessException("Failed to add a Qualification into the database");
        }
        return toAdd;
    }

    public Qualification removeQualification(@NotNull String name) throws Exception {
        Qualification alreadyExists = qDao.ReadQualification(name);
        if(alreadyExists == null){
            throw new ObjectNotFoundException("No such qualification.");
        }
        boolean response = qDao.DeleteQualification(name);
        if(!response){
            throw new DatabaseAccessException("Failed to add a Qualification into the database");
        }
        return alreadyExists;
    }


    // TODO ADD Remove qualification and remove from linked employees and shifts the qualification like in "removePermission()"
    public Permission getPermission(@NotNull String name) throws Exception {
        Permission permission = qDao.ReadPermission(name);
        if(permission == null){
            throw new ObjectNotFoundException("a permission with given name doesn't exist");
        }
        return permission;
    }

    public Permission addPermission(@NotNull String name) throws Exception {
        Permission toAdd = new Permission(name);
        if(qDao.ReadPermission(name) != null){
            throw new ObjectAlreadyExistsException("a permission with such name already exists");
        }
        boolean response = qDao.CreatePermission(toAdd);
        return toAdd;
    }

    public Permission removePermission(@NotNull String name) throws Exception {
        Permission toRemove = qDao.ReadPermission(name);
        if (toRemove != null) {
            boolean response = qDao.DeletePermission(name);
            if(!response){
                throw new DatabaseAccessException("failed to remove permission from database");
            }
            // unlink permission from existing qualifications
            List<Qualification> qualificationsList = qDao.ReadAllQualifications();
            for(Qualification q : qualificationsList) {
                try
                {
                    removePermissionFromQualification(name, q.getName());
                } catch (Exception e) {
                    // do nothing
                    e.printStackTrace();
                }
            }
            return toRemove;
        } else {
            throw new ObjectNotFoundException("a permission with given name doesn't exist");
        }
    }

    public Qualification addPermissionToQualification(String permName, String qualName) throws Exception {
        Qualification q = qDao.ReadQualification(qualName);
        Permission p = qDao.ReadPermission(permName);
        if(q == null){
            throw new ObjectNotFoundException("The qualification doesn't exist");
        }
        if(p == null){
            throw new ObjectNotFoundException("The permission doesn't exist");
        }
        if(q.hasPermission(p)) {
            throw new ObjectAlreadyExistsException("The qualification already has this permission. ");
        }
        boolean response = qDao.UpdateAddPermission(qualName, permName);
        if(!response){
            throw new DatabaseAccessException("failed to update qualification's permissions");
        }
        q.addPermission(p);
        return  q;
    }

    public Qualification removePermissionFromQualification(String permName, String qualName) throws Exception {
        Qualification q = qDao.ReadQualification(qualName);
        Permission p = qDao.ReadPermission(permName);
        if(!q.hasPermission(p)) {
            throw new ObjectNotFoundException("The qualification doesn't have this permission. ");
        }
        boolean response = qDao.UpdateRemovePermission(qualName, permName);
        if(!response){
            throw new DatabaseAccessException("failed to update qualification's permissions");
        }
        q.addPermission(p);
        return  q;
    }

    public Permission checkPermission(List<String> qualifications, Permission permission) throws Exception{
        for (String qualification : qualifications) {
            if(qDao.ReadQualification(qualification).hasPermission(permission)){
                return permission;
            }
        }
        throw new ObjectNotFoundException("Employee does not have the permission. ");
    }

    public void clearDatabases(){
        qDao.DeleteAll();
    }


}
