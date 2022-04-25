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

    public QualificationController(@NotNull DALController dalController){
        //TODO make a call to DAL to restore the state
        dalController.execute();
        qualifications = new ArrayList<>();
        permissions = new ArrayList<>();
    }

    public Response<List<Qualification>> getQualifications() {
        return Response.makeSuccess(qualifications);
    }

    public Response<List<Permission>> getPermissions() {
        return Response.makeSuccess(permissions);
    }


    public Response<Qualification> getQualification(@NotNull String name){
        for (Qualification q: qualifications) {
            if(q.getName().equals(name)){
                return Response.makeSuccess(q);
            }
        }
        return Response.makeFailure("failed to find qualification with that name");
    }

    public Response<Qualification> addQualification(@NotNull String name){
        Qualification toAdd = new Qualification(name, new ArrayList<>());
        if(qualifications.contains(toAdd)){
            return Response.makeFailure("a qualification with such name already exists");
        }
        qualifications.add(toAdd);
        return Response.makeSuccess(toAdd);
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

    public Response<Qualification> removeQualification(@NotNull String name) {
        Qualification toRemove = null;
        for(Qualification q : qualifications) {
            if(q.getName().equals(name)) {
                toRemove = q;
            }
        }
        if(toRemove == null) {
            return Response.makeFailure("No such qualification. ");
        }
        qualifications.remove(toRemove);
        return Response.makeSuccess(toRemove);
        //TODO add interaction with DAL
    }


    // TODO ADD Remove qualification and remove from linked empoyees and shifts the qualification like in "removePermission()"
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
                break;
            }
        }
        if (toRemove != null) {
            // unlink permission from existing qualifications
            for(Qualification q : qualifications) {
                removePermissionFromQualification(name, q.getName());
            }
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
        if(q.hasPermission(p)) {
            return Response.makeFailure("The qualification already has this permission. ");
        }
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
        if(!q.hasPermission(p)) {
            return Response.makeFailure("The qualification doesn't have this permission. ");
        }
        q.removePermission(p);
        return  Response.makeSuccess(q);
    }


}
