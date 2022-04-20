package BusinessLayer;

public class Branch extends Site{
    public Branch(int id, String address, int deliveryZone, String phoneNumber, String contactName) throws Exception {
        super(id, address, deliveryZone, phoneNumber, contactName);
    }

    @Override
    public boolean canBeADestination() {
        return true;
    }

    @Override
    public String toString() {
        return "--branch--    " + super.toString();
    }

    @Override
    public String getDescriptionWithoutDeliveryZone() {
        return super.getDescriptionWithoutDeliveryZone();
    }
}
