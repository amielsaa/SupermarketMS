package PresentationLayer.CLI.Pages;

import BusinessLayer.BankAccountDetails;
import BusinessLayer.Qualification;
import ServiceLayer.Gateway;
import Utilities.CLIException;
import Utilities.Response;

import java.util.Scanner;

public class EditEmployeePage extends ResponsePage<Boolean>
{
    @Override
    public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
    {
//        TODO IMPLEMENT
//          Update employee name
//          Update employee salary
//          Update employee bank account details
//          Add employee qualifications
//          Remove employee qualifications

//        case updateEmployeeName:
//        System.out.println("Please provide the employee's new name");
//        String new_name = getStringInput(input);
//        Response<String> r_emp_name = gateway.updateEmployeeName(e_id, new_name);
//        if(!r_emp_name.isSuccess()){
//            System.out.println(r_emp_name.getMessage());
//            break;
//        }
//        System.out.println("Success");
//        break;
//        case updateEmployeeSalary:
//        System.out.println("Please provide the employee's salary");
//        double new_salary = getDoubleInput(input);
//        Response<Double> r_sal = gateway.updateEmployeeSalary(e_id, new_salary);
//        if(!r_sal.isSuccess()){
//            System.out.println(r_sal.getMessage());
//            break;
//        }
//        System.out.println("Success");
//        break;
//        case updateEmployeeBankAccountDetails:
//        System.out.println("Please provide new bank account details:");
//        System.out.println("Bank id");
//        int b_id = getIntInput(input);
//        System.out.println("Bank branch id");
//        int b_b_id = getIntInput(input);
//        System.out.println("Account id");
//        int acc_id = getIntInput(input);
//        System.out.println("Bank name");
//        String b_name = getStringInput(input);
//        System.out.println("Bank branch name");
//        String b_b_name = getStringInput(input);
//        System.out.println("Account owner name");
//        String acc_o_name = getStringInput(input);
//        BankAccountDetails bad = new BankAccountDetails(b_id, b_b_id, acc_id, b_name, b_b_name, acc_o_name);
//        Response<BankAccountDetails> r_bad = gateway.updateEmployeeBankAccountDetails(e_id, bad);
//        if(!r_bad.isSuccess()){
//            System.out.println(r_bad.getMessage());
//            break;
//        }
//        System.out.println("Success");
//        break;
//        case addEmployeeQualifications:
//        System.out.println("Please provide the name of qualification to add");
//        String qual_name_adding = getStringInput(input);
//        Response<Qualification> q_res_adding = gateway.getQualification(qual_name_adding);
//        if(!q_res_adding.isSuccess()){
//            System.out.println(q_res_adding.getMessage());
//            break;
//        }
//        Response<Qualification> q_add_res = gateway.employeeAddQualification(e_id, q_res_adding.getData());
//        if(!q_add_res.isSuccess()){
//            System.out.println(q_add_res.getMessage());
//            break;
//        }
//        System.out.println("Success");
//        break;
//        case removeEmployeeQualifications:
//        System.out.println("Please provide the name of qualification to delete");
//        String qual_name = getStringInput(input);
//        Response<Qualification> q_res = gateway.employeeRemoveQualification(e_id, qual_name);
//        if(!q_res.isSuccess()){
//            System.out.println(q_res.getMessage());
//            break;
//        }
//        System.out.println("Success");
//        break;

        return true;
    }
}
