package Utilities;

import Employee.ServiceLayer.Gateway;
import Utilities.Exceptions.CLIException;

import java.util.Scanner;

public interface Page
{
    public void run(Scanner input, Gateway g) throws CLIException;
}
