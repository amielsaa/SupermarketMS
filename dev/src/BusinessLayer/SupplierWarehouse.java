package BusinessLayer;

public class SupplierWarehouse extends Site{
    public SupplierWarehouse(int id, String address, int deliveryZone, String phoneNumber, String contactName) throws Exception {
        super(id, address, deliveryZone, phoneNumber, contactName);
    }

    @Override
    public boolean canBeADestination() {
        return false;
    }

    @Override
    public String toString() {
        return "supplier warehouse\n" + super.toString();
    }

    public String getDescriptionWithoutDeliveryZone() {
        return "supplier warehouse\n" + super.getDescriptionWithoutDeliveryZone();
    }
}
