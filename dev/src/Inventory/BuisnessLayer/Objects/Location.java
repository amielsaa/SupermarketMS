package Inventory.BuisnessLayer.Objects;

public class Location {

    private String locationName;
    private int aisle;
    private int shelfNum;

    public Location(String locationName, int aisle, int shelfNum) {
        this.locationName = locationName;
        this.aisle = aisle;
        this.shelfNum = shelfNum;
    }
}
