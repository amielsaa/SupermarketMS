package EmployeeModule.PresentationLayer.CLI.Pages;

import EmployeeModule.BusinessLayer.BankAccountDetails;
import EmployeeModule.BusinessLayer.Employee;
import EmployeeModule.ServiceLayer.Gateway;
import Utilities.Exceptions.CLIException;
import Utilities.Response;
import Utilities.ResponsePage;

import java.time.LocalDateTime;
import java.util.Scanner;

import static Utilities.CLIUtil.PrettyInput.*;
import static Utilities.CLIUtil.PrettyPrint.*;

public class AddEmployeePage extends ResponsePage<Boolean>
{

    @Override
    public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
    {
        try
        {
            System.out.println(makeTitle("Please enter the new employee's information"));
            int id = printAndWaitForLegalInt(input, "ID number: ", (x) -> (x > 0), "Illegal id number. ");
            String e_name = printAndWaitForLegalString(input, "Full Name: ");

            int b_id = printAndWaitForLegalInt(input, "Bank id: ", (x) -> (x > 0), "Illegal id number. ");
            int b_b_id = printAndWaitForLegalInt(input, "Bank branch id: ", (x) -> (x > 0), "Illegal id number. ");
            int acc_id = printAndWaitForLegalInt(input, "Account id: ", (x) -> (x > 0), "Illegal id number. ");
            String b_name = printAndWaitForLegalString(input, "Bank name: ");
            String b_b_name = printAndWaitForLegalString(input, "Bank branch name: ");
            String acc_o_name = printAndWaitForLegalString(input, "Account owner name: ");
            BankAccountDetails bad = new BankAccountDetails(b_id, b_b_id, acc_id, b_name, b_b_name, acc_o_name);

            Double salary = printAndWaitForLegalDouble(input, "Salary: ");

            System.out.println("Enter work starting date in the next format yyyy-MM-dd: ");

            Response<String> input_res = tryNextLine(input);
            if (!input_res.isSuccess())
            {
                throw new CLIException(input_res.getMessage());
            }
            String date = input_res.getData() + "T06:00:00";
            LocalDateTime st_date = LocalDateTime.parse(date);

            String w_c_desc = printAndWaitForLegalString(input, "Additional working conditions information: ", (s) -> (s.length() <= 300), "Input is too long. ");

            Response<Employee> res_add_emp = g.addEmployee(id, e_name, bad, salary, st_date, w_c_desc);
            if (!res_add_emp.isSuccess())
            {
                throw new CLIException(res_add_emp.getMessage());
            }
            System.out.println(makeSuccessMessage());

        } catch (Exception e) {
            System.out.println(makeErrorMessage("Failed to add new employee, " + e.getMessage()));
        } finally
        {
            return true;
        }
    }
}
