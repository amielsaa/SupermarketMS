package EmployeeModule.PresentationLayer.CLI.Pages;

import EmployeeModule.BusinessLayer.Permission;
import EmployeeModule.BusinessLayer.Qualification;
import EmployeeModule.ServiceLayer.Gateway;
import Utilities.Exceptions.CLIException;
import Utilities.Pair;
import Utilities.CLIUtil.PrettyTable;
import Utilities.Response;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static Utilities.CLIUtil.PrettyInput.printAndWaitForLegalInt;
import static Utilities.CLIUtil.PrettyInput.printAndWaitForLegalString;
import static Utilities.CLIUtil.PrettyPrint.*;

public class EditQualificationPage extends OptionsMenuPage
{
    private ResponsePage<Boolean> pgEmptyFalse = new ReturnPage();

    private Qualification qualification;

    @Override
    public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException {
        String qualificationName = printAndWaitForLegalString(input, "Enter the name of the qualification you wish to edit: ");
        Response<Qualification> res_qual = g.getQualification(qualificationName);
        if(res_qual.isSuccess()){
            this.qualification = res_qual.getData();
            super.runWithResponse(input, g);
        }
        else {
            System.out.println(makeErrorMessage("Failed to show qualification, " + res_qual.getMessage()));
        }
        return true;
    }

    @Override
    public Map<String, ResponsePage<Boolean>> getOptionsMap()
    {
        return new LinkedHashMap<String, ResponsePage<Boolean>>() {{
            put("Return", pgEmptyFalse);
            put("Add Permission to Qualification", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                try {
                    String perm_name_add = printAndWaitForLegalString(args.getKey(), "Name of the permission to add: ");
                    Response<Qualification> res_perm_add = args.getValue().addPermissionToQualification(perm_name_add, qualification.getName());
                    if(!res_perm_add.isSuccess()){
                        throw new CLIException(res_perm_add.getMessage());
                    }
                    System.out.println(makeSuccessMessage());
                } catch (Exception e) {
                    System.out.println(makeErrorMessage(e.getMessage()));
                } finally {
                    return true;
                }
            }));
            put("Remove Permission from Qualification", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                try {
                    List<Permission> permissions = qualification.getPermissions();
                    System.out.println(makeBigTitle("Permissions of " + qualification.getName()));
                    PrettyTable table = new PrettyTable("Name");
                    for (Permission p : permissions)
                    {
                        table.insert(p.getName());
                    }
                    System.out.println(table.toString());

                    String perm_name_rem2 = printAndWaitForLegalString(args.getKey(), "Name of the permission to remove: ");
                    Response<Qualification> res_perm_rem2 = args.getValue().removePermissionFromQualification(perm_name_rem2, qualification.getName());
                    if(!res_perm_rem2.isSuccess()){
                        throw new CLIException(res_perm_rem2.getMessage());
                    }
                    System.out.println(makeSuccessMessage());
                } catch (Exception e) {
                    System.out.println(makeErrorMessage(e.getMessage()));
                } finally {
                    return true;
                }
            }));
        }};
    }

    @Override
    public String getTitle()
    {
        return "Editing " + qualification.getName();
    }
}
