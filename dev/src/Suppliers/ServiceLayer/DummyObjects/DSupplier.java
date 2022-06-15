package Suppliers.ServiceLayer.DummyObjects;

import Suppliers.BusinessLayer.Contact;

import Suppliers.BusinessLayer.Supplier;
import misc.Days;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DSupplier {
    private String Name;
    private int Business_Num;
    private List<DContact> Contacts;
    private DQuantityAgreement Quantity_Agreement;
    private Set<Days> Days;
    private String DeliveryZone, Address;

    private boolean Self_Delivery_Or_Pickup;// if we need to pick-up or he delivers us



    public DSupplier(Supplier s) {
        Name = s.getName();
        Business_Num = s.getBusiness_Num();
        Address = s.getAddress();
        DeliveryZone = s.getDeliveryZone();
        Self_Delivery_Or_Pickup = s.isSelf_Delivery_Or_Pickup();
        Days=s.getDays_To_Deliver();
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

    public String getDeliveryZone() {
        return DeliveryZone;
    }

    public String getAddress() {
        return Address;
    }

    public boolean isSelf_Delivery_Or_Pickup() {
        return Self_Delivery_Or_Pickup;
    }



    public String toString(){
        return "Supplier name: " + getName() + " ,Business number: " + getBusiness_Num()+ " ,Delivery Zone: " + getDeliveryZone() + " ,Address: " + getAddress() +" ,Is self delivering: " + isSelf_Delivery_Or_Pickup() + "\nContacts: " + getContacts().toString() + "\nQuantity Agreement: " + getQuantity_Agreement() + "\nDays of delivery: " + getDays();

    }

    public Set<misc.Days> getDays() {
        return Days;
    }
}
