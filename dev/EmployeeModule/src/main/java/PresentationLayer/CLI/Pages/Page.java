package PresentationLayer.CLI.Pages;

import ServiceLayer.Gateway;
import Utilities.CLIException;

import java.util.Scanner;

public interface Page
{
    public void run(Scanner input, Gateway g) throws CLIException;
}
