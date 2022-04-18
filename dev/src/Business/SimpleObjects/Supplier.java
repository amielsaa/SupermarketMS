package Business.SimpleObjects;

public class Supplier extends Site {

    public Supplier(int id, String address, String deliveryZone, String phoneNumber, String contactName) {
        super(id, address, deliveryZone, phoneNumber, contactName);
    }

    @Override
    public boolean canBeADestination() {
        return true;
    }
}