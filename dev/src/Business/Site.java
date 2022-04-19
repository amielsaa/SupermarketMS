package Business;

public abstract class Site {

    public enum DeliveryZone{North, Center, South}

    private String address;
    private DeliveryZone deliveryZone;
    private String phoneNumber;
    private String contactName;
    private int id;
    public Site(int id, String address, int deliveryZone, String phoneNumber, String contactName) throws Exception {
        this.address = address;
        this.deliveryZone = stringToDeliveryZone(deliveryZone);
        this.phoneNumber = phoneNumber;
        this.contactName = contactName;
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public DeliveryZone getDeliveryZone() {
        return deliveryZone;
    }

    public void setDeliveryZone(DeliveryZone deliveryZone) {
        this.deliveryZone = deliveryZone;
    }

    public void setDeliveryZone(int deliveryZone) throws Exception {
        this.deliveryZone = stringToDeliveryZone(deliveryZone);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public int getId() {
        return id;
    }

    public static DeliveryZone stringToDeliveryZone(int deliveryZone) throws Exception {
        if (deliveryZone == 1)
            return DeliveryZone.North;
        if (deliveryZone == 2)
            return DeliveryZone.Center;
        if (deliveryZone == 3)
            return DeliveryZone.South;
        throw new Exception(deliveryZone + " is an illegal delivery zone, the please enter north center or south");
    }

    public abstract boolean canBeADestination();
}
