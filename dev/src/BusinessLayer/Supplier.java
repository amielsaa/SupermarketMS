package BusinessLayer;

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
    private QuantityAgrreement Quantity_Agrreement;
    private boolean Delivery_By_Days; //if the supplier Delivers by days or not
    private boolean Self_Delivery_Or_Pickup;// if we need to pick-up or he delivers us
    private Set<Days> Days_To_Deliver;

    public Supplier(String name, int business_num, int bank_acc_num, PaymentDetails payment_details, List<Contact> contacts, QuantityAgrreement quantity_agrreement, boolean delivery_by_days, boolean self_delivery_or_pickup, Set<Days> days_to_deliver) {
        Name = name;
        Business_Num = business_num;
        Bank_Acc_Num = bank_acc_num;
        Payment_Details = payment_details;
        Contacts = contacts;
        Quantity_Agrreement = quantity_agrreement;
        Delivery_By_Days = delivery_by_days;
        Self_Delivery_Or_Pickup = self_delivery_or_pickup;
        Days_To_Deliver = days_to_deliver;
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

    public QuantityAgrreement getQuantity_Agrreement() {
        return Quantity_Agrreement;
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

    public void setDays_To_Deliver(Set<Days> days_To_Deliver) {
        Days_To_Deliver = days_To_Deliver;
    }

    public void setDelivery_By_Days(boolean delivery_By_Days) {
        Delivery_By_Days = delivery_By_Days;
    }

    public void setPayment_Details(PaymentDetails payment_Details) {
        Payment_Details = payment_Details;
    }

    public void setQuantity_Agrreement(QuantityAgrreement quantity_Agrreement) {
        Quantity_Agrreement = quantity_Agrreement;
    }

    public void setSelf_Delivery_Or_Pickup(boolean self_Delivery_Or_Pickup) {
        Self_Delivery_Or_Pickup = self_Delivery_Or_Pickup;
    }
}
