package DeliveryModule.BusinessLayer;

public class SupplierWarehouse extends Site{
    public SupplierWarehouse(int id, String address, int deliveryZone, String phoneNumber, String contactName) throws Exception {
        super(id, address, deliveryZone, phoneNumber, contactName);
    }

    public SupplierWarehouse(int id, String address, DeliveryZone deliveryZone, String phoneNumber, String contactName) throws Exception {
        super(id, address, deliveryZone, phoneNumber, contactName);
    }

    @Override
    public boolean canBeADestination() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s\n\t* Type: Supplier Warehouse\n",super.toString());
    }

    @Override
    public String getType(){return "SupplierWarehouse";}
}
