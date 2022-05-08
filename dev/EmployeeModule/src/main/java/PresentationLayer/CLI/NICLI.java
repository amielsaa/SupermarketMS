package PresentationLayer.CLI;

import PresentationLayer.CLI.Pages.LoginPage;
import PresentationLayer.CLI.Pages.Page;
import ServiceLayer.Gateway;
import Utilities.CLIException;

import java.util.Scanner;

public class NICLI
{
    private final Page pgLogin = new LoginPage();

    private Scanner input;
    private Gateway gateway;

    public static void main(String[] args) {
        NICLI cli = new NICLI();
        cli.run();
    }

    public NICLI() {
        this.input = new Scanner(System.in);
        this.gateway = new Gateway();
    }

    private void run() {
        try {
            gateway.initDefaultData();
        } catch (Exception e) {
            System.out.println("WARNING! initialization of the default data resulted in an error. ");
        }
        try
        {
            pgLogin.run(input, gateway);
        } catch (CLIException e){
            System.out.println(e.getMessage());
        }
    }
}
