package Business.SimpleObjects;

public class Store extends Site{
    public Store(int id, String address, String deliveryZone, String phoneNumber, String contactName) {
        super(id, address, deliveryZone, phoneNumber, contactName);
    }

    @Override
    public boolean canBeADestination() {
        return true;
    }
}
