package Utilities;

import EmployeeModule.ServiceLayer.Gateway;
import Utilities.Exceptions.CLIException;

import java.util.Scanner;
import java.util.function.Function;

public abstract class ResponsePage<T> implements Page
{
    public abstract T runWithResponse(Scanner input, Gateway g) throws CLIException;

    @Override
    public void run(Scanner input, Gateway g) throws CLIException
    {
        runWithResponse(input, g);
    }

    public static <T> ResponsePage<T> makeResponsePage (Function<Pair<Scanner, Gateway>, T> function) {
        return new ResponsePage<T>() {
            @Override
            public T runWithResponse(Scanner input, Gateway g) throws CLIException {
                return function.apply(new Pair<Scanner, Gateway>(input, g));
            }
        };
    }
}
