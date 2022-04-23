package PresentationLayer;

import BusinessLayer.Delivery;
import BusinessLayer.Site;
import BusinessLayer.Truck;
import ServiceLayer.DeliveryService;
import ServiceLayer.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UserTerminal {
    private Scanner sc;
    private DeliveryService service;



    public UserTerminal(){
        sc = new Scanner(System.in).useDelimiter("\n");
        service = new DeliveryService();
    }
    public void run() {
        printWelcomeMessage();
        boolean isUserFinished = false;
        while (!isUserFinished){
            printMainMenuMessage();
            String userData = sc.next();
            switch (userData) {
                case "1":
                    runStiesMenu();
                    break;
                case "2":
                    runTrucksMenu();
                    break;
                case "3":
                    runDeliveriesMenu();
                    break;
                case "4":
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
        print("### Welcome to \"Super-Lee\" deliveries! ###");

    }
    private void printMainMenuMessage()
    {
        print("\n### Main Menu ###\n" +
                "please select menu" +
                "\n1 for sites" +
                "\n2 for trucks" +
                "\n3 for deliveries" +
                "\n4 to exit the program");
    }

    private void runStiesMenu() {
        boolean isUserFinished = false;
        while (!isUserFinished){
            printSiteMenuMessage();
            String userData = sc.next();
            switch (userData) {
                case "1":
                    runSiteCreation();
                    break;
                case "2":
                    runSiteEdit();
                    break;
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
                case "7":
                    isUserFinished = true;
                    break;
                default:
                    printIllegalOptionMessage();
            }
        }
    }

    private void runSiteCreation() {
        print("please pick up site type:" +
                "\n1 for a supplier delivery" +
                "\n2 for a branch");
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
        print("Enter site address:");
        String param1 = sc.next();

        int param2 = deliveryZoneSelection();

        print("Enter contact phone number");
        String param3 = sc.next();

        print("Enter up contact name:");
        String param4 = sc.next();

        if (isBranch)
            service.addBranch(param1,param2,param3,param4);
        else
            service.addSupplierWarehouse(param1,param2,param3,param4);
    }

    private void runSiteEdit() {
        Response<Site> res;
        int siteId = 0;
        boolean idLoaded=false;
        boolean isUserFinished = false;
        while (!isUserFinished){
            print("\n### Site Editing Menu ###");
            while (!idLoaded){
                printAllSites();
                print("Enter site id:");
                siteId = selectInt();
                res = service.getSite(siteId);
                if(res.isSuccess()){idLoaded=true;}
                else print(res.getMessage());
            }
            res=service.getSite(siteId);
            print("Editing site: "+res.getData());
            print("please pick up what you want to edit" +
                    "\n1 for site address" +
                    "\n2 for site delivery zone" +
                    "\n3 for contact's phone number" +
                    "\n4 for contact's name" +
                    "\n5 for stop editing");
            String userData = sc.next();
            switch (userData) {
                case "1":
                    print("Enter new address:");
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
                    print("Enter new contact phone number:");
                    Response r3 = service.editSitePhoneNumber(siteId,sc.next());
                    if (!r3.isSuccess())
                        print(r3.getMessage());
                    break;
                case "4":
                    print("Enter new contact name:");
                    Response r4 = service.editSiteContactName(siteId,sc.next());
                    if (!r4.isSuccess())
                        print(r4.getMessage());
                    break;
                case "5":
                    isUserFinished = true;
                default:
                    printIllegalOptionMessage();
            }
        }
    }

    private void printAllSites(){
        Response<Collection<Site>> res = service.getAllSites();
        print("Site List:");
        if (res.isSuccess())
            for (Site s: res.getData())
                print(s.toString());
        else
            print(res.getMessage());
        print("");
    }

    private void printSiteMenuMessage() {
        print("\n### Site Menu ###\n" +
                "please select site operation\n" +
                "1 for creating a new site\n" +
                "2 for editing a site\n" +
                "3 for deleting a site\n" +
                "4 to view a certain site\n" +
                "5 to view all sites in a certain delivery zone\n" +
                "6 to view all sites\n" +
                "7 to return to the main menu");
    }

    private void runTrucksMenu() {
        boolean isUserFinished = false;
        while (!isUserFinished){
            print("\n### Truck Menu ###\n" +
                    "please select truck operation\n" +
                    "1 for creating a new truck\n" +
                    "2 for editing a truck\n" +
                    "3 for deleting\n" +
                    "4 to view all trucks\n" +
                    "5 to return to the main menu");
            String userData = sc.next();
            switch (userData) {
                case "1":
                    runTruckCreation();
                    break;
                case "2":
                    runTruckEdit();
                    break;
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
                case "5":
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
        Response<String> res;
        int truckId = 0;
        boolean found = false;
        boolean isUserFinished = false;
        while (!isUserFinished){
            print("\n### Truck Editing Menu ###");
            while (!found){
                printAllTrucks();
                print("Enter truck id:");
                truckId = selectInt();
                res = service.getTruck(truckId);
                if(res.isSuccess()){found=true;}
                else print(res.getMessage());
            }
            res=service.getTruck(truckId);
            print("Editing truck: "+res.getData());
            print("please pick up what you want to edit\n" +
                    "1 for truck plate id\n" +
                    "2 for truck model\n" +
                    "3 for truck's max weight\n" +
                    "4 for stop editing");
            String userData = sc.next();
            switch (userData) {
                case "1":
                    print("Enter new plate number:");
                    int newPlateNum=selectInt();
                    Response r1 = service.editPlateNum(truckId,newPlateNum);
                    if (!r1.isSuccess())
                        print(r1.getMessage());
                    else truckId=newPlateNum;
                    break;
                case "2":
                    print("Enter new model:");
                    Response r2 = service.editModel(truckId,sc.next());
                    if (!r2.isSuccess())
                        print(r2.getMessage());
                    break;
                case "3":
                    print("Enter new max weight:");
                    Response r3 = service.editMaxWeight(truckId,selectInt());
                    if (!r3.isSuccess())
                        print(r3.getMessage());
                    break;
                case "4":
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
                    "\n\t3. Return to Main Menu");
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
                    "\n\t3. Return to Delivery Menu");
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
        boolean isUserFinished = false;
        String userData;
        while (!isUserFinished){
            print("\n### Upcoming Deliveries Menu ###\n" +
                    "Enter option number to execute the desirable operation:" +
                    "\n\t1. View all upcoming deliveries" +
                    "\n\t2. Search an upcoming delivery" +
                    "\n\t3. Add a new delivery" +
                    "\n\t4. Edit a delivery" +
                    "\n\t5. Delete an upcoming delivery" +
                    "\n\t6. Return to Delivery Menu");
            userData = sc.next();
            switch (userData) {
                case "1":
                    printAllUpcomingDeliveries();
                    break;
                case "2":
                    searchDelivery(false);
                    break;
                case "3":
                    printResponse(addDelivery());
                    break;
                case "4":
                    EditDelivery();
                    break;
                case "5":
                    printResponse(DeleteDelivery());
                    break;
                case "6":
                    isUserFinished = true;
                    break;
                default:
                    printIllegalOptionMessage();
            }
        }

    }

    private Response addDelivery() {
        System.out.print("Enter start time, ");
        LocalDateTime startDate=parseDate();
        System.out.print("Enter end time, ");
        LocalDateTime endDate=parseDate();
        printAllTrucks();
        print("Enter truck id:");
        int truckId=selectInt();
        print("Enter driver id:");
        int driverId=selectInt();
        printAllSites();
        print("Enter new origin id:");
        int originId=selectInt();
        return service.addDelivery(startDate,endDate,truckId,driverId,originId);
    }


    private void EditDelivery() {
        boolean isUserFinished = false;
        String userData;
        int deliveryId = 0;
        boolean idLoaded=false;
        Response<String> idRes;
        while (!isUserFinished){
            print("\n### Upcoming Delivery Editing Menu ###");
            while(!idLoaded){
                print("Enter delivery id:");
                deliveryId=selectInt();
                idRes=service.searchUpcomingDelivery(deliveryId);
                if(idRes.isSuccess()){idLoaded=true;}
                else print(idRes.getMessage());
            }
            idRes=service.searchUpcomingDelivery(deliveryId);
            print(String.format("\nEditing delivery: %s\n",idRes.getData()));
            print("Enter option number to execute the desirable operation:" +
                    "\n\t1. Add a destination" +
                    "\n\t2. Remove a destination" +
                    "\n\t3. Add an item to destination" +
                    "\n\t4. Remove an item from destination" +
                    "\n\t5. Edit item quantity" +
                    "\n\t6. Edit start Time" +
                    "\n\t7. Edit End Time" +
                    "\n\t8. Change driver" +
                    "\n\t9. Change truck" +
                    "\n\t10. Change origin site" +
                    "\n\t11. Edit truck's weight" +
                    "\n\t12. Complete the delivery" +
                    "\n\t13. Return to Upcoming Delivery Menu");

            userData = sc.next();
            switch (userData) {
                case "1":
                    printResponse(service.addDestinationToDelivery(deliveryId,chooseDest(service.getDestList().getData())));
                    break;
                case "2":
                    print("Enter destination id:");
                    printResponse(service.removeDestinationFromDelivery(deliveryId,selectInt()));
                    break;
                case "3":
                    printResponse(addItemToDest(deliveryId));
                    break;
                case "4":
                    printResponse(removeItemFromDest(deliveryId));
                    break;
                case "5":
                    printResponse(editQuantity(deliveryId));
                    break;
                case "6":
                    printResponse(editDate(deliveryId,true));
                    break;
                case "7":
                    printResponse(editDate(deliveryId,false));
                    break;
                case "8":
                    print("Enter new driver id:");
                    printResponse(service.editDeliveryDriver(deliveryId,selectInt()));
                    break;
                case "9":
                    printResponse(changeTruck(deliveryId));
                    break;
                case "10":
                    printAllSites();
                    print("Enter new origin id:");
                    printResponse(service.editDeliveryOrigin(deliveryId,selectInt()));
                    break;
                case "11":
                    print("Enter truck's weight:");
                    printResponse(service.editDeliveryWeight(deliveryId,selectInt()));
                    break;
                case "12":
                    printResponse(service.completeDelivery(deliveryId));
                    break;
                case "13":
                    isUserFinished = true;
                    break;
                default:
                    printIllegalOptionMessage();
            }
        }
    }

    private Response changeTruck(int deliveryId) {
        printAllTrucks();
        print("Enter new truck id:");
        return service.editDeliveryTruck(deliveryId,selectInt());
    }

    private Response editDate(int deliveryId,boolean start) {
            LocalDateTime date=parseDate();
            if(start) return service.editDeliveryStartTime(deliveryId,date);
            else return service.editDeliveryEndTime(deliveryId,date);
    }

    private LocalDateTime parseDate(){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        while (true){
            print("use the format <dd-MM-yyyy HH:mm>:");
            try {
                return LocalDateTime.parse(sc.next(),formatter);
            }catch (Exception e){
                System.out.print("Invalid date, ");
            }
        }
    }

    private Response editQuantity(int deliveryId) {
        print("Enter destination:");
        int destId=selectInt();
        print("Enter item:");
        String item=sc.next();
        print("Enter quantity:");
        int quantity=selectInt();
        return service.editDeliveryItemQuantity(deliveryId,destId,item,quantity);
    }

    private Response removeItemFromDest(int deliveryId) {
        print("Enter destination:");
        int destId=selectInt();
        print("Enter item:");
        String item=sc.next();
        return service.removeItemFromDeliveryDestination(deliveryId,destId,item);
    }

    private Response addItemToDest(int deliveryId) {
        print("Enter destination:");
        int destId=selectInt();
        print("Enter item:");
        String item=sc.next();
        print("Enter quantity:");
        int quantity=selectInt();
        return service.addItemToDeliveryDestination(deliveryId,destId,item,quantity);
    }

    private Response DeleteDelivery() {
        print("Enter delivery id:");
        return service.deleteDelivery(selectInt());

    }

    private void printAllUpcomingDeliveries() {
        Response<ArrayList<Delivery>> res=service.viewUpcomingDeliveries();
        ArrayList<Delivery>  upcomingDeliveries=res.getData();
        print("Upcoming Deliveries:");
        for(Delivery delivery:upcomingDeliveries){
            print(delivery.toString());
        }
    }

    private void printTruckById(int id){
        Response<ArrayList<Truck>> res = service.getTrucks();
        ArrayList<Truck> tl = res.getData();
        for (Truck t : tl)
            if (t.getPlateNum() == id)
                print(t.toString());
    }

    private void printAllTrucks(){
        Response<ArrayList<Truck>> res=service.getTrucks();
        ArrayList<Truck>  trucks=res.getData();
        print("Truck List:");
        for(Truck truck:trucks){
            print(truck.toString());
        }
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
        print("please pick a delivery zone:" +
                "\n0 for north" +
                "\n1 for center" +
                "\n2 for south");
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

    private void printResponse(Response res){
        if (res.isSuccess() && res.getData() instanceof String)
            print((String) res.getData());
        else
            print(res.getMessage());
    }

    private int chooseDest(ArrayList<String> sites){
        print("Destination List:");
        for(String dest:sites){
            print(dest);
        }
        print("Enter site id:");
        return selectInt();
    }

}