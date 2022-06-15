package Suppliers.ServiceLayer.DummyObjects;

import java.time.LocalDate;
import java.util.Set;

public class PrepareDelivery {
    DOrder order;
    String address;
    Set<LocalDate> days;


    public PrepareDelivery(DOrder order,String address,Set<LocalDate> days){
        this.order=order;
        this.address=address;
        this.days=days;
    }

    public DOrder getOrder() {
        return order;
    }

    public String getAddress() {
        return address;
    }

    public Set<LocalDate> getDays() {
        return days;
    }
}
