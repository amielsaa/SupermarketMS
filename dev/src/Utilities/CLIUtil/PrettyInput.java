package Utilities.CLIUtil;

import Employee.BusinessLayer.ShiftId;
import Employee.BusinessLayer.ShiftTime;
import Utilities.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Predicate;

public class PrettyInput
{
    public static Integer printAndWaitForLegalInt(Scanner s, String prompt) {
        return printAndWaitForLegalInt(s, prompt, new Predicate<Integer>()
        {
            @Override
            public boolean test(Integer i)
            {
                return true;
            }
        }, "Will never get here. ");
    }

    public static String printAndWaitForLegalString(Scanner s, String prompt) {
        return printAndWaitForLegalString(s, prompt, new Predicate<String>()
        {
            @Override
            public boolean test(String s)
            {
                return true;
            }
        }, "Will never get here. ");
    }

    public static Double printAndWaitForLegalDouble(Scanner s, String prompt) {
        return printAndWaitForLegalDouble(s, prompt, new Predicate<Double>()
        {
            @Override
            public boolean test(Double d)
            {
                return true;
            }
        }, "Will never get here. ");
    }

    public static Integer printAndWaitForLegalInt(Scanner s, String prompt, Predicate<Integer> pred, String predMessage) {
        System.out.print(prompt);
        Response<Integer> res = tryNextInt(s);
        while(!res.isSuccess() || !pred.test(res.getData())) {
            if(!res.isSuccess())
            {
                System.out.println(res.getMessage());
            }
            else {
                System.out.println(predMessage);
            }
            System.out.print(prompt);
            res = tryNextInt(s);
        }
        return res.getData();
    }

    public static String printAndWaitForLegalString(Scanner s, String prompt, Predicate<String> pred, String predMessage) {
        System.out.print(prompt);
        Response<String> res = tryNextLine(s);
        while(!res.isSuccess() || !pred.test(res.getData())) {
            if(!res.isSuccess())
            {
                System.out.println(res.getMessage());
            }
            else {
                System.out.println(predMessage);
            }
            System.out.print(prompt);
            res = tryNextLine(s);
        }
        return res.getData();
    }

    public static Double printAndWaitForLegalDouble(Scanner s, String prompt, Predicate<Double> pred, String predMessage) {
        System.out.print(prompt);
        Response<Double> res = tryNextDouble(s);
        while(!res.isSuccess() || !pred.test(res.getData())) {
            if(!res.isSuccess())
            {
                System.out.println(res.getMessage());
            }
            else {
                System.out.println(predMessage);
            }
            System.out.print(prompt);
            res = tryNextDouble(s);
        }
        return res.getData();
    }
    public static ShiftId printAndWaitForLegalShiftId(Scanner s, String prompt) {
        int branchId = printAndWaitForLegalInt(s, "Please provide the branch id: ", (x) -> (x >= 0), "Illegal branch id. ");
        return printAndWaitForLegalShiftId(s, prompt, branchId);
    }
    public static ShiftId printAndWaitForLegalShiftId(Scanner s, String prompt, int branchId) {
            System.out.println(PrettyPrint.makeTitle("Shift Date"));
            LocalDateTime date = getDateInput(s);
            System.out.println("");
            String _sh_time = printAndWaitForLegalString(s, "Please provide the shift time (DAY/NIGHT): ", (String str) -> (str.equals("DAY") || str.equals("NIGHT")), "Wrong input. ");
            ShiftTime time = _sh_time.equals("DAY") ? ShiftTime.DAY : (_sh_time.equals("NIGHT") ? ShiftTime.NIGHT : null);
            return new ShiftId(branchId, date, time);
    }
    public static Response<Integer> tryNextInt(Scanner s) {
        try {
            return Response.makeSuccess(Integer.parseInt(s.nextLine()));
        } catch (Exception e) {
            if(e instanceof InputMismatchException) {
                // flushing buffer
                s.nextLine();
                return Response.makeFailure("Illegal input.");
            }
            else {
                // flushing buffer
                s.nextLine();
                return Response.makeFailure("Error while getting input.  " + e.getMessage());
            }
        }
    }

    public static Response<String> tryNextLine(Scanner s) {
        try {
            return Response.makeSuccess(s.nextLine());
        } catch (Exception e) {
            if(e instanceof InputMismatchException) {
                // flushing buffer
                s.nextLine();
                return Response.makeFailure("Illegal input.");
            }
            else {
                // flushing buffer
                s.nextLine();
                return Response.makeFailure("Error while getting input.  " + e.getMessage());
            }
        }
    }

    public static Response<Double> tryNextDouble(Scanner s) {
        try {
            return Response.makeSuccess(Double.parseDouble(s.nextLine()));
        } catch (Exception e) {
            if(e instanceof InputMismatchException) {
                // flushing buffer
                s.nextLine();
                return Response.makeFailure("Illegal input.");
            }
            else {
                // flushing buffer
                s.nextLine();
                return Response.makeFailure("Error while getting input.  " + e.getMessage());
            }
        }
    }

    public static LocalDateTime getDateInput(Scanner input){
        System.out.println("Please provide the date in the following format: 'yyyy-MM-dd', for example: 2022-03-29;\n" +
                "If you want you can add the full time in format 'yyyy-MM-ddThh:mm:ss', for example: 2022-03-29T21:08:30");
        while (true){
            Response<String> res = tryNextLine(input);
            if(!res.isSuccess()) {
                System.out.println("Wrong input, try again");
                continue;
            }
            String in = res.getData();
            if(isDateAndTime(in + "T06:00:00")){
                return LocalDateTime.parse(in + "T06:00:00");
            }
            else if(isDateAndTime(in)){
                return LocalDateTime.parse(in);
            }
            else{
                System.out.println("Wrong input, try again");
            }
        }
    }

    private static boolean isDateAndTime(String s){
        try{
            LocalDateTime.parse(s);
        }
        catch (DateTimeParseException e){
            return false;
        }
        return true;
    }
}
