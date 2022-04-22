package PresentationLayer;

import BusinessLayer.Site;
import BusinessLayer.Truck;
import ServiceLayer.DeliveryService;
import ServiceLayer.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class UserTerminal {
    private Scanner sc;
    private DeliveryService service;
  //  private SiteCommandsHandler siteCommandsHandler;


    public UserTerminal(){
        sc = new Scanner(System.in).useDelimiter("\n");
        service = new DeliveryService();
//        siteCommandsHandler = new SiteCommandsHandler(service);
    }
    public void run() {
        printWelcomeMessage();
        boolean isUserFinished = false;
        while (!isUserFinished){
            printMainManuMessage();
            String userData = sc.next();
            switch (userData) {
                case "1":
                    runStiesManu();
                    break;
                case "2":
                    runTrucksManu();
                    break;
                case "3":
                    runDeliveriesMenu();
                    break;
                case "0":
                    isUserFinished = true;
                    break;
                default:
                    printIllegalOptionMessage();
            }
        }
        sc.close();
    }

    private void printWelcomeMessage()
    {
        print("Welcome to \"Super Lee\" deliveries!\n");

    }
    private void printMainManuMessage()
    {
        print("please select manu\n1 for sites\n2 for trucks\n3 for deliveries\n0 to exit the program\n");
    }

    private void runStiesManu() {
        boolean isUserFinished = false;
        while (!isUserFinished){
            printSiteManuMessage();
            String userData = sc.next();
            switch (userData) {
                case "1":
                    runSiteCreation();
                case "2":
                    runSiteEdit();
                case "3":
                    printAllSites();
                    print("please select site id that you want to delete");
                    Response res1 = service.deleteSite(selectInt());
                    if (!res1.isSuccess())
                        print(res1.getMessage());
                    break;
                case "4":
                    print("please select site id that you want to view");
                    Response<Site> res2 = service.getSite(selectInt());
                    if (res2.isSuccess())
                        print(res2.getData().toString());
                    else
                        print(res2.getMessage());
                    break;
                case "5":
                    Response<Collection<Site>> res3 = service.viewSitesPerZone(deliveryZoneSelection());
                    if (res3.isSuccess())
                        for (Site s: res3.getData())
                            print(s.toString());
                    else
                        print(res3.getMessage());
                    break;
                case "6":
                    printAllSites();
                    break;
                case "0":
                    isUserFinished = true;
                    break;
                default:
                    printIllegalOptionMessage();
            }
        }
    }

    private void runSiteCreation() {
        print("please pick up site type =\n1 for a supplier delivery\n2 for a branch");
        boolean isUserFinished = false;
        boolean isBranch = false;
        while (!isUserFinished){
            String userData = sc.next();
            switch (userData) {
                case "1":
                    isUserFinished = true;
                    break;
                case "2":
                    isBranch = true;
                    isUserFinished = true;
                    break;
                default:
                    printIllegalOptionMessage();
            }
        }
        print("please enter up site address");
        String param1 = sc.next();

        int param2 = deliveryZoneSelection();

        print("please enter up contact's phone number");
        String param3 = sc.next();

        print("please enter up contact's name");
        String param4 = sc.next();

        if (isBranch)
            service.addBranch(param1,param2,param3,param4);
        else
            service.addSupplierWarehouse(param1,param2,param3,param4);
    }

    private void runSiteEdit() {
        printAllSites();
        Response<Site> res;
        int siteId;
        do {
            print("please select site id that you want to edit");
            siteId = selectInt();
            res = service.getSite(siteId);
        }
        while (!res.isSuccess());

        boolean isUserFinished = false;
        while (!isUserFinished){
            print("please pick up what you want to edit\n1 for site address\n2 for site delivery zone\n3 for contact's phone number\n4 for contact's name\n0 for stop editing\n");
            print(service.getSite(siteId).getData().toString());
            String userData = sc.next();
            switch (userData) {
                case "1":
                    Response r1 = service.editSiteAddress(siteId,sc.next());
                    if (!r1.isSuccess())
                        print(r1.getMessage());
                    break;
                case "2":
                    Response r2 = service.editSiteDeliveryZone(siteId,deliveryZoneSelection());
                    if (!r2.isSuccess())
                        print(r2.getMessage());
                    break;
                case "3":
                    Response r3 = service.editSitePhoneNumber(siteId,sc.next());
                    if (!r3.isSuccess())
                        print(r3.getMessage());
                    break;
                case "4":
                    Response r4 = service.editSiteContactName(siteId,sc.next());
                    if (!r4.isSuccess())
                        print(r4.getMessage());
                    break;
                case "0":
                    isUserFinished = true;
                default:
                    printIllegalOptionMessage();
            }
        }
    }

    private void printAllSites(){
        Response<Collection<Site>> res = service.getAllSites();
        print("");
        if (res.isSuccess())
            for (Site s: res.getData())
                print(s.toString());
        else
            print(res.getMessage());
        print("");
    }

    private void printSiteManuMessage() {
        print("please select site operation\n1 for creating a new site\n2 for editing a site\n3 for deleting a site\n4 to view a certain site\n5 to view all sites in a certain delivery zone\n6 to view all sites\n0 to return to the main manu");
    }

    private void runTrucksManu() {
        boolean isUserFinished = false;
        while (!isUserFinished){
            print("please select truck operation\n1 for creating a new truck\n2 for editing a truck\n3 for deleting\n4 to view all trucks\n0 to return to the main manu\"");
            String userData = sc.next();
            switch (userData) {
                case "1":
                    runTruckCreation();
                case "2":
                    runTruckEdit();
                case "3":
                    printAllTrucks();
                    print("please select truck plate id that you want to delete");
                    Response res = service.deleteTruck(selectInt());
                    if (!res.isSuccess())
                        print(res.getMessage());
                    break;
                case "4":
                    printAllTrucks();
                    break;
                case "0":
                    isUserFinished = true;
                    break;
                default:
                    printIllegalOptionMessage();
            }
        }
    }

    private void runTruckCreation() {
        print("please select plate number");
        int param1 = selectInt();
        print("please select truck model");
        String param2 = sc.next();
        print("please select truck max weight");
        int param3 = selectInt();
        service.addTruck(param1,param2,param3);
    }

    private void runTruckEdit() {
        printAllTrucks();
        Response<ArrayList<Truck>> res;
        int truckId;
        boolean found = false;
        do {
            print("please select site id that you want to edit");
            truckId = selectInt();
            res = service.getTrucks();
            ArrayList<Truck> tl = res.getData();
            for (Truck t : tl)
                if (t.getPlateNum() == truckId) {
                    print(t.toString());
                    found = true;
                }

        }
        while (!found);

        boolean isUserFinished = false;
        while (!isUserFinished){
            print("please pick up what you want to edit\n1 for truck plate id\n2 for truck model\n3 for truck's max weight\n0 for stop editing\n");
            String userData = sc.next();
            printTruckById(truckId);
            switch (userData) {
                case "1":
                    Response r1 = service.editPlateNum(truckId,selectInt());
                    if (!r1.isSuccess())
                        print(r1.getMessage());
                    break;
                case "2":
                    Response r2 = service.editModel(truckId,sc.next());
                    if (!r2.isSuccess())
                        print(r2.getMessage());
                    break;
                case "3":
                    Response r3 = service.editMaxWeight(truckId,selectInt());
                    if (!r3.isSuccess())
                        print(r3.getMessage());
                    break;
                case "0":
                    isUserFinished = true;
                default:
                    printIllegalOptionMessage();
            }
        }
    }

    private void runDeliveriesMenu() {
        boolean isUserFinished = false;
        while (!isUserFinished){
            print("\n### Delivery Menu ###\n" +
                    "Enter option number to execute the desirable operation:" +
                    "\n\t1. Go to Upcoming Deliveries Menu" +
                    "\n\t2. Go to Completed Deliveries Menu" +
                    "\n\t3. Return to Main Menu\n");
            String userData = sc.next();
            switch (userData) {
                case "1":
                    runUpcomingDeliveriesMenu();
                    break;
                case "2":
                    runCompletedDeliveriesMenu();
                    break;
                case "3":
                    isUserFinished = true;
                    break;
                default:
                    printIllegalOptionMessage();
            }
        }
    }

    private void runCompletedDeliveriesMenu() {
        boolean isUserFinished = false;
        while (!isUserFinished){
            print("\n### Completed Deliveries Menu ###\n" +
                    "Enter option number to execute the desirable operation:" +
                    "\n\t1. View delivery archive" +
                    "\n\t2. Search a completed delivery" +
                    "\n\t3. Return to Delivery Menu\n");
            String userData = sc.next();
            switch (userData) {
                case "1":
                    printDeliveryArchive();
                    break;
                case "2":
                    searchDelivery(true);
                    break;
                case "3":
                    isUserFinished = true;
                    break;
                default:
                    printIllegalOptionMessage();
            }
        }
    }

    private void searchDelivery(boolean completed) {
        Response<String> res;
        print("Enter delivery id:");
        int id=selectInt();
        if(completed){
            res = service.searchCompletedDelivery(id);
        }else {
            res=service.searchUpcomingDelivery(id);
        }
        if (res.isSuccess())
            print(res.getData());
        else
            print(res.getMessage());
    }

    private void printDeliveryArchive() {
        Response<ArrayList<String>> res=service.viewDeliveryArchive();
        ArrayList<String>  archive=res.getData();
        print("\nDelivery Archive:\n");
        for(String delivery:archive){
            print(delivery);
        }
    }

    private void runUpcomingDeliveriesMenu() {

    }

    private void printTruckById(int id){
        Response<ArrayList<Truck>> res = service.getTrucks();
        ArrayList<Truck> tl = res.getData();
        for (Truck t : tl)
            if (t.getPlateNum() == id)
                print(t.toString());
    }

    private void printAllTrucks(){
        Response<ArrayList<Truck>> res = service.getTrucks();
        print("");
        if (res.isSuccess())
            for (Truck t: res.getData())
                print(t.toString());
        else
            print(res.getMessage());
        print("");
    }


    //shortcut for System.out.println
    private void print(String message)
    {
        System.out.println(message);
    }
    private void printIllegalOptionMessage() {
        print("you picked an illegal option, please try again\n");
    }

    private int deliveryZoneSelection()
    {
        print("please pick up delivery zone =\n0 for north\n1 for center\n2 for south");
        while (true){
            String userData = sc.next();
            switch (userData) {
                case "0":
                    return 0;
                case "1":
                    return 1;
                case "2":
                    return 2;
                default:
                    printIllegalOptionMessage();
            }
        }
    }

    private int selectInt()
    {
        while (true) {
            try {
                int res = Integer.parseInt(sc.next());
                return res;
            }
            catch (Exception e){
                printIllegalOptionMessage();
            }
        }
    }
}


/*package PresentationLayer;

import ServiceLayer.DeliveryService;

import java.util.Scanner;

public class UserTerminal {
    private Scanner sc;
    private DeliveryService service;
    private SiteCommandsHandler siteCommandsHandler;


    public UserTerminal(){
        sc = new Scanner(System.in).useDelimiter("\n");
        service = new DeliveryService();
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
}*/
