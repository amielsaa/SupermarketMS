package PresentationLayer.CLI.Pages;

import BusinessLayer.Employee;
import ServiceLayer.Gateway;
import Utilities.CLIException;

import java.util.Scanner;

import static Utilities.CLIException.fetchResponse;
import static Utilities.PrettyInput.printAndWaitForLegalInt;
import static Utilities.PrettyPrint.makeBigTitle;
import static Utilities.Util.checkLegalId;

public class LoginPage implements Page
{
    private final String WELCOME_TEXT = "\n" +
            "███████ ██    ██ ██████  ███████ ██████  ██      ███████ ███████ \n" +
            "██      ██    ██ ██   ██ ██      ██   ██ ██      ██      ██      \n" +
            "███████ ██    ██ ██████  █████   ██████  ██      █████   █████   \n" +
            "     ██ ██    ██ ██      ██      ██   ██ ██      ██      ██      \n" +
            "███████  ██████  ██      ███████ ██   ██ ███████ ███████ ███████ \n" +
            "   [Employees Module]                                      v 1.2";

    private final ResponsePage<Boolean> pgMenu = new MenuPage();

    private boolean isLoggedIn = false;
    private int loggedUID = -1;

    public void run(Scanner input, Gateway g) throws CLIException
    {
        if(isLoggedIn) {
            Employee employee = fetchResponse(g.getEmployee(loggedUID));
            System.out.println(makeBigTitle("WELCOME, " + employee.getName()));
            boolean stayLoggedIn = pgMenu.runWithResponse(input, g);

            if(!stayLoggedIn) {
                logout(g);
            }
        } else {
            System.out.println(WELCOME_TEXT);
            while(!isLoggedIn){
                int id = printAndWaitForLegalInt(input, "Please enter your id to enter (or -1 to exit): ", (x) -> (x == -1 || checkLegalId(x)), "Illegal id. ");
                if(id == -1) {
                    return;
                } else {
                    try {
                        login(g, id);

                    } catch (CLIException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        run(input, g);
    }

    public void login(Gateway g, int uid) throws CLIException {
        Employee e = fetchResponse(g.login(uid));
        loggedUID = e.getId();
        isLoggedIn = true;
    }

    public void logout(Gateway g) {
        g.logout();
        loggedUID = -1;
        isLoggedIn = false;
    }
}
