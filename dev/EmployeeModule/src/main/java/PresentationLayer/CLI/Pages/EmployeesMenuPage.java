package PresentationLayer.CLI.Pages;

import BusinessLayer.BankAccountDetails;
import BusinessLayer.Employee;
import BusinessLayer.Qualification;
import BusinessLayer.TimeInterval;
import ServiceLayer.Gateway;
import Utilities.CLIException;
import Utilities.Pair;
import Utilities.Response;

import java.time.LocalDateTime;
import java.util.*;

import static Utilities.PrettyInput.printAndWaitForLegalInt;
import static Utilities.PrettyPrint.*;
import static Utilities.Util.checkLegalId;

public class EmployeesMenuPage extends OptionsMenuPage
{
    ResponsePage<Boolean> pgEmptyFalse = new ReturnPage();
    ResponsePage<Boolean> pgAddEmployee = new AddEmployeePage();
    ResponsePage<Boolean> pgEditEmployee = new EditEmployeePage();
    ResponsePage<Boolean> pgManageWorkingHours = new ManageEmployeeWorkingHoursPage();

    @Override
    public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
    {
        // TODO try fetch and display employees
//        Response<List<Employee>> res_employees = gateway.getEmployees();
//        if(!res_employees.isSuccess()){
//            System.out.println(res_employees.getMessage());
//            break;
//        }
//        List<Employee> employees = res_employees.getData();
//        for (Employee employee : employees) {
//
//            System.out.println(employee);
//        }

        super.runWithResponse(input, g);
        return true;
    }

    @Override
    public Map<String, ResponsePage<Boolean>> getOptionsMap()
    {
        return new LinkedHashMap<String, ResponsePage<Boolean>>() {{
            put("Return", pgEmptyFalse);
            // UNCOMMENT TO ADD FUNCTIONALITY
//            put("Get Employee", makeResponsePage((Pair<Scanner, Gateway> args) -> {
//                int e_id = printAndWaitForLegalInt(args.getKey(), "Enter the employee id: ", (x) -> checkLegalId(x), "Illegal id. ");
//                Response<Employee> res_emp = args.getValue().getEmployee(e_id);
//                if(!res_emp.isSuccess()){
//                    System.out.println(makeErrorMessage(res_emp.getMessage()));
//                }
//                else {
//                    System.out.println(makeSuccessMessage());
//                }
//                return true;
//            }));
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
            put("Manage Working Hours", pgManageWorkingHours);
        }};
    }

    @Override
    public String getTitle()
    {
        return "Manage Employees";
    }
}
