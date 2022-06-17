package Employee.PresentationLayer.CLI.Pages;

import Delivery.PresentationLayer.UserTerminal;
import Employee.BusinessLayer.Employee;
import Employee.BusinessLayer.Permission;
import Employee.BusinessLayer.Qualification;
import Employee.ServiceLayer.Gateway;
import SupplierInventory.SIPresentation;
import Utilities.CLIUtil.PrettyTable;
import Utilities.Exceptions.CLIException;
import Utilities.Response;
import Utilities.ResponsePage;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static Utilities.CLIUtil.PrettyPrint.makeBigTitle;
import static Utilities.CLIUtil.PrettyPrint.makeErrorMessage;

public class MenuPage extends OptionsMenuPage
{
    ResponsePage<Boolean> pgEmployeesMenu = new EmployeesMenuPage();
    ResponsePage<Boolean> pgViewEmployeesMenu = new ResponsePage<Boolean>()
    {
        @Override
        public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
        {
            // TODO Check if correct
            Response<List<Employee>> res_employees = g.getEmployees();
            if(!res_employees.isSuccess()){
                System.out.println(makeErrorMessage(res_employees.getMessage()));
            }
            else {
                List<Employee> employees = res_employees.getData();
                PrettyTable table = new PrettyTable("ID", "Name");
                for(Employee e : employees) {
                    table.insert(Integer.toString(e.getId()), e.getName());
                }
                System.out.println(table.toString());
            }
            return true;
        }
        @Override
        public void run(Scanner input, Gateway g) throws CLIException
        {
            runWithResponse(input, g);
        }
    };
    ResponsePage<Boolean> pgShiftsMenu = new ShiftsMenuPage();
    ResponsePage<Boolean> pgQualificationsMenu = new QualificationsMenuPage();
    ResponsePage<Boolean> pgViewQualificationsMenu = new ResponsePage<Boolean>()
    {
        @Override
        public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
        {
            // TODO Check if correct
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
            return false;
        }
        @Override
        public void run(Scanner input, Gateway g) throws CLIException
        {
            runWithResponse(input, g);
        }
    };
    ResponsePage<Boolean> pgEmptyFalse = new ResponsePage<Boolean>()
    {
        @Override
        public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
        {
            return false;
        }
        @Override
        public void run(Scanner input, Gateway g) throws CLIException
        {
            runWithResponse(input, g);
        }
    };

    UserTerminal deliverySystemTerminal = new UserTerminal();
    SIPresentation si = new SIPresentation();
    @Override
    public Map<String, ResponsePage<Boolean>> getOptionsMap()
    {
        return new LinkedHashMap<String, ResponsePage<Boolean>>() {{
            put("Logout", pgEmptyFalse);
            if(g.canManageEmployees().getData()) {
                put("Manage Employees", pgEmployeesMenu);
            } else if(g.canViewEmployees().getData()) {
                put("View Employees", pgViewEmployeesMenu);
            }
            if(g.canManageShift().getData()) {
                put("Manage Shifts", pgShiftsMenu);
            }
            if(g.canManageQualifications().getData()) {
                put("Manage Qualifications", pgQualificationsMenu);
            } else if(g.canViewQualifications().getData()) {
                put("View Qualifications", pgViewQualificationsMenu);
            }

            if(g.canViewDeliveries().getData()) {
                put("Open Delivery System", deliverySystemTerminal);
            }
            if(g.canViewInventory().getData() || g.canViewSuppliers().getData()) {
                put("Open Inventory System", si);
            }
        }};
    }

    @Override
    public String getTitle()
    {
        return "Main Menu";
    }
}
