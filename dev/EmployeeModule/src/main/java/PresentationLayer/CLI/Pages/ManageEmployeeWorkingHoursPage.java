package PresentationLayer.CLI.Pages;

import BusinessLayer.TimeInterval;
import ServiceLayer.Gateway;
import Utilities.CLIException;
import Utilities.Response;

import java.time.LocalDateTime;
import java.util.Scanner;

public class ManageEmployeeWorkingHoursPage extends ResponsePage<Boolean>
{
    @Override
    public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
    {
        // TODO IMPLEMENT
        //      Remove employee working hours
        //      Add employee working hours

//        case addEmployeeWorkingHours:
//        System.out.println("Please provide starting time in the next format: yyyy-MM-ddThh:mm:ss");
//        LocalDateTime start = getDateInput(input);
//        System.out.println("Please provide ending time in the next format: yyyy-MM-ddThh:mm:ss");
//        LocalDateTime finish = getDateInput(input);
//        Response<TimeInterval> r_time = gateway.employeeAddWorkingHour(e_id, start, finish);
//        if(!r_time.isSuccess()){
//            System.out.println(r_time.getMessage());
//            break;
//        }
//        System.out.println("Success");
//        break;
//        case removeEmployeeWorkingHours:
//        System.out.println("Please provide starting time in the next format: yyyy-MM-ddThh:mm:ss");
//        start = getDateInput(input);
//        System.out.println("Please provide ending time in the next format: yyyy-MM-ddThh:mm:ss");
//        finish = getDateInput(input);
//        r_time = gateway.employeeAddWorkingHour(e_id, start, finish);
//        if(!r_time.isSuccess()){
//            System.out.println(r_time.getMessage());
//            break;
//        }
//        System.out.println("Success");
//        break;

        return true;
    }
}
