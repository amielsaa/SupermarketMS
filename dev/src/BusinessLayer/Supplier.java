package BusinessLayer;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

    public Supplier(String name, int business_num, int bank_acc_num, String payment_details, Contact contact, QuantityAgreement quantity_agreement, boolean delivery_by_days, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver) {
        Name = name;
        Business_Num = business_num;
        Bank_Acc_Num = bank_acc_num;
//        Payment_Details = payment_details;
        Contacts = new LinkedList<Contact>();
        Contacts.add(contact);
        Delivery_By_Days = delivery_by_days;
        Self_Delivery_Or_Pickup = self_delivery_or_pickup;
        Quantity_Agreement = quantity_agreement;
//        Days_To_Deliver = days_to_deliver;
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

    public void setDays_To_Deliver(Set<Integer> days_To_Deliver) {
        //todo
    }

    public void setDelivery_By_Days(boolean delivery_By_Days) {
        Delivery_By_Days = delivery_By_Days;
    }

    public void setPayment_Details(PaymentDetails payment_Details) {
        Payment_Details = payment_Details;
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
