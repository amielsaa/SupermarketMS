package BusinessLayer;

import javax.print.attribute.SetOfIntegerSyntax;
import java.util.*;

enum Days {sunday,monday,tuesday,wednesday,thursday,friday,saturday}
enum PaymentDetails {credit,cash,plus30,plus60,check}

public class Supplier {
    private String Name;
    private int Business_Num;
    private int Bank_Acc_Num;
    private PaymentDetails Payment_Details;
    private List<Contact> Contacts;
    private QuantityAgreement Quantity_Agreement;
    private boolean Delivery_By_Days; //if the supplier Delivers by days or not
    private boolean Self_Delivery_Or_Pickup;// if we need to pick-up or he delivers us
    private Set<Days> Days_To_Deliver;


    public Supplier(){}//todo: remove this constructor - just for testing!

    public Supplier(String name, int business_num, int bank_acc_num, String payment_details, String contactName, int contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name, boolean delivery_by_days, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver) {
        Name = name;
        Business_Num = business_num;
        Bank_Acc_Num = bank_acc_num;
        Payment_Details =setPayment_Details(payment_details);
        Contacts = new LinkedList<Contact>();
        Contacts.add(new Contact(contactName, contactPhone));
        Delivery_By_Days = delivery_by_days;
        Self_Delivery_Or_Pickup = self_delivery_or_pickup;
        Quantity_Agreement = new QuantityAgreement(item_num_to_price, item_num_to_discount, item_num_to_name);
        if(!Delivery_By_Days)
            Days_To_Deliver = null;
        else
            Days_To_Deliver=setDays_To_Deliver(days_to_deliver);
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public boolean isDelivery_By_Days() {
        return Delivery_By_Days;
    }

    public boolean isSelf_Delivery_Or_Pickup() {
        return Self_Delivery_Or_Pickup;
    }

    public int getBank_Acc_Num() {
        return Bank_Acc_Num;
    }

    public int getBusiness_Num() {
        return Business_Num;
    }

    public List<Contact> getContacts() {
        return Contacts;
    }

    public PaymentDetails getPayment_Details() {
        return Payment_Details;
    }

    public QuantityAgreement getQuantity_Agreement() {
        return Quantity_Agreement;
    }

    public Set<Days> getDays_To_Deliver() {
        return Days_To_Deliver;
    }

    public void setBank_Acc_Num(int bank_Acc_Num) {
        Bank_Acc_Num = bank_Acc_Num;
    }

    public void setBusiness_Num(int business_Num) {
        Business_Num = business_Num;
    }

    public void setContacts(List<Contact> contacts) {
        Contacts = contacts;
    }

    public Set<Days> setDays_To_Deliver(Set<Integer> days_To_Deliver) {
        Set<Days> toreturn =new LinkedHashSet<Days>();
        if(days_To_Deliver.contains(1))
            toreturn.add(Days.sunday);
        if(days_To_Deliver.contains(2))
            toreturn.add(Days.monday);
        if(days_To_Deliver.contains(3))
            toreturn.add(Days.tuesday);
        if(days_To_Deliver.contains(4))
            toreturn.add(Days.wednesday);
        if(days_To_Deliver.contains(5))
            toreturn.add(Days.thursday);
        if(days_To_Deliver.contains(6))
            toreturn.add(Days.friday);
        if(days_To_Deliver.contains(7))
            toreturn.add(Days.saturday);

        return toreturn;
    }

    public void setDelivery_By_Days(boolean delivery_By_Days) {
        Delivery_By_Days = delivery_By_Days;
    }

    public PaymentDetails setPayment_Details(String payment_Details) {
        if(payment_Details.equals("credit"))
            return PaymentDetails.credit;
        else if(payment_Details.equals("cash"))
            return PaymentDetails.cash;
        else if(payment_Details.equals("plus30"))
            return PaymentDetails.plus30;
        else if(payment_Details.equals("plus60"))
            return PaymentDetails.plus60;
        else if(payment_Details.equals("check"))
            return PaymentDetails.check;
        else
            throw new IllegalArgumentException("Payment Method isnt valid");


    }

    public void setQuantity_Agreement(QuantityAgreement quantity_Agreement) {
        Quantity_Agreement = quantity_Agreement;
    }

    public void setSelf_Delivery_Or_Pickup(boolean self_Delivery_Or_Pickup) {
        Self_Delivery_Or_Pickup = self_Delivery_Or_Pickup;
    }

    public void addSupplierContact(String name, int contactPhone) {
        //todo
    }

    public void removeSupplierContact(int contactPhone) {
        //todo
    }


}
