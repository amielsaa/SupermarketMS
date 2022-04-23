package BusinessLayer;

public abstract class Site {
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
        if(deliveryZone<0 ||deliveryZone>DeliveryZone.values().length)
            throw new Exception(deliveryZone + " is not an option, enter a valid one");
       return DeliveryZone.values()[deliveryZone];

    }

    public String toString(){
        return String.format("%s\n\t* Delivery Zone: %s",getDescriptionWithoutDeliveryZone(),deliveryZone.name());
    }
    public String getDescriptionWithoutDeliveryZone(){
        return String.format("Address: %s\n\t* Id: %d\n\t* Contact name: %s\n\t* Contact phone number: %s",address,id,contactName,phoneNumber);
    }

    public abstract boolean canBeADestination();
}
