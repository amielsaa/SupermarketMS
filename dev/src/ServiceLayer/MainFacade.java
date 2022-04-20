package ServiceLayer;

import BusinessLayer.Contact;
import BusinessLayer.QuantityAgreement;
import BusinessLayer.Supplier;
import ServiceLayer.DummyObjects.DSupplier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class MainFacade {
    SupplierFacade fSupplier;

    public MainFacade(){
        fSupplier = new SupplierFacade();
    }

    public void main() {
        Boolean running = true;
        Scanner s = new Scanner(System.in);
        while(running){ //main program loop
            System.out.print("Enter command (0 - Exit, 1 - Add supplier): ");
            String input = s.nextLine();

            switch (input){
                case ("0"): {
                    running = false;
                    s.close();
                }
                case ("1"): {
                    createSupplier(s);
                }
                case ("2"): {
                    makeOrder(s);
                }
            }

        }
        System.out.println("Goodbye, come again!");



    }

    private void createSupplier(Scanner s){
        System.out.print("Enter supplier name: ");
        String supplierName = s.nextLine();
        String businessNumberString;
        int businessNumber = -1;
        while(true){
            System.out.print("Enter supplier business number: ");
            businessNumberString = s.nextLine();
            if(legalNumberCheck(businessNumberString)) {
                businessNumber = Integer.parseInt(businessNumberString);
                break;
            }
            else System.out.println("Invalid business number");
        }

        String bankNumberString;
        int bankNumber = -1;
        while(true){
            System.out.print("Enter supplier bank account number: ");
            bankNumberString = s.nextLine();
            if(legalNumberCheck(bankNumberString)) {
                bankNumber = Integer.parseInt(bankNumberString);
                break;
            }
            else System.out.println("Invalid bank number");
        }


        System.out.print("Enter supplier payment option (1 - Credit, 2 - Cash, 3 - Plus30, 4 - Plus60, 5 - Check): ");
        String paymentDetail = s.nextLine();
        paymentDetail = paymentDetailNumberToString(paymentDetail);

        System.out.print("We need atleast one contact person.\nEnter contact name: ");
        String contactName = s.nextLine();
        String contactNumberString;
        int contactNumber = -1;
        while(true){
            System.out.print("Enter contact phone number: ");
            contactNumberString = s.nextLine();
            if(legalNumberCheck(contactNumberString)) {
                contactNumber = Integer.parseInt(contactNumberString);
                break;
            }
            else System.out.println("Invalid phone number");
        }


        System.out.print("Is the supplier doing self-delivery? (1 - yes, 2 - no, default - no): ");
        String selfDeliveryString = s.nextLine();
        boolean selfDelivery = stringToBoolean(selfDeliveryString);
        System.out.print("Is the supplier delivering by days? (1 - yes, 2 - no, default - no): ");
        String deliveryDaysString = s.nextLine();
        boolean deliveryDays = stringToBoolean(deliveryDaysString);
        Set<Integer> daysToDeliver = deliveryDaysLoop(s);


        HashMap<Integer, Integer> item_num_to_price = new HashMap<Integer, Integer>();
        HashMap<Integer, String> item_num_to_name = new HashMap<Integer, String>();
        HashMap<Integer,HashMap<Integer,Integer>> item_Num_To_Discount = new HashMap<Integer,HashMap<Integer,Integer>>();
        createQuantityAgreement(s, item_num_to_price, item_num_to_name, item_Num_To_Discount);
        Response<DSupplier> newsupplier = fSupplier.addSupplier(supplierName,businessNumber,bankNumber,paymentDetail,contactName, contactNumber, item_num_to_price, item_num_to_name, item_Num_To_Discount, deliveryDays, selfDelivery, daysToDeliver);
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
            catch (Exception E){ //todo: check double insertion
                System.out.println("Illegal day number, try again.");
            }
        }


        return days;
    }

    private void createQuantityAgreement(Scanner s, HashMap<Integer, Integer> item_num_to_price, HashMap<Integer, String> item_num_to_name, HashMap<Integer,HashMap<Integer,Integer>> item_Num_To_Discount){
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
                int priceNumber = Integer.parseInt(itemPrice);
                if(priceNumber<=0)
                    System.out.println("Illegal item price, try again.");
                else {
                    item_num_to_price.put(itemID, priceNumber);
                    item_num_to_name.put(itemID, itemName);
                    itemID++;
                }

            }
            catch (Exception E){ //todo: check double insertion
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
                    //todo: add checks
                    if(!item_Num_To_Discount.containsKey(itemIdNumber))
                        item_Num_To_Discount.put(itemIdNumber, new HashMap<>());
                    item_Num_To_Discount.get(itemIdNumber).put(itemDiscountNumber, itemAmountNumber); //todo: tell Ari about the order of nested hash - discount,amount

                }

            }
            catch (Exception E){
                System.out.println("Illegal item discount/price/ID, try again.");
            }
        }

    }

    private void makeOrder(Scanner s){
        //todo: collect info for func
        String businessnumString;
        int businessNumber = -1;
        while(true){
            System.out.print("Enter supplier BN: ");
            businessnumString = s.nextLine();
            if(legalNumberCheck(businessnumString)) {
                businessNumber = Integer.parseInt(businessnumString);
                break;
            }
            else System.out.println("Invalid business number");
        }
        if(businessNumber>=0) {
            printSupplierItems(businessNumber); //todo: create func that prints supplier's stash
            for (int x : item_num_to_name.keySet()) {
                System.out.println("Item ID: " + x + ", Item name: " + item_num_to_name.get(x) + ", Item price: " + item_num_to_price.get(x));
            }

        }


        Response res = fSupplier.makeOrder(businessnum, order);
    }

    private boolean legalNumberCheck(String input){
        int x = -1;
        try{
            x = Integer.parseInt(input);
        }
        catch(Exception e){}
        return x != -1;
    }
}
