package DeliveryModule.PresentationLayer;

import DeliveryModule.BusinessLayer.Delivery;
import DeliveryModule.BusinessLayer.Site;
import DeliveryModule.BusinessLayer.Truck;
import DeliveryModule.ServiceLayer.DeliveryService;
import EmployeeModule.ServiceLayer.Gateway;
import Utilities.Exceptions.CLIException;
import Utilities.Response;
import Utilities.ResponsePage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class UserTerminal extends ResponsePage<Boolean>
{
    private Scanner sc;
    private DeliveryService service;
    public UserTerminal(){

    }

    //############################# Main Menu ###################################################
    @Override
    public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
    {
        sc = input;
        Response<DeliveryService> r1 = g.getDeliveryService();
        if(!r1.isSuccess()) {
            System.out.println("Failed to get delivery service. " + r1.getMessage());
            return true;
        }
        service = r1.getData();

        printWelcomeMessage();
        boolean isUserFinished = false;
        while (!isUserFinished){
            printMainMenuMessage();
            String userData = sc.next();
            switch (userData) {
                case "1":
                    runDeliveriesMenu();
                    break;
                case "2":
                    runSitesMenu();
                    break;
                case "3":
                    runTrucksMenu();
                    break;
                case "4":
                    isUserFinished = true;
                    break;
                default:
                    printIllegalOptionMessage();
            }
        }

        // This is for the parent page that openes this page, true indicates the system will not quit after calling this page
        return true;
    }

    private void printWelcomeMessage()
    { print("\n\n### Welcome to \"Super-Lee\" deliveries! ###");}
    private void printMainMenuMessage()
    {
        print("\n### Main Menu ###\n" +
                "Enter option number to execute the desirable operation:" +
                "\n\t1. Go to Delivery Menu" +
                "\n\t2. Go to Site menu" +
                "\n\t3. Go to Truck Menu" +
                "\n\t4. Exit");
    }


    //############################# Site Menu ###################################################
    private void runSitesMenu() {
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
                    print("Enter site id:");
                    printResponse(service.deleteSite(selectInt()));
                    break;
                case "4":
                    print("Enter site id:");
                    Response<Site> res=service.getSite(selectInt());
                    printResponse(res);
                    if(res.isSuccess()) print(res.getData().toString());
                    break;
                case "5":
                    printSiteByZone();
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
        print("Enter option number to choose site type:" +
                "\n\t1. Supplier Warehouse" +
                "\n\t2. Branch");
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
        print("Enter contact name:");
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
                print("Enter new site id:");
                siteId = selectInt();
                res = service.getSite(siteId);
                if(res.isSuccess()){idLoaded=true;}
                else print(res.getMessage());
            }
            res=service.getSite(siteId);
            print("Editing site: "+res.getData());
            print("Enter option number to execute the desirable operation:" +
                    "\n\t1. Edit address" +
                    "\n\t2. Edit delivery zone" +
                    "\n\t3. Edit contact phone number" +
                    "\n\t4. Edit contact name" +
                    "\n\t5. Return to Site Menu");
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

    private void printSiteByZone() {
        int zone=deliveryZoneSelection();
        Response<String> zoneName=service.getDeliveryZoneName(zone);
        if(zoneName.isSuccess()){
            print(String.format("\nSites of delivery zone: %s",zoneName.getData()));
            for(Site site:service.viewSitesPerZone(zone).getData()){
                print(site.toString());
            }
        }else printResponse(zoneName);
    }

    private void printAllSites(){
        Response<Collection<Site>> res = service.getAllSites();
        print("\nSite List:");
        if (res.isSuccess())
            for (Site s: res.getData())
                print(s.toString());
        else
            print(res.getMessage());
    }

    private void printSiteMenuMessage() {
        print("\n### Site Menu ###\n" +
                "Enter option number to execute the desirable operation:" +
                "\n\t1. Add a new site" +
                "\n\t2. Edit a site" +
                "\n\t3. Delete a site" +
                "\n\t4. Search a site" +
                "\n\t5. View all sites of a delivery zone" +
                "\n\t6. View all sites" +
                "\n\t7. Return to Main Menu");
    }

    private int deliveryZoneSelection()
    {
        while (true){
            print("Enter option number to choose a delivery zone:" +
                    "\n\t0. North" +
                    "\n\t1. Center" +
                    "\n\t2. South");
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

    //############################# Truck Menu ###################################################
    private void runTrucksMenu() {
        boolean isUserFinished = false;
        while (!isUserFinished){
            print("\n### Truck Menu ###\n" +
                    "Enter option number to execute the desirable operation:" +
                    "\n\t1. Add a new truck" +
                    "\n\t2. Edit a truck" +
                    "\n\t3. Delete a truck" +
                    "\n\t4. View all trucks" +
                    "\n\t5. Search a truck" +
                    "\n\t6. Return to Main Menu");
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
                    print("Enter plate number:");
                    printResponse(service.deleteTruck(selectInt()));
                    break;
                case "4":
                    printAllTrucks();
                    break;
                case "5":
                    print("Enter plate number:");
                    printTruckById(selectInt());
                    break;
                case "6":
                    isUserFinished = true;
                    break;
                default:
                    printIllegalOptionMessage();
            }
        }
    }

    private void runTruckCreation() {
        print("Enter plate number:");
        int param1 = selectInt();
        print("Enter model:");
        String param2 = sc.next();
        print("Enter max weight:");
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
                print("Enter truck plate number:");
                truckId = selectInt();
                res = service.getTruck(truckId);
                if(res.isSuccess()){found=true;}
                else print(res.getMessage());
            }
            res = service.getTruck(truckId);
            print("Editing truck: "+res.getData());
            print("Enter option number to execute the desirable operation:" +
                    "\n\t1. Edit plate number" +
                    "\n\t2. Edit model" +
                    "\n\t3. Edit max weight" +
                    "\n\t4. Return to Truck Menu");
            String userData = sc.next();
            switch (userData) {
                case "1":
                    print("Enter new plate number:");
                    int newPlateNum=selectInt();
                    Response r1 = service.editPlateNum(truckId,newPlateNum);
                    printResponse(r1);
                    if (r1.isSuccess())
                        truckId=newPlateNum;
                    break;
                case "2":
                    print("Enter new model:");
                    printResponse(service.editModel(truckId,sc.next()));
                    break;
                case "3":
                    print("Enter new max weight:");
                    printResponse(service.editMaxWeight(truckId,selectInt()));
                    break;
                case "4":
                    isUserFinished = true;
                default:
                    printIllegalOptionMessage();
            }
        }
    }

    private void printTruckById(int id){
        printResponse(service.getTruck(id));
    }

    private void printAllTrucks(){
        Response<ArrayList<Truck>> res=service.getTrucks();
        ArrayList<Truck>  trucks=res.getData();
        print("\nTruck List:");
        for(Truck truck:trucks){
            print(truck.toString());
        }
    }

    //############################# Delivery Menu ###################################################
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
            print(String.format("Editing delivery: %s",idRes.getData()));
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
                    printResponse(service.addDestinationToDelivery(deliveryId,chooseDest()));
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
                    printDriverList();
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
                    isUserFinished = printCompleteDelivery(service.completeDelivery(deliveryId));
                    break;
                case "13":
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
       printResponse(res);
    }

    private void printDeliveryArchive() {
        Response<ArrayList<String>> res=service.viewDeliveryArchive();
        ArrayList<String>  archive=res.getData();
        print("\nDelivery Archive:\n");
        for(String delivery:archive){
            print(delivery);
        }
    }

    private Response addDelivery() {
        Response found=null;
        int truckId = 0;
        int driverId=0;
        int originId=0;
        int destId=0;
        System.out.print("Enter start time, ");
        LocalDateTime startDate=parseDate();
        System.out.print("Enter end time, ");
        LocalDateTime endDate=parseDate();
        printAllTrucks();
        while (found==null) {
            print("Enter truck plate number:");
            truckId = selectInt();
            found=service.getTruck(truckId);
            if(!found.isSuccess()){
                print(found.getMessage());
                found=null;
            }
        }
        found=null;
        printDriverList();
        while (found==null) {
            print("Enter driver id:");
            driverId=selectInt();
            found=service.getDriver(driverId);
            if(!found.isSuccess()){
                print(found.getMessage());
                found=null;
            }
        }
        found=null;
        printAllSites();
        while (found==null) {
            print("Enter origin id:");
            originId=selectInt();
            found=service.getSite(originId);
            if(!found.isSuccess()){
                print(found.getMessage());
                found=null;
            }
        }
        destId=chooseDest();
        return service.addDelivery(startDate,endDate,truckId,driverId,originId,destId);
    }

    private void printDriverList(){
        print("\nDriver List:");
        for(String driver:service.getDriverList().getData()){
            print(driver);
        }
    }

    private Response changeTruck(int deliveryId) {
        printAllTrucks();
        print("Enter new truck id:");
        return service.editDeliveryTruck(deliveryId,selectInt());
    }

    private Response editDate(int deliveryId,boolean start) {
        if(start) System.out.print("Enter start time, ");
        else System.out.print("Enter end time, ");
        LocalDateTime date=parseDate();
        if(start) return service.editDeliveryStartTime(deliveryId,date);
        else return service.editDeliveryEndTime(deliveryId,date);
    }

    private Response editQuantity(int deliveryId) {
        print("Enter destination id:");
        int destId=selectInt();
        print("Enter item name:");
        String item=sc.next();
        print("Enter new quantity:");
        int quantity=selectInt();
        return service.editDeliveryItemQuantity(deliveryId,destId,item,quantity);
    }

    private Response removeItemFromDest(int deliveryId) {
        print("Enter destination id:");
        int destId=selectInt();
        print("Enter item name:");
        String item=sc.next();
        return service.removeItemFromDeliveryDestination(deliveryId,destId,item);
    }

    private Response addItemToDest(int deliveryId) {
        print("Enter destination id:");
        int destId=selectInt();
        print("Enter item name:");
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

    private int chooseDest(){
        Response<Site> found=null;
        int id=0;
        print("\nDestination List:");
        for(String dest:service.getDestList().getData()){
            print(dest);
        }while (found==null) {
            print("Enter destination's site id:");
            id = selectInt();
            found = service.getSite(id);
            if (!found.isSuccess()) {
                print(found.getMessage());
                found = null;
            }
        }
        return id;
    }

    private boolean printCompleteDelivery(Response res){
        if (res.isSuccess())
            return true;
        print(res.getMessage());
        return false;
    }

    //############################# Parsing & Printing ###################################################
    private void print(String message){System.out.println(message);}
    private void printIllegalOptionMessage() {print("You picked an illegal option, please try again");}
    private int selectInt()
    {
        while (true) {
            String input = sc.next();
            if (input.matches("-\\d+"))
                print("negative numbers are not allowed, please try again");
            else if (!input.matches("\\d+"))
                print("you entered a something that is not a number, please try again");
            else {
                try {
                    return Integer.parseInt(input);
                } catch (Exception e) {
                    print("your number is too big, please enter a smaller number");
                }
            }
        }
    }

    private LocalDateTime parseDate(){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        while (true){
            print("use the format <dd-MM-yyyy HH:mm>:");
            try {
                LocalDateTime dateTime=LocalDateTime.parse(sc.next(),formatter);
                if(dateTime.isBefore(LocalDateTime.now())){
                    throw new Exception("This date has passed, enter a new date and ");
                }
                return dateTime;
            }catch (Exception e){
                if(e instanceof DateTimeParseException)
                    System.out.print("Invalid date, ");
                else System.out.print(e.getMessage());
            }
        }
    }

    private void printResponse(Response res){
        if (res.isSuccess() && res.getData() instanceof String)
            print((String) res.getData());
        else
            print(res.getMessage());
    }
}