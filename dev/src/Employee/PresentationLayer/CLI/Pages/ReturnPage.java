package Employee.PresentationLayer.CLI.Pages;

import Employee.ServiceLayer.Gateway;
import Utilities.Exceptions.CLIException;
import Utilities.ResponsePage;

import java.util.Scanner;

public class ReturnPage extends ResponsePage<Boolean>
{
    @Override
    public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
    {
        return false;
    }
}
