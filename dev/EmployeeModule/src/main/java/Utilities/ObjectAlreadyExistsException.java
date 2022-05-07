package Utilities;

public class ObjectAlreadyExistsException extends Exception {
    public ObjectAlreadyExistsException(String errorMessage){
        super(errorMessage);
    }
}

