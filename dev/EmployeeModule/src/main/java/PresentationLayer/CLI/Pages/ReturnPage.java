package PresentationLayer.CLI.Pages;

import ServiceLayer.Gateway;
import Utilities.CLIException;

import java.util.Scanner;

public class ReturnPage extends ResponsePage<Boolean>
{
    @Override
    public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
    {
        return false;
    }
}