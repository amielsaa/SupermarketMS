package BusinessLayer;

public abstract class Site {

    protected enum DeliveryZone{North, Center, South}

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

    protected void setAddress(String address) {
        this.address = address;
    }

    public DeliveryZone getDeliveryZone() {
        return deliveryZone;
    }

    protected void setDeliveryZone(DeliveryZone deliveryZone) {
        this.deliveryZone = deliveryZone;
    }

    public void setDeliveryZone(int deliveryZone) throws Exception {
        this.deliveryZone = stringToDeliveryZone(deliveryZone);
    }

    protected String getPhoneNumber() {
        return phoneNumber;
    }

    protected void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactName() {
        return contactName;
    }

    protected void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public int getId() {
        return id;
    }

    protected static DeliveryZone stringToDeliveryZone(int deliveryZone) throws Exception {
        if (deliveryZone == 1)
            return DeliveryZone.North;
        if (deliveryZone == 2)
            return DeliveryZone.Center;
        if (deliveryZone == 3)
            return DeliveryZone.South;
        throw new Exception(deliveryZone + " is an illegal delivery zone, the please enter north center or south");
    }

    public String toString(){
        if (deliveryZone == DeliveryZone.North)
            return "delivery zone: north\n" + getDescriptionWithoutDeliveryZone();
        if (deliveryZone == DeliveryZone.Center)
            return "delivery zone: center\n" + getDescriptionWithoutDeliveryZone();
        return "delivery zone: south\n" + getDescriptionWithoutDeliveryZone();
    }
    public String getDescriptionWithoutDeliveryZone(){
        return "id: " + id + "\n" +
               "site address: " + address +"\n" +
               "the site contact is " + contactName + " (phone number: " + phoneNumber + ")";
    }


    public abstract boolean canBeADestination();
}
