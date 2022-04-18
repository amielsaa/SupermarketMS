package Business.SimpleObjects;

public abstract class Site {
    private String address;
    private String deliveryZone;
    private String phoneNumber;
    private String contactName;
    private int id;
    public Site(int id, String address, String deliveryZone, String phoneNumber, String contactName) {
        this.address = address;
        this.deliveryZone = deliveryZone;
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

    public String getDeliveryZone() {
        return deliveryZone;
    }

    public void setDeliveryZone(String deliveryZone) {
        this.deliveryZone = deliveryZone;
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

    public abstract boolean canBeADestination();
}
