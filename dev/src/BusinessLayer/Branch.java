package BusinessLayer;

public class Branch extends Site{
    public Branch(int id, String address, int deliveryZone, String phoneNumber, String contactName) throws Exception {
        super(id, address, deliveryZone, phoneNumber, contactName);
    }

    public Branch(int id, String address, DeliveryZone deliveryZone, String phoneNumber, String contactName) throws Exception {
        super(id, address, deliveryZone, phoneNumber, contactName);
    }

    @Override
    public boolean canBeADestination() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s\n\t* Type: Branch\n",super.toString());
    }

    @Override
    public String getType(){return "Branch";}

}
