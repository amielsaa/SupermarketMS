package EmployeeModule.PresentationLayer.CLI.Pages;

import EmployeeModule.BusinessLayer.*;
import EmployeeModule.ServiceLayer.Gateway;
import Utilities.Exceptions.CLIException;
import Utilities.Pair;
import Utilities.Response;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import static Utilities.CLIUtil.PrettyInput.*;
import static Utilities.CLIUtil.PrettyPrint.*;
import static Utilities.Util.checkLegalId;

public class EditEmployeePage extends OptionsMenuPage
{
    ResponsePage<Boolean> pgEmptyFalse = new ReturnPage();

    int e_id;

    @Override
    public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
    {
        e_id = printAndWaitForLegalInt(input, "Enter the id of the employee you wish to edit: ", (x) -> checkLegalId(x), "Illegal id. ");
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
            put("Edit Name", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                String new_name = printAndWaitForLegalString(args.getKey(), "New Name: ");
                Response<String> r_emp_name = args.getValue().updateEmployeeName(e_id, new_name);
                if(!r_emp_name.isSuccess()){
                    System.out.println(r_emp_name.getMessage());
                    return false;
                }
                System.out.println(makeSuccessMessage());
                return false;
            }));
            put("Edit Salary", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                Double new_salary = printAndWaitForLegalDouble(args.getKey(), "New Salary: ");
                Response<Double> r_emp_name = args.getValue().updateEmployeeSalary(e_id, new_salary);
                if(!r_emp_name.isSuccess()){
                    System.out.println(r_emp_name.getMessage());
                    return false;
                }
                System.out.println(makeSuccessMessage());
                return false;
            }));
            put("Edit bank account details", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                int b_id = printAndWaitForLegalInt(args.getKey(), "New bank id: ", (x) -> (x > 0), "Illegal id number. ");
                int b_b_id = printAndWaitForLegalInt(args.getKey(), "New bank branch id: ", (x) -> (x > 0), "Illegal id number. ");
                int acc_id = printAndWaitForLegalInt(args.getKey(), "New account id: ", (x) -> (x > 0), "Illegal id number. ");
                String b_name = printAndWaitForLegalString(args.getKey(), "New bank name: ");
                String b_b_name = printAndWaitForLegalString(args.getKey(), "New bank branch name: ");
                String acc_o_name = printAndWaitForLegalString(args.getKey(), "New account owner name: ");
                BankAccountDetails new_bad = new BankAccountDetails(b_id, b_b_id, acc_id, b_name, b_b_name, acc_o_name);
                Response<BankAccountDetails> r_emp_name = args.getValue().updateEmployeeBankAccountDetails(e_id, new_bad);
                if(!r_emp_name.isSuccess()){
                    System.out.println(r_emp_name.getMessage());
                    return false;
                }
                System.out.println(makeSuccessMessage());
                return false;
            }));
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
            put("Add Qualification", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                String qual_name_adding = printAndWaitForLegalString(args.getKey(), "New qualification name: ");
                Response<Qualification> q_res_adding = args.getValue().getQualification(qual_name_adding);
                if(!q_res_adding.isSuccess())
                {
                    System.out.println(q_res_adding.getMessage());
                    return false;
                }
                Response<Qualification> add_res = args.getValue().employeeAddQualification(e_id, q_res_adding.getData());
                if(!add_res.isSuccess())
                {
                    System.out.println(add_res.getMessage());
                    return false;
                }
                System.out.println(makeSuccessMessage());
                return false;
            }));
            put("Remove Qualification", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                String qual_name = printAndWaitForLegalString(args.getKey(), "Qualification Name: ");
                Response<Qualification> q_res = args.getValue().employeeRemoveQualification(e_id, qual_name);
                if(!q_res.isSuccess()){
                    System.out.println(q_res.getMessage());
                    return false;
                }
                System.out.println(makeSuccessMessage());
                return false;
            }));
        }};
    }

    @Override
    public String getTitle()
    {
        return "Edit Employee";
    }
}
