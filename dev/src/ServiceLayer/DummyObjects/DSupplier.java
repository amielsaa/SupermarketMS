package ServiceLayer.DummyObjects;

import BusinessLayer.Contact;

import BusinessLayer.QuantityAgreement;
import BusinessLayer.Supplier;
import misc.Days;
import misc.PaymentDetails;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DSupplier {
    private String Name;
    private int Business_Num;
    private List<DContact> Contacts;
    private DQuantityAgreement Quantity_Agreement;
    private boolean Delivery_By_Days; //if the supplier Delivers by days or not
    private boolean Self_Delivery_Or_Pickup;// if we need to pick-up or he delivers us
    private Set<Days> Days_To_Deliver;
    //todo: edit


    public DSupplier(Supplier s) {
        Name = s.getName();
        Business_Num = s.getBusiness_Num();
        Delivery_By_Days = s.isDelivery_By_Days();
        Self_Delivery_Or_Pickup = s.isSelf_Delivery_Or_Pickup();
        Days_To_Deliver = s.getDays_To_Deliver();
        Contacts = new LinkedList<DContact>();
        for(Contact c : s.getContacts())
            Contacts.add(new DContact(c));
        Quantity_Agreement = new DQuantityAgreement(s.getQuantity_Agreement());
    }

    public String getName() {
        return Name;
    }

    public int getBusiness_Num() {
        return Business_Num;
    }

    public List<DContact> getContacts() {
        return Contacts;
    }

    public DQuantityAgreement getQuantity_Agreement() {
        return Quantity_Agreement;
    }

    public boolean isDelivery_By_Days() {
        return Delivery_By_Days;
    }

    public boolean isSelf_Delivery_Or_Pickup() {
        return Self_Delivery_Or_Pickup;
    }

    public Set<Days> getDays_To_Deliver() {
        return Days_To_Deliver;
    }

    public String toString(){
        return "Supplier name: " + getName() + " ,Business number: " + getBusiness_Num() +" ,Is self delivering: " + isSelf_Delivery_Or_Pickup() + "\nIs delivering by days: " +isDelivery_By_Days() + " ,Days to deliver: " + getDays_To_Deliver() + "\nContacts: " + getContacts().toString() + "\nQuantity Agreement: " + getQuantity_Agreement();
    }
}
