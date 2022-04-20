package BusinessLayer;

import BusinessLayer.Permission;
import BusinessLayer.Qualification;
import DataAccessLayer.DALController;
import Utilities.Response;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class QualificationController
{
    //general list of all qualifications; if an employee has it then it is in his list too
    private List<Qualification> qualifications;
    //general list of permissions; if a qualification gives such permission then it is in the qualification's list
    private List<Permission> permissions;

    public QualificationController(DALController dalController){
        //TODO make a call to DAL to restore the state
        dalController.execute();
        qualifications = new ArrayList<>();
        permissions = new ArrayList<>();
    }

    public Response<List<Qualification>> getQualifications() {
        return Response.makeSuccess(qualifications);
    }

    public Response<Qualification> getQualification(@NotNull String name){
        for (Qualification q: qualifications) {
            if(q.getName().equals(name)){
                return Response.makeSuccess(q);
            }
        }
        return Response.makeFailure("failed to find qualification with that name");
    }

    //TODO plan how to add new qualifications - add a blank list of permissions or make the list earlier
    public Response<Qualification> addQualification(@NotNull String name){
        Qualification toAdd = new Qualification(name, new ArrayList<>());
        if(qualifications.contains(toAdd)){
            return Response.makeFailure("a qualification with such name already exists");
        }
        qualifications.add(toAdd);
        return Response.makeSuccess(toAdd);
    }

    //TODO check if it is wise to give an  option to rename a Qualification
    public Response<Qualification> renameQualification(@NotNull String name, @NotNull String newName){
        Qualification toChange = null;
        for (Qualification q: qualifications) {
            if(q.getName().equals(name)){
                toChange = q;
            }
            if(q.getName().equals(newName)){
                return Response.makeFailure("a qualification with such name already exists");
            }
        }
        if(toChange!=null){
            toChange.setName(newName);
        }
        return Response.makeFailure("a qualification with given name doesn't exist");
    }

    public Response<Permission> getPermission(@NotNull String name){
        for (Permission p: permissions) {
            if(p.getName().equals(name)){
                return Response.makeSuccess(p);
            }
        }
        return Response.makeFailure("a permission with given name doesn't exist");
    }

    public Response<Permission> addPermission(@NotNull String name){
        Permission toAdd = new Permission(name);
        if(permissions.contains(toAdd)){
            return Response.makeFailure("a permission with such name already exists");
        }
        permissions.add(toAdd);
        return Response.makeSuccess(toAdd);
    }

    public Response<Permission> removePermission(@NotNull String name) {
        Permission toRemove = null;
        for (Permission p : permissions) {
            if (p.getName().equals(name)) {
                toRemove = p;
            }
        }
        if (toRemove != null) {
            permissions.remove(toRemove);
            return Response.makeSuccess(toRemove);
        } else {
            return Response.makeFailure("a permission with given name doesn't exist");


        }
        //TODO add interaction with DAL


    }

    public Response<Qualification> addPermissionToQualification(String permName, String qualName){
        Response<Qualification> res1 = getQualification(qualName);
        if(!res1.isSuccess()){
            return Response.makeFailure("no qualification with such name");
        }
        Qualification q = res1.getData();
        Response<Permission> res2 = getPermission(permName);
        if(!res2.isSuccess()){
            return Response.makeFailure("no permission with such name");
        }
        Permission p = res2.getData();
        q.addPermission(p);
        return  Response.makeSuccess(q);
    }

    public Response<Qualification> removePermissionFromQualification(String permName, String qualName){
        Response<Qualification> res1 = getQualification(qualName);
        if(!res1.isSuccess()){
            return Response.makeFailure("no qualification with such name");
        }
        Qualification q = res1.getData();
        Response<Permission> res2 = getPermission(permName);
        if(!res2.isSuccess()){
            return Response.makeFailure("no permission with such name");
        }
        Permission p = res2.getData();
        q.removePermission(p);
        return  Response.makeSuccess(q);
    }


}
