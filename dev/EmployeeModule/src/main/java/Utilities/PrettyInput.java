package Utilities;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Predicate;

public class PrettyInput
{
    public static Integer printAndWaitForLegalInt(Scanner s, String prompt) {
        return printAndWaitForLegalInt(s, prompt, new Predicate<Integer>()
        {
            @Override
            public boolean test(Integer integer)
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

    public static Response<Integer> tryNextInt(Scanner s) {
        try {
            return Response.makeSuccess(s.nextInt());
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
}
