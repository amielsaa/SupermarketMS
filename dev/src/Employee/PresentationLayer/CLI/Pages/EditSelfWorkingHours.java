package Employee.PresentationLayer.CLI.Pages;

import Employee.BusinessLayer.BankAccountDetails;
import Employee.BusinessLayer.Employee;
import Employee.BusinessLayer.Qualification;
import Employee.BusinessLayer.TimeInterval;
import Employee.ServiceLayer.Gateway;
import Utilities.Exceptions.CLIException;
import Utilities.Pair;
import Utilities.Response;
import Utilities.ResponsePage;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import static Utilities.CLIUtil.PrettyInput.*;
import static Utilities.CLIUtil.PrettyInput.printAndWaitForLegalString;
import static Utilities.CLIUtil.PrettyPrint.*;
import static Utilities.Util.checkLegalId;

public class EditSelfWorkingHours extends OptionsMenuPage {


    ResponsePage<Boolean> pgEmptyFalse = new ReturnPage();

    int e_id;

    @Override
    public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
    {
        //e_id = printAndWaitForLegalInt(input, "Enter the id of the employee you wish to edit: ", (x) -> checkLegalId(x), "Illegal id. ");
        Response<Employee> r = g.getLoggedUser();
        if(!r.isSuccess()){
            makeErrorMessage(r.getMessage());
            return true;
        }
        e_id = r.getData().getId();
        Response<Employee> res_emp = g.getEmployee(e_id);
        if(res_emp.isSuccess()){
            printEmployee(res_emp.getData());
            if(super.runWithResponse(input, g)) {
                runWithResponse(input, g);
            }
        }
        else {
            System.out.println(makeErrorMessage("Failed to show employee, " + res_emp.getMessage()));
        }
        return true;
    }


    @Override
    public Map<String, ResponsePage<Boolean>> getOptionsMap()
    {
        return new LinkedHashMap<String, ResponsePage<Boolean>>() {{
            put("Return", pgEmptyFalse);
            put("Add Working Hour", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                try
                {
                    System.out.println("Starting Time: ");
                    LocalDateTime start = getDateInput(args.getKey());
                    System.out.println("Ending Time: ");
                    LocalDateTime finish = getDateInput(args.getKey());
                    Response<TimeInterval> r_time = args.getValue().employeeAddWorkingHour(e_id, start, finish);
                    if(!r_time.isSuccess()){
                        throw new CLIException(r_time.getMessage());
                    }
                    System.out.println(makeSuccessMessage());
                } catch (Exception e) {
                    System.out.println(makeErrorMessage("Failed to add working hour, " + e.getMessage()));
                } finally {
                    return false;
                }
            }));
            put("Remove Working Hour", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                try
                {
                    LocalDateTime start = getDateInput(args.getKey());
                    Response<TimeInterval> r_time = args.getValue().employeeRemoveWorkingHour(e_id, start);
                    if(!r_time.isSuccess()){
                        throw new CLIException(r_time.getMessage());
                    }
                    System.out.println(makeSuccessMessage());
                } catch (Exception e) {
                    System.out.println(makeErrorMessage("Failed to remove working hour, " + e.getMessage()));
                } finally {
                    return false;
                }
            }));
        }};
    }

    @Override
    public String getTitle() {
        return null;
    }
}
