package PresentationLayer;

import BusinessLayer.Contact;
import BusinessLayer.QuantityAgreement;
import BusinessLayer.Supplier;
import ServiceLayer.DummyObjects.DOrder;
import ServiceLayer.DummyObjects.DQuantityAgreement;
import ServiceLayer.DummyObjects.DSupplier;
import ServiceLayer.Response;
import ServiceLayer.SupplierFacade;

import java.util.*;

public class PresentationMain {
    SupplierFacade fSupplier;

    public PresentationMain(){
        fSupplier = new SupplierFacade();
    }

    public void main() {
        Boolean running = true;
        Scanner s = new Scanner(System.in);
        while(running){ //main program loop
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("(0 - Exit, 1 - Create supplier, 2 - Remove Supplier, 3 - Get Supplier, 4 - Add Supplier Delivery Day, 5 - Remove Supplier Delivery Day)");
            System.out.println("(6 - Update Supplier Delivery Day, 7 - Update Supplier Payment Details, 8 - Update Supplier Bank Account, 9 - Update Supplier Self Delivery)");
            System.out.println("(10 - Add Supplier Contact, 11 - Remove Supplier Contact, 12 - Update Contact Phone Number)");
            System.out.println("(13 - Make Order, 14 - Get Order, 15 - Get All Orders From Supplier, 16 - Load Test Data)");

            System.out.print("Enter command: ");
            String input = s.nextLine();

            switch (input){
                case ("0"): {
                    running = false;
                    s.close();
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
                    addSupplierDeliveryDay(s);
                    break;
                }
                case("5"): {
                    removeSupplierDeliveryDay(s);
                    break;
                }
                case("6"): {
                    updateSupplierDeliveryDays(s);
                    break;
                }
                case("7"): {
                    updateSupplierPaymentDetails(s);
                    break;
                }
                case("8"): {
                    updateSupplierBankAccount(s);
                    break;
                }
                case("9"): {
                    updateSupplierSelfDelivery(s);
                    break;
                }
                case("10"): {
                    addSupplierContact(s);
                    break;
                }
                case("11"): {
                    removeSupplierContact(s);
                    break;
                }
                case("12"): {
                    updateContactPhoneNumber(s);
                    break;
                }
                case("13"): {
                    makeOrder(s);
                    break;
                }
                case("14"): {
                    getOrder(s);
                    break;
                }
                case("15"): {
                    getAllOrdersFromSupplier(s);
                    break;
                }
                case("16"): {
                    loadData();
                    break;
                }



            }

        }
        System.out.println("Goodbye, come again!");



    }


    private void createSupplier(Scanner s){
        System.out.print("Enter supplier name: ");
        String supplierName = s.nextLine();
        int businessNumber =getIntFromUser(s,"business number");
        int bankNumber = getIntFromUser(s,"supplier bank account number");
        String paymentDetail = getPaymentFromUser(s);
        System.out.print("We need atleast one contact person.\nEnter contact name: ");
        String contactName = s.nextLine();
        System.out.print("Enter contact phone number: ");
        String contactNumber = s.nextLine();

        boolean selfDelivery = getBooleanFromUser(s,"Is the supplier doing self-delivery? (1 - yes, 2 - no, default - no): ");
        boolean deliveryDays = getBooleanFromUser(s,"Is the supplier delivering by days? (1 - yes, 2 - no, default - no): ");
        Set<Integer> daysToDeliver = deliveryDaysLoop(s);

        HashMap<Integer, Double> item_num_to_price = new HashMap<Integer, Double>();
        HashMap<Integer, String> item_num_to_name = new HashMap<Integer, String>();
        HashMap<Integer,HashMap<Integer,Integer>> item_Num_To_Discount = new HashMap<Integer,HashMap<Integer,Integer>>();
        createQuantityAgreement(s, item_num_to_price, item_num_to_name, item_Num_To_Discount);
        Response<DSupplier> newsupplier = fSupplier.addSupplier(supplierName,businessNumber,bankNumber,paymentDetail,contactName, contactNumber, item_num_to_price,item_Num_To_Discount , item_num_to_name, deliveryDays, selfDelivery, daysToDeliver);
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
        System.out.println("(If you selected 'No' for days to deliver - insert '0')");
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

    private void createQuantityAgreement(Scanner s, HashMap<Integer, Double> item_num_to_price, HashMap<Integer, String> item_num_to_name, HashMap<Integer,HashMap<Integer,Integer>> item_Num_To_Discount){
        System.out.println("Now we need to add the items that the supplier can supply.\nType item name and price. When you are done, type 'STOP' for the item name.");
        int itemID = 0;
        String itemName = "-1";
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
            System.out.print("Item price: ");
            itemPrice = s.nextLine();
            try{
                double priceNumber = Double.parseDouble(itemPrice);
                if(priceNumber<=0)
                    System.out.println("Illegal item price, try again.");
                else {
                    item_num_to_price.put(itemID, priceNumber);
                    item_num_to_name.put(itemID, itemName);
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
        for(int x:item_num_to_name.keySet()){
            System.out.println("Item ID: " + x + ", Item name: " + item_num_to_name.get(x) + ", Item price: " + item_num_to_price.get(x));
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
                if(itemIdNumber<0 || itemDiscountNumber>100 || itemDiscountNumber<0 || itemAmountNumber<=0 || itemIdNumber>=item_num_to_name.keySet().size())
                    System.out.println("Illegal item discount/price/ID, try again.");
                else{
                    if(!item_Num_To_Discount.containsKey(itemIdNumber))
                        item_Num_To_Discount.put(itemIdNumber, new HashMap<>());
                    item_Num_To_Discount.get(itemIdNumber).put(itemAmountNumber, itemDiscountNumber);

                }

            }
            catch (Exception E){
                System.out.println("Illegal item discount/price/ID, try again.");
            }
        }

    }

    private void makeOrder(Scanner s){
        String businessnumString;
        int businessNumber = getIntFromUser(s,"supplier business number");

        Response printItemsResponse = printSupplierItems(businessNumber);
        if(!printItemsResponse.isSuccess()){
            System.out.println(printItemsResponse.getMessage());
            return;
        }

        HashMap<Integer,Integer> order = new HashMap<>();
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
                if(itemIdNumber<0 || itemQuantity<0)
                    System.out.println("Illegal item ID/quantity, try again.");
                else{
                    order.put(itemIdNumber, itemQuantity);
                }

            }
            catch (Exception E){
                System.out.println("Illegal item ID/quantity, try again.");
            }
        }

        Response res = fSupplier.makeOrder(businessNumber, order);
        if(res.isSuccess())
            System.out.println("Order created successfully.");
        else System.out.println(res.getMessage());
    }

    private boolean legalNumberCheck(String input){
        int x = -1;
        try{
            x = Integer.parseInt(input);
        }
        catch(Exception e){}
        return x != -1;
    }

    private Response printSupplierItems(int businessNumber){
        Response<DQuantityAgreement> qa = fSupplier.getSupplierQuantityAgreement(businessNumber);
        if(qa.isSuccess()) {
            HashMap<Integer, String> item_Num_To_Name = qa.getData().getItem_Num_To_Name();
            HashMap<Integer, Double> item_Num_To_Price = qa.getData().getItem_Num_To_Price();
            for (int x : item_Num_To_Name.keySet()) {
                System.out.println("Item ID: " + x + ", Item name: " + item_Num_To_Name.get(x) + ", Item price: " + item_Num_To_Price.get(x));
            }
            return Response.makeSuccess(null);
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

        Response<DSupplier> newsupplier = fSupplier.removeSupplier(businessNumber);
        if(newsupplier.isSuccess())
            System.out.println("Supplier removed successfully.");
        else System.out.println(newsupplier.getMessage());

    }

    private void getSupplier(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        Response<DSupplier> newsupplier = fSupplier.getSupplier(businessNumber);
        if(newsupplier.isSuccess())
            System.out.println(newsupplier.getData().toString());
        else System.out.println(newsupplier.getMessage());
    }

    private void addSupplierDeliveryDay(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        System.out.println("Instructions: (1 - Sunday, 2 - Monday, 3 - Tuesday, 4 - Wednesday, 5 - Thursday, 6 - Friday, 7 - Saturday, 0 - Stop)");
        int dayNumber = getIntFromUser(s, "day");
        Response res = fSupplier.addSupplierDeliveryDay(businessNumber, dayNumber);
        if(res.isSuccess())
            System.out.println("Day added successfully.");
        else System.out.println(res.getMessage());
    }

    private void removeSupplierDeliveryDay(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        System.out.println("Select a day to remove.\nInstructions: (1 - Sunday, 2 - Monday, 3 - Tuesday, 4 - Wednesday, 5 - Thursday, 6 - Friday, 7 - Saturday, 0 - Stop)");
        int dayNumber = getIntFromUser(s, "day");
        Response res = fSupplier.removeSupplierDeliveryDay(businessNumber, dayNumber);
        if(res.isSuccess())
            System.out.println("Day removed successfully.");
        else System.out.println(res.getMessage());
    }

    private void updateSupplierDeliveryDays(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        Set<Integer> days = deliveryDaysLoop(s);
        Response res = fSupplier.updateSupplierDeliveryDays(businessNumber, days);
        if(res.isSuccess())
            System.out.println("Days updated successfully.");
        else System.out.println(res.getMessage());
    }

    private void updateSupplierPaymentDetails(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        String payment = getPaymentFromUser(s);
        Response res = fSupplier.updateSupplierPaymentDetails(businessNumber, payment);
        if(res.isSuccess())
            System.out.println("Supplier Payment Details have been updated successfully.");
        else System.out.println(res.getMessage());
    }

    private void updateSupplierBankAccount(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        int bankNumber = getIntFromUser(s, "supplier bank account number");
        Response res = fSupplier.updateSupplierBankAccount(businessNumber, bankNumber);
        if(res.isSuccess())
            System.out.println("Supplier bank account have been updated successfully.");
        else System.out.println(res.getMessage());
    }

    private void updateSupplierSelfDelivery(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        boolean selfDelivery = getBooleanFromUser(s, "Is the supplier doing self-delivery? (1 - yes, 2 - no, default - no): ");
        Response res = fSupplier.updateSupplierSelfDelivery(businessNumber, selfDelivery);
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
        Response res = fSupplier.addSupplierContact(businessNumber, contactName, contactPhone);
        if(res.isSuccess())
            System.out.println("Contact added successfully.");
        else System.out.println(res.getMessage());
    }

    private void removeSupplierContact(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        System.out.println("Enter contact phone number: ");
        String contactPhone = s.nextLine();
        Response res = fSupplier.removeSupplierContact(businessNumber, contactPhone);
        if(res.isSuccess())
            System.out.println("Contact removed successfully.");
        else System.out.println(res.getMessage());
    }

    private void getOrder(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        int contactPhone = getIntFromUser(s, "orderID");
        Response<DOrder> res = fSupplier.getOrder(businessNumber, contactPhone);
        if(res.isSuccess())
            System.out.println(res.getData().toString());
        else System.out.println(res.getMessage());
    }

    private void getAllOrdersFromSupplier(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        Response<List<DOrder>> res = fSupplier.getAllOrdersFromSupplier(businessNumber);
        if(res.isSuccess())
            System.out.println(printOrderList(res.getData()));
        else System.out.println(res.getMessage());
    }

    private void updateContactPhoneNumber(Scanner s) {
        int businessNumber = getIntFromUser(s, "supplier business number");
        System.out.print("Enter old phone number: ");
        String oldPhone = s.nextLine();
        System.out.print("Enter new phone number: ");
        String newPhone = s.nextLine();
        Response<List<DOrder>> res = fSupplier.updateContactPhoneNumber(businessNumber, oldPhone, newPhone);
        if(res.isSuccess())
            System.out.println(printOrderList(res.getData()));
        else System.out.println(res.getMessage());
    }



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
        HashMap<Integer, Double> item_Num_To_Price = new HashMap<>();
        item_Num_To_Price.put(0, 5.0);
        item_Num_To_Price.put(1, 20.0);
        HashMap<Integer,HashMap<Integer,Integer>> item_Num_To_Discount = new HashMap<>();
        HashMap<Integer,Integer> dis1 = new HashMap<>();
        dis1.put(200, 5);
        item_Num_To_Discount.put(0, dis1);
        HashMap<Integer,String> item_Num_To_Name = new HashMap<>();
        item_Num_To_Name.put(0, "milk");
        item_Num_To_Name.put(1, "beef");
        Set<Integer> daysSet = new HashSet<>();
        daysSet.add(1);
        daysSet.add(3);
        Response<DSupplier> newsupplier = fSupplier.addSupplier("Feliks Kablan",111111111,123456789,"credit","Ari", "05490090090", item_Num_To_Price, item_Num_To_Discount, item_Num_To_Name, true, true, daysSet);
        if(newsupplier.isSuccess())
            System.out.println("Supplier created successfully.");
        else System.out.println(newsupplier.getMessage());

        HashMap<Integer,Integer> orderMap = new HashMap<>();
        orderMap.put(0,100);
        Response<DOrder> neworder = fSupplier.makeOrder(111111111, orderMap);
        if(neworder.isSuccess())
            System.out.println("Order created successfully.");
        else System.out.println(neworder.getMessage());
    }


}
