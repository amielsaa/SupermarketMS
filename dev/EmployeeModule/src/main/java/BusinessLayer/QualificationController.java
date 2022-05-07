package BusinessLayer;

import BusinessLayer.Permission;
import BusinessLayer.Qualification;
import DataAccessLayer.DALController;
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

    public QualificationController(@NotNull DALController dalController){
        //TODO make a call to DAL to restore the state
        dalController.execute();
        qualifications = new ArrayList<>();
        permissions = new ArrayList<>();
    }

    public List<Qualification> getQualifications() {
        return qualifications;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }


    public Qualification getQualification(@NotNull String name) throws Exception {
        for (Qualification q: qualifications) {
            if(q.getName().equals(name)){
                return q;
            }
        }
        throw new ObjectNotFoundException("failed to find qualification with that name");
    }

    public Qualification addQualification(@NotNull String name) throws Exception {
        Qualification toAdd = new Qualification(name, new ArrayList<>());
        if(qualifications.contains(toAdd)){
            throw new ObjectAlreadyExistsException("a qualification with such name already exists");
        }
        qualifications.add(toAdd);
        return toAdd;
    }

//    public Response<Qualification> renameQualification(@NotNull String name, @NotNull String newName){
//        Qualification toChange = null;
//        for (Qualification q: qualifications) {
//            if(q.getName().equals(name)){
//                toChange = q;
//            }
//            if(q.getName().equals(newName)){
//                return Response.makeFailure("a qualification with such name already exists");
//            }
//        }
//        if(toChange!=null){
//            toChange.setName(newName);
//        }
//        return Response.makeFailure("a qualification with given name doesn't exist");
//    }

    public Qualification removeQualification(@NotNull String name) throws Exception {
        Qualification toRemove = null;
        for(Qualification q : qualifications) {
            if(q.getName().equals(name)) {
                toRemove = q;
            }
        }
        if(toRemove == null) {
            throw new ObjectNotFoundException("No such qualification. ");
        }
        qualifications.remove(toRemove);
        return toRemove;
        //TODO add interaction with DAL
    }


    // TODO ADD Remove qualification and remove from linked empoyees and shifts the qualification like in "removePermission()"
    public Permission getPermission(@NotNull String name) throws Exception {
        for (Permission p: permissions) {
            if(p.getName().equals(name)){
                return p;
            }
        }
        throw new ObjectNotFoundException("a permission with given name doesn't exist");
    }

    public Permission addPermission(@NotNull String name) throws Exception {
        Permission toAdd = new Permission(name);
        if(permissions.contains(toAdd)){
            throw new ObjectAlreadyExistsException("a permission with such name already exists");
        }
        permissions.add(toAdd);
        return toAdd;
    }

    public Permission removePermission(@NotNull String name) throws Exception {
        Permission toRemove = null;
        for (Permission p : permissions) {
            if (p.getName().equals(name)) {
                toRemove = p;
                break;
            }
        }
        if (toRemove != null) {
            // unlink permission from existing qualifications
            for(Qualification q : qualifications) {
                try
                {
                    removePermissionFromQualification(name, q.getName());
                } catch (Exception e) {
                    // do nothing
                }
            }
            permissions.remove(toRemove);
            return toRemove;
        } else {
            throw new ObjectNotFoundException("a permission with given name doesn't exist");


        }
        //TODO add interaction with DAL


    }

    public Qualification addPermissionToQualification(String permName, String qualName) throws Exception {
        Qualification q = getQualification(qualName);
        Permission p = getPermission(permName);

        if(q.hasPermission(p)) {
            throw new ObjectAlreadyExistsException("The qualification already has this permission. ");
        }
        q.addPermission(p);
        return  q;
    }

    public Qualification removePermissionFromQualification(String permName, String qualName) throws Exception {
        Qualification q = getQualification(qualName);
        Permission p = getPermission(permName);

        if(!q.hasPermission(p)) {
            throw new ObjectNotFoundException("The qualification doesn't have this permission. ");
        }
        q.removePermission(p);
        return  q;
    }


}
