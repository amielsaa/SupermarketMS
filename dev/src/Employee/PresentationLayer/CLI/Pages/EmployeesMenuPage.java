package Employee.PresentationLayer.CLI.Pages;

import Employee.BusinessLayer.Employee;
import Employee.BusinessLayer.*;
import Employee.ServiceLayer.Gateway;
import Utilities.Exceptions.CLIException;
import Utilities.Pair;
import Utilities.CLIUtil.PrettyTable;
import Utilities.Response;
import Utilities.ResponsePage;

import java.util.*;

import static Utilities.CLIUtil.PrettyInput.*;
import static Utilities.CLIUtil.PrettyPrint.*;
import static Utilities.Util.checkLegalId;

public class EmployeesMenuPage extends OptionsMenuPage
{
    ResponsePage<Boolean> pgEmptyFalse = new ReturnPage();
    ResponsePage<Boolean> pgAddEmployee = new AddEmployeePage();
    ResponsePage<Boolean> pgEditEmployee = new EditEmployeePage();

    @Override
    public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
    {
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
            put("Show Employee Information", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                int e_id = printAndWaitForLegalInt(args.getKey(), "Enter the employee id: ", (x) -> checkLegalId(x), "Illegal id. ");
                Response<Employee> res_emp = args.getValue().getEmployee(e_id);
                if(res_emp.isSuccess()){
                    Employee e = res_emp.getData();
                    System.out.println(makeBigTitle("Showing Information on " + e.getName()));
                    printEmployee(e);
                    printBankAccountDetails(e.getBankAccountDetails());
                }
                else {
                    System.out.println(makeErrorMessage("Failed to show employee, " + res_emp.getMessage()));
                }
                printAndWaitForLegalString(args.getKey(), "Enter anything to continue: ");
                return true;
            }));
            put("Add Employee", pgAddEmployee);
            put("Remove Employee", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                int e_id = printAndWaitForLegalInt(args.getKey(), "Enter the employee id: ", (x) -> checkLegalId(x), "Illegal id. ");
                Response<Employee> res_rem_emp = args.getValue().removeEmployee(e_id);
                if(!res_rem_emp.isSuccess()){
                    System.out.println(makeErrorMessage(res_rem_emp.getMessage()));
                }
                else {
                    System.out.println(makeSuccessMessage());
                }
                return true;
            }));
            put("Edit Employee", pgEditEmployee);
        }};
    }

    @Override
    public String getTitle()
    {
        return "Manage Employees";
    }
}
