package EmployeeModule.PresentationLayer.CLI.Pages;

import EmployeeModule.BusinessLayer.Permission;
import EmployeeModule.BusinessLayer.Qualification;
import EmployeeModule.ServiceLayer.Gateway;
import Utilities.Exceptions.CLIException;
import Utilities.Pair;
import Utilities.CLIUtil.PrettyTable;
import Utilities.Response;
import Utilities.ResponsePage;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static Utilities.CLIUtil.PrettyInput.printAndWaitForLegalString;
import static Utilities.CLIUtil.PrettyPrint.*;

public class QualificationsMenuPage extends OptionsMenuPage
{
    private ResponsePage<Boolean> pgEmptyFalse = new ReturnPage();
    private ResponsePage<Boolean> pgEditQualification = new EditQualificationPage();

    @Override
    public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
    {
        // Print Qualifications
        Response<List<Qualification>> res_qualification = g.getQualifications();
        if(!res_qualification.isSuccess()){
            System.out.println(makeErrorMessage(res_qualification.getMessage()));
        }
        else {
            System.out.println(makeBigTitle("Qualifications"));
            List<Qualification> qualifications = res_qualification.getData();
            PrettyTable table = new PrettyTable("Name", "Permissions");
            for(Qualification q : qualifications) {
                String pString = "[";
                int i = 0;
                List<Permission> qPermissions = q.getPermissions();
                for(Permission p : qPermissions) {
                    pString += p.getName();
                    if(i < qPermissions.size() - 1) {
                        pString += ", ";
                    }
                    i++;
                }
                pString += "]";
                table.insert(q.getName(), pString);
            }
            System.out.println(table.toString());
        }
        // Print permissions
        Response<List<Permission>> res_permission = g.getPermissions();
        if(!res_permission.isSuccess()){
            System.out.println(makeErrorMessage(res_permission.getMessage()));
        } else
        {
            System.out.println(makeBigTitle("Permissions"));
            List<Permission> permissions = res_permission.getData();
            PrettyTable table = new PrettyTable("Name");
            for (Permission p : permissions)
            {
                table.insert(p.getName());
            }
            System.out.println(table.toString());
        }

        if(super.runWithResponse(input, g)) {
            runWithResponse(input, g);
        }
        return true;
    }
    @Override
    public Map<String, ResponsePage<Boolean>> getOptionsMap()
    {
        return new LinkedHashMap<String, ResponsePage<Boolean>>() {{
            put("Return", pgEmptyFalse);
            put("Add Qualification", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                try {
                    String qual_name = printAndWaitForLegalString(args.getKey(), "New qualification name: ");

                    Response<Qualification> res_qual = args.getValue().addQualification(qual_name);
                    if(!res_qual.isSuccess()){
                        throw new CLIException(res_qual.getMessage());
                    }
                    System.out.println(makeSuccessMessage());
                } catch (Exception e) {
                    System.out.println(makeErrorMessage(e.getMessage()));
                } finally {
                    return true;
                }
            }));
            put("Remove Qualification", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                try {
                    String qual_name = printAndWaitForLegalString(args.getKey(), "Name of the qualification to remove: ");

                    Response<Qualification> res_qual = args.getValue().removeQualification(qual_name);
                    if(!res_qual.isSuccess()){
                        throw new CLIException(res_qual.getMessage());
                    }
                    System.out.println(makeSuccessMessage());
                } catch (Exception e) {
                    System.out.println(makeErrorMessage(e.getMessage()));
                } finally {
                    return true;
                }
            }));
            put("Add Permission", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                try {
                    String per_name = printAndWaitForLegalString(args.getKey(), "New permission name: ");

                    Response<Permission> res_qual = args.getValue().addPermission(per_name);
                    if(!res_qual.isSuccess()){
                        throw new CLIException(res_qual.getMessage());
                    }
                    System.out.println(makeSuccessMessage());
                } catch (Exception e) {
                    System.out.println(makeErrorMessage(e.getMessage()));
                } finally {
                    return true;
                }
            }));
            put("Remove Permission", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                try {
                    String per_name = printAndWaitForLegalString(args.getKey(), "Name of the permission to remove: ");

                    Response<Permission> res_qual = args.getValue().removePermission(per_name);
                    if(!res_qual.isSuccess()){
                        throw new CLIException(res_qual.getMessage());
                    }
                    System.out.println(makeSuccessMessage());
                } catch (Exception e) {
                    System.out.println(makeErrorMessage(e.getMessage()));
                } finally {
                    return true;
                }
            }));
            put("Edit Qualification", pgEditQualification);
        }};
    }

    @Override
    public String getTitle()
    {
        return "Manage Qualifications";
    }
}
