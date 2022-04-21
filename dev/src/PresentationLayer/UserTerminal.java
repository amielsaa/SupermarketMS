package PresentationLayer;

import ServiceLayer.DeliveryService;

import java.util.Scanner;

public class UserTerminal {
    private Scanner sc;
    private DeliveryService service;
    private SiteCommandsHandler siteCommandsHandler;


    public UserTerminal(){
        sc = new Scanner(System.in).useDelimiter("\n");
        service = new DeliveryService(true);
        siteCommandsHandler = new SiteCommandsHandler(service);
    }
    public void run() {
        PrintWelcomeMessage();
        boolean isUserFinished = false;
        while (!isUserFinished){
            isUserFinished = executeUserCommand(service, sc);
        }
        sc.close();
    }


    private boolean executeUserCommand(DeliveryService service, Scanner sc) {
        String[] userData = sc.next().split(" ");
        if (userData.length == 0)
            return false;
        String userCommand = userData[0];
        if (userCommand.equals("exit"))
            return true;
        else if (userCommand.equals("help"))
            PrintHelpMessage();
        else if (siteCommandsHandler.isSiteCommand(userCommand))
            siteCommandsHandler.executeCommand(userData);
        return false;
    }

    private void PrintHelpMessage() {
        System.out.println("2. sites commands");
        System.out.println("");
        System.out.println("2.1: add_supplier <address> <delivery zone> <contact's phone number> <contact's name>");
        System.out.println("adds a new supplier");
    }

    public void PrintWelcomeMessage()
    {

    }
}
