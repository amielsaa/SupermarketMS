package ServiceLayer;

import BusinessLayer.Contact;
import BusinessLayer.QuantityAgreement;
import BusinessLayer.Supplier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class MainFacade {
    SupplierFacade fSupplier;

    public MainFacade(){
        SupplierFacade fSupplier = new SupplierFacade();
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
//                    createSupplier(s); //todo: restore
                    createQuantityAgreement(s);
                }
            }

        }
        System.out.println("Goodbye, come again!");



    }

    private void createSupplier(Scanner s){
        //todo: String name, int business_num, int bank_acc_num, String payment_details, Contact contact, QuantityAgreement quantity_agreement, boolean delivery_by_days, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver
        System.out.print("Enter supplier name: ");
        String supplierName = s.nextLine();
        System.out.print("Enter supplier business number: ");
        String businessNumber = s.nextLine();
        System.out.print("Enter supplier bank account number: ");
        String bankNumber = s.nextLine();
        System.out.print("Enter supplier payment option (1 - Credit, 2 - Cash, 3 - Plus30, 4 - Plus60, 5 - Check): ");
        String paymentDetail = s.nextLine();
        paymentDetail = paymentDetailNumberToString(paymentDetail);

        System.out.print("We need atleast one contact person.\nEnter contact name: ");
        String contactName = s.nextLine();
        System.out.print("Enter contact phone number: ");
        String contactNumberString = s.nextLine();

        //todo: create legalNumberCheck function and apply it to every number----------------------------------------------------

        System.out.print("Is the supplier doing self-delivery? (1 - yes, 2 - no, default - no): ");
        String selfDeliveryString = s.nextLine();
        boolean selfDelivery = stringToBoolean(selfDeliveryString);
        System.out.print("Is the supplier delivering by days? (1 - yes, 2 - no, default - no): ");
        String deliveryDaysString = s.nextLine();
        boolean deliveryDays = stringToBoolean(deliveryDaysString);

        Set<Integer> daysToDeliver = deliveryDaysLoop(s);
        QuantityAgreement qa = createQuantityAgreement(s);
        Supplier newsupplier = fSupplier.addSupplier(supplierName,businessNumber,bankNumber,paymentDetail,new Contact(contactName, contactNumber), qa, deliveryDays, selfDelivery, daysToDeliver);


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

    private QuantityAgreement createQuantityAgreement(Scanner s){
        //todo: HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name
        HashMap<Integer, Integer> item_num_to_price = new HashMap<Integer, Integer>();
        HashMap<Integer, String> item_num_to_name = new HashMap<Integer, String>();
        HashMap<Integer,HashMap<Integer,Integer>> item_Num_To_Discount = new HashMap<Integer,HashMap<Integer,Integer>>();
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
        QuantityAgreement newagreement = fSupplier.createQuantityAgreement(item_num_to_price, item_Num_To_Discount, item_num_to_name);
        return newagreement;
    }

    private boolean legalNumberCheck(String input){

    }
}
