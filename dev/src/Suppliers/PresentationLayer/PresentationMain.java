package Suppliers.PresentationLayer;

import SupplierInventory.SIService;
import Suppliers.ServiceLayer.DummyObjects.DOrder;
import Suppliers.ServiceLayer.DummyObjects.DQuantityAgreement;
import Suppliers.ServiceLayer.DummyObjects.DRoutineOrder;
import Suppliers.ServiceLayer.DummyObjects.DSupplier;
import Suppliers.ServiceLayer.Response;
import Suppliers.ServiceLayer.SupplierFacade;
import misc.Pair;

import java.util.*;

public class PresentationMain {

    SIService service;

    public PresentationMain(SIService s){
        service=s;
        service.SetStartingValues();
    }


    public void main() {
        Boolean running = true;
        Scanner s = new Scanner(System.in);

        
        outOfStock(s);


        while(running){ //main program loop
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("(0 - Exit, 1 - Create supplier, 2 - Remove Supplier, 3 - Get Supplier, 4 - Update Supplier Payment Details)");
            System.out.println("(5 - Update Supplier Bank Account, 6 - Update Supplier Self Delivery, 7 - Add Supplier Contact, 8 - Remove Supplier Contact)");
            System.out.println("(9 - Make Order, 10 - Make Routine Order, 11 - Get Order, 12 - Get All Orders From Supplier)");
            System.out.println("(13 - Get All Routine Orders, 14 - Get All Suppliers, 15 - Load Data, 16 - Update Tomorrow's Routine Order)");


            System.out.print("Enter command: ");
            String input = s.nextLine();

            switch (input){
                case ("0"): {
                    running = false;
//                    s.close();
                    break;
                }
                case ("1"): {
                    createSupplier(s);
                    break;
                }
                case ("2"): {
                    removeSupplier(s);
                    break;
                }
                case("3"): {
                    getSupplier(s);
                    break;
                }
                case("4"): {
                    updateSupplierPaymentDetails(s);
                    break;
                }
                case("5"): {
                    updateSupplierBankAccount(s);
                    break;
                }
                case("6"): {
                    updateSupplierSelfDelivery(s);
                    break;
                }
                case("7"): {
                    addSupplierContact(s);
                    break;
                }
                case("8"): {
                    removeSupplierContact(s);
                    break;
                }
                case("9"): {
                    makeOrder(s);
                    break;
                }
                case("10"): {
                    makeRoutineOrder(s);
                    break;
                }
                case("11"): {
                    getOrder(s);
                    break;
                }
                case("12"): {
                    getAllOrdersFromSupplier(s);
                    break;
                }
                case("13"): {
                    getAllRoutineOrders(s);
                    break;
                }
                case("14"): {
                    getAllSuppliers(s);
                    break;
                }
                case("15"): {
                    loadData();
                    break;
                }
                case("16"): {
                    outOfStock(s);
                    break;
                }




            }

        }
        System.out.println("Goodbye, come again!");



    }

    private void getAllSuppliers(Scanner s) {
        Response<List<DSupplier>> a = service.getAllSuppliers();
        for(DSupplier sup : a.getData()) {
            System.out.println(sup.toString());
            System.out.println("\n-------------------------");
        }
    }

    private void getAllRoutineOrders(Scanner s) {
        Response<List<DRoutineOrder>> a = service.getAllRoutineOrders();
        for(DRoutineOrder o : a.getData()) {
            System.out.println(o.toString());
            System.out.println("\n-------------------------");
        }
    }


    private void createSupplier(Scanner s){
        System.out.print("Enter supplier name: ");
        String supplierName = s.nextLine();
        int businessNumber =getIntFromUser(s,"business number");
        int bankNumber = getIntFromUser(s,"supplier bank account number");
        System.out.print("Enter supplier delivery zone: ");
        String deliveryzone = s.nextLine();
        System.out.print("Enter supplier address: ");
        String address = s.nextLine();
        String paymentDetail = getPaymentFromUser(s);
        System.out.print("We need atleast one contact person.\nEnter contact name: ");
        String contactName = s.nextLine();
        System.out.print("Enter contact phone number: ");
        String contactNumber = s.nextLine();
        boolean selfDelivery = getBooleanFromUser(s,"Is the supplier doing self-delivery? (1 - yes, 2 - no, default - no): ");
        Set<Integer> days = deliveryDaysLoop(s);
        HashMap<Pair<String,String>, Double> item_to_price = new HashMap<Pair<String,String>, Double>();
        HashMap<Pair<String,String>,HashMap<Integer,Integer>> item_Num_To_Discount = new HashMap<Pair<String,String>,HashMap<Integer,Integer>>();
        createQuantityAgreement(s, item_to_price, item_Num_To_Discount);
        Response<DSupplier> newsupplier = service.addSupplier(supplierName,businessNumber,bankNumber,paymentDetail, days, contactName, contactNumber, item_to_price, item_Num_To_Discount, selfDelivery, deliveryzone, address);
        if(newsupplier.isSuccess())
            System.out.println("Supplier created successfully.");
        else System.out.println(newsupplier.getMessage());
    }

    private String paymentDetailNumberToString(String input){
        switch (input){
            case("1"):return "credit";
            case("2"):return "cash";
            case("3"):return "plus30";
            case("4"):return "plus60";
            case("5"):return "check";
        }
        return "none";
    }

    private boolean stringToBoolean(String input){
        switch (input){
            case("1"):return true;
            case("2"):return false;
        }
        return false;
    }

    private Set<Integer> deliveryDaysLoop(Scanner s){
        Set<Integer> days = new HashSet<Integer>();
        System.out.println("Instructions: (1 - Sunday, 2 - Monday, 3 - Tuesday, 4 - Wednesday, 5 - Thursday, 6 - Friday, 7 - Saturday, 0 - Stop)");
        System.out.println("Type the number of the day you want to select and when you selected them all - type '0'");
        String day = "-1";
        boolean running = true;
        while(running) {
            System.out.print("Select delivery days: ");
            day = s.nextLine();
            try{
                int dayNumber = Integer.parseInt(day);
                if(dayNumber==0)
                    running = false;
                else if (dayNumber<8 && dayNumber>0) days.add(dayNumber);
                else System.out.println("Illegal day number, try again.");

            }
            catch (Exception E){
                System.out.println("Illegal day number, try again.");
            }
        }


        return days;
    }

    private void createQuantityAgreement(Scanner s, HashMap<Pair<String,String>, Double> item_to_price, HashMap<Pair<String,String>,HashMap<Integer,Integer>> item_To_Discount){
        System.out.println("Now we need to add the items that the supplier can supply.\nType item name, producer and price. When you are done, type 'STOP' for the item name.");
        int itemID = 0;
        String itemName = "-1";
        String itemProducer = "-1";
        String itemPrice = "-1";
        //------------------------------------Item name+price Loop------------------------------------
        //------------------------------------Item name+price Loop------------------------------------
        //------------------------------------Item name+price Loop------------------------------------
        boolean running = true;
        while(running) {
            System.out.print("Item name: ");
            itemName = s.nextLine();
            if(itemName.equals("STOP")) {
                running = false;
                break;
            }
            System.out.print("Item producer: ");
            itemProducer = s.nextLine();
            System.out.print("Item price: ");
            itemPrice = s.nextLine();
            try{
                double priceNumber = Double.parseDouble(itemPrice);
                if(priceNumber<=0)
                    System.out.println("Illegal item price, try again.");
                else {
                    item_to_price.put(new Pair<>(itemName, itemProducer), priceNumber);
                    itemID++;
                }

            }
            catch (Exception E){
                System.out.println("Illegal item name/price, try again.");
            }
        }
        //------------------------------------Discount Loop------------------------------------
        //------------------------------------Discount Loop------------------------------------
        //------------------------------------Discount Loop------------------------------------
        System.out.println("Now you can add discounts for items after certain amount.\nEnter item ID, then the discount in % and the amount for the discount.\nType 'STOP' in Item ID to stop adding discounts.");
        running = true;
        int index=0;
        HashMap<Integer, Pair<String,String>> itemsMap = new HashMap<>();
        for(Pair<String, String> x:item_to_price.keySet()){
            System.out.println("Item ID: " + index + ", Item name: " + x.getFirst() + ", Item producer: " + x.getSecond() + ", Item price: " + item_to_price.get(x));
            itemsMap.put(index, x);
            index++;
        }

        while(running) {
            System.out.print("Item ID: ");
            String itemIdString = s.nextLine();
            if(itemIdString.equals("STOP"))
            {
                running=false;
                break;
            }
            System.out.print("Item discount in %: ");
            String itemDiscount = s.nextLine();
            System.out.print("Discount applied after amount: ");
            String itemDiscountAmount = s.nextLine();
            try{
                int itemIdNumber = Integer.parseInt(itemIdString);
                int itemDiscountNumber = Integer.parseInt(itemDiscount);
                int itemAmountNumber = Integer.parseInt(itemDiscountAmount);
                if(itemIdNumber<0 || itemDiscountNumber>100 || itemDiscountNumber<0 || itemAmountNumber<=0 || itemIdNumber>=index)
                    System.out.println("Illegal item discount/price/ID, try again.");
                else{
                    Pair<String,String> currPair = itemsMap.get(itemIdNumber);
                    if(!item_To_Discount.containsKey(currPair))
                        item_To_Discount.put(currPair, new HashMap<Integer,Integer>());
                    item_To_Discount.get(currPair).put(itemAmountNumber, itemDiscountNumber);
                }

            }
            catch (Exception E){
                System.out.println("Illegal item discount/price/ID, try again.");
            }
        }

    }

    private Response<Pair<Integer, HashMap>> orderBaseInfo(Scanner s){
        int businessNumber = getIntFromUser(s,"supplier business number");

        Response<HashMap<Integer, Pair<String,String>>> printItemsResponse = printSupplierItems(businessNumber);
        if(!printItemsResponse.isSuccess()){
            return Response.makeFailure(printItemsResponse.getMessage());
        }

        HashMap<Pair<String,String>,Integer> order = new HashMap<>();
        System.out.println("Add items to the order. Type item ID and quantity of it.\nWhen you are done, type 'STOP' for the item name.");
        while(true) {
            System.out.print("Item ID: ");
            String itemIdString = s.nextLine();
            if(itemIdString.equals("STOP"))
            {
                break;
            }
            System.out.print("Item quantity: ");
            String itemQuantityString = s.nextLine();
            try{
                int itemIdNumber = Integer.parseInt(itemIdString);
                int itemQuantity = Integer.parseInt(itemQuantityString);
                if(itemIdNumber<0 || itemQuantity<0 || !printItemsResponse.getData().containsKey(itemIdNumber))
                    System.out.println("Illegal item name/producer/quantity, try again.");
                else{
                    order.put(printItemsResponse.getData().get(itemIdNumber), itemQuantity);
                }

            }
            catch (Exception E){
                System.out.println("Illegal item name/producer/quantity, try again.");
            }
        }
        return Response.makeSuccess(new Pair<>(businessNumber, order));

    }

    private void makeOrder(Scanner s){
        Response<Pair<Integer, HashMap>> baseInfo = orderBaseInfo(s);
        if(baseInfo.isSuccess()) {
            Response res = service.makeOrder(baseInfo.getData().getFirst(), baseInfo.getData().getSecond());
            if(res.isSuccess())
                System.out.println("Order created successfully.");
            else System.out.println(res.getMessage());
        }
        else System.out.println(baseInfo.getMessage());

    }

    private void makeRoutineOrder(Scanner s) {
        Response<Pair<Integer, HashMap>> baseInfo = orderBaseInfo(s);
        if(baseInfo.isSuccess()) {
            System.out.println("Select days for the routine order to be delivered.\nDays must be included in the supplier's delivery days.");
            Set<Integer> days = deliveryDaysLoop(s);
            Response res = service.makeRoutineOrder(baseInfo.getData().getFirst(), baseInfo.getData().getSecond(), days);
            if(res.isSuccess())
                System.out.println("Routine Order created successfully.");
            else System.out.println(res.getMessage());
        }
        else System.out.println(baseInfo.getMessage());

    }

    private boolean legalNumberCheck(String input){
        int x = -1;
        try{
            x = Integer.parseInt(input);
        }
        catch(Exception e){}
        return x != -1;
    }

    private Response<HashMap<Integer, Pair<String,String>>> printSupplierItems(int businessNumber){
        Response<DQuantityAgreement> qa = service.getSupplierQuantityAgreement(businessNumber);
        if(qa.isSuccess()) {
            int runningIndex=0;
            HashMap<Integer, Pair<String,String>> items = new HashMap<>();
            HashMap<Pair<String,String>, Double> item_Num_To_Price = qa.getData().getItem_Num_To_Price();
            for (Pair<String,String> x : item_Num_To_Price.keySet()) {
                System.out.println("#: " + runningIndex + ", Item name: " + x.getFirst() + ", Item producer: " + x.getSecond() + ", Item price: " + item_Num_To_Price.get(x));
                items.put(runningIndex,x);
                runningIndex++;
            }
            return Response.makeSuccess(items);
        }
        return Response.makeFailure("Supplier isn't found.");
    }

    private void removeSupplier(Scanner s) {
        int businessNumber = -1;
        String businessNumberString;
        while(true){
            System.out.print("Enter supplier BN: ");
            businessNumberString = s.nextLine();
            if(legalNumberCheck(businessNumberString)) {
                businessNumber = Integer.parseInt(businessNumberString);
                break;
            }
            else System.out.println("Invalid business number");
        }

        Response<DSupplier> newsupplier = service.removeSupplier(businessNumber);
        if(newsupplier.isSuccess())
            System.out.println("Supplier removed successfully.");
        else System.out.println(newsupplier.getMessage());

    }

    private void getSupplier(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        Response<DSupplier> newsupplier = service.getSupplier(businessNumber);
        if(newsupplier.isSuccess())
            System.out.println(newsupplier.getData().toString());
        else System.out.println(newsupplier.getMessage());
    }

//    private void addSupplierDeliveryDay(Scanner s) {
//        int businessNumber = getIntFromUser(s, "supplier business number");
//        System.out.println("Instructions: (1 - Sunday, 2 - Monday, 3 - Tuesday, 4 - Wednesday, 5 - Thursday, 6 - Friday, 7 - Saturday, 0 - Stop)");
//        int dayNumber = getIntFromUser(s, "day");
//        Response res = service.addSupplierDeliveryDay(businessNumber, dayNumber);
//        if(res.isSuccess())
//            System.out.println("Day added successfully.");
//        else System.out.println(res.getMessage());
//    }
//
//    private void removeSupplierDeliveryDay(Scanner s) {
//        int businessNumber = getIntFromUser(s, "supplier business number");
//        System.out.println("Select a day to remove.\nInstructions: (1 - Sunday, 2 - Monday, 3 - Tuesday, 4 - Wednesday, 5 - Thursday, 6 - Friday, 7 - Saturday, 0 - Stop)");
//        int dayNumber = getIntFromUser(s, "day");
//        Response res = service.removeSupplierDeliveryDay(businessNumber, dayNumber);
//        if(res.isSuccess())
//            System.out.println("Day removed successfully.");
//        else System.out.println(res.getMessage());
//    }
//
//    private void updateSupplierDeliveryDays(Scanner s) {
//        int businessNumber = getIntFromUser(s, "supplier business number");
//        Set<Integer> days = deliveryDaysLoop(s);
//        Response res = service.updateSupplierDeliveryDays(businessNumber, days);
//        if(res.isSuccess())
//            System.out.println("Days updated successfully.");
//        else System.out.println(res.getMessage());
//    }

    private void updateSupplierPaymentDetails(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        String payment = getPaymentFromUser(s);
        Response res = service.updateSupplierPaymentDetails(businessNumber, payment);
        if(res.isSuccess())
            System.out.println("Supplier Payment Details have been updated successfully.");
        else System.out.println(res.getMessage());
    }

    private void updateSupplierBankAccount(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        int bankNumber = getIntFromUser(s, "supplier bank account number");
        Response res = service.updateSupplierBankAccount(businessNumber, bankNumber);
        if(res.isSuccess())
            System.out.println("Supplier bank account have been updated successfully.");
        else System.out.println(res.getMessage());
    }

    private void updateSupplierSelfDelivery(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        boolean selfDelivery = getBooleanFromUser(s, "Is the supplier doing self-delivery? (1 - yes, 2 - no, default - no): ");
        Response res = service.updateSupplierSelfDelivery(businessNumber, selfDelivery);
        if(res.isSuccess())
            System.out.println("Supplier self-delivery preference has been updated successfully.");
        else System.out.println(res.getMessage());
    }

    private void addSupplierContact(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        System.out.println("Enter contact name: ");
        String contactName = s.nextLine();
        System.out.println("Enter contact phone number: ");
        String contactPhone = s.nextLine();
        Response res = service.addSupplierContact(businessNumber, contactName, contactPhone);
        if(res.isSuccess())
            System.out.println("Contact added successfully.");
        else System.out.println(res.getMessage());
    }

    private void removeSupplierContact(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        System.out.println("Enter contact phone number: ");
        String contactPhone = s.nextLine();
        Response res = service.removeSupplierContact(businessNumber, contactPhone);
        if(res.isSuccess())
            System.out.println("Contact removed successfully.");
        else System.out.println(res.getMessage());
    }

    private void getOrder(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        int contactPhone = getIntFromUser(s, "orderID");
        Response<DOrder> res = service.getOrder(businessNumber, contactPhone);
        if(res.isSuccess())
            System.out.println(res.getData().toString());
        else System.out.println(res.getMessage());
    }

    private void getAllOrdersFromSupplier(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        Response<List<DOrder>> res = service.getAllOrdersFromSupplier(businessNumber);
        if(res.isSuccess())
            System.out.println(printOrderList(res.getData()));
        else System.out.println(res.getMessage());
    }

    private void outOfStock(Scanner s) {
        Response<List<DRoutineOrder>> orders = service.getAllRoutineOrdersForTomorrow();
        if(orders.getData().isEmpty())
            System.out.println("No routine orders for tomorrow");
        else if(orders.isSuccess()){
            System.out.println("Routine orders for tomorrow:");
            int index = 0;
            HashMap<Integer,DRoutineOrder> mappingOrders = new HashMap<>();
            for(DRoutineOrder o : orders.getData()){
                System.out.println("Order " + index + ":\n" + o.toString());
                mappingOrders.put(index,o);
                index++;
            }
            System.out.println("Would you like to add/alter/remove items from any order?");
            System.out.println("1 - Add/alter, 2 - remove, 3 - no action (any other key - no action): ");
            int command = getIntFromUser(s,"command");
            switch (command){
                case (1): {
                    int ordernum = getIntFromUser(s,"order number");
                    while(!mappingOrders.containsKey(ordernum)) {
                        System.out.println("Order number doesn't exist.");
                        ordernum = getIntFromUser(s,"order number");
                    }
                    System.out.print("Enter item name: ");
                    String itemname = s.nextLine();
                    System.out.print("Enter item producer: ");
                    String itemproducer = s.nextLine();
                    int amount = getIntFromUser(s,"new item amount");
                    int orderID = mappingOrders.get(ordernum).getOrder_Id();
                    int bn = mappingOrders.get(ordernum).getSupplier_BN();
                    Response<DRoutineOrder> updatedOrder = service.addOrUpdateRoutineOrder(bn,orderID,itemname,itemproducer,amount);
                    if(updatedOrder.isSuccess()){
                        System.out.println("Update has been successful. The updated order:");
                        System.out.println(updatedOrder.getData().toString());
                    }
                    else System.out.println(updatedOrder.getMessage());
                    break;
                }
                case (2): {
                    int ordernum = getIntFromUser(s,"order number");
                    System.out.print("Enter item name: ");
                    String itemname = s.nextLine();
                    System.out.print("Enter item producer: ");
                    String itemproducer = s.nextLine();
                    int orderID = mappingOrders.get(ordernum).getOrder_Id();
                    int bn = mappingOrders.get(ordernum).getSupplier_BN();
                    Response<DRoutineOrder> updatedOrder = service.deleteItemFromRoutineOrder(bn,orderID,itemname,itemproducer);
                    if(updatedOrder.isSuccess()){
                        System.out.println("Item has been deleted successfully. The updated order:");
                        updatedOrder.getData().toString();
                    }
                    else System.out.println(updatedOrder.getMessage());
                    break;
                }
                default: {
                    break;
                }
            }

        }
//        else System.out.println("No routine orders for tomorrow.");
    }


//    private void updateContactPhoneNumber(Scanner s) {
//        int businessNumber = getIntFromUser(s, "supplier business number");
//        System.out.print("Enter old phone number: ");
//        String oldPhone = s.nextLine();
//        System.out.print("Enter new phone number: ");
//        String newPhone = s.nextLine();
//        Response<List<DOrder>> res = service.updateContactPhoneNumber(businessNumber, oldPhone, newPhone);
//        if(res.isSuccess())
//            System.out.println(printOrderList(res.getData()));
//        else System.out.println(res.getMessage());
//    }



    private String printOrderList(List<DOrder> orderList){
        String ret = "\n";
        for(DOrder x:orderList){
            ret += x.toString() + "\n-------------------------\n";
        }
        return ret;
    }

    private int getIntFromUser(Scanner s, String field){
        int intNumber = -1;
        String intNumberString;
        while(true){
            System.out.print("Enter " + field + ": ");
            intNumberString = s.nextLine();
            if(legalNumberCheck(intNumberString)) {
                intNumber = Integer.parseInt(intNumberString);
                break;
            }
            else System.out.println("Invalid " + field);
        }
        return intNumber;
    }

    private String getPaymentFromUser(Scanner s){
        System.out.print("Enter supplier payment option (1 - Credit, 2 - Cash, 3 - Plus30, 4 - Plus60, 5 - Check): ");
        String paymentDetail = s.nextLine();
        paymentDetail = paymentDetailNumberToString(paymentDetail);
        return paymentDetail;
    }

    private boolean getBooleanFromUser(Scanner s, String message){
        System.out.print(message);
        String someString = s.nextLine();
        boolean someBoolean = stringToBoolean(someString);
        return someBoolean;
    }

    private void loadData(){
        //This function has some business logic inside, but it's here only for the testers to have some info in the program
        //(that is why it's here and not in business or service)

        HashMap<Pair<String,String>,Double> item_Num_To_Name = new HashMap<>();
        Pair first = new Pair<>("Apple","Perot");
        Pair second = new Pair<>("Milk","Tnuva");
        item_Num_To_Name.put(first, 5.0);
        item_Num_To_Name.put(second, 20.0);
        HashMap<Pair<String,String>,HashMap<Integer,Integer>> item_Num_To_Discount = new HashMap<>();
        HashMap<Integer,Integer> dis1 = new HashMap<>();
        dis1.put(200, 5);
        item_Num_To_Discount.put(first, dis1);
        Set<Integer> daysSet = new HashSet<>();
        daysSet.add(1);
        daysSet.add(3);
        //String name, int business_num, int bank_acc_num, String payment_details,Set<Integer> days, String contactName, String contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, boolean self_delivery_or_pickup
        Response<DSupplier> newsupplier = service.addSupplier("Feliks Kablan",111111111,123456789,"credit",daysSet, "ari", "05490090090", item_Num_To_Name, item_Num_To_Discount, true, "South", "Rager 123");
        if(newsupplier.isSuccess())
            System.out.println("Supplier created successfully.");
        else System.out.println(newsupplier.getMessage());

        HashMap<Pair<String,String>,Integer> orderMap = new HashMap<>();
        orderMap.put(first,100);
        Response<DOrder> neworder = service.makeOrder(111111111, orderMap);
        if(neworder.isSuccess())
            System.out.println("Order created successfully.");
        else System.out.println(neworder.getMessage());
    }


}
