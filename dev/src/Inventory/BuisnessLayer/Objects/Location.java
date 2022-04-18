package Inventory.BuisnessLayer.Objects;

public class Location {

    enum Locations { STORE, WAREHOUSE}

    private Locations locationName;
    private int aisle;
    private int shelfNum;

    public Location(Locations locationName, int aisle, int shelfNum) {
        this.locationName = locationName;
        this.aisle = aisle;
        this.shelfNum = shelfNum;
    }
}
