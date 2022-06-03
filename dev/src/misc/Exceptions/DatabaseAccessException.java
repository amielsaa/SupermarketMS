package Utilities.Exceptions;

public class DatabaseAccessException extends Exception {

    public DatabaseAccessException(String errorMessage){
        super(errorMessage);
    }
}
