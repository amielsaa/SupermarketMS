package Utilities.Exceptions;

import Utilities.Response;

public class CLIException extends Exception
{
    public CLIException(String errorMessage){
        super(errorMessage);
    }

    public static <T> T fetchResponse(Response<T> result) throws CLIException {
        if(!result.isSuccess()) {
            throw new CLIException(result.getMessage());
        }
        return result.getData();
    }
}
