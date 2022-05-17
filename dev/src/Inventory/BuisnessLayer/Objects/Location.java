package Inventory.BuisnessLayer.Objects;

public class Location {


    private Locations locationName;
    private int aisle;
    private int shelfNum;

    public Location(Locations locationName, int aisle, int shelfNum) {
        this.locationName = locationName;
        this.aisle = aisle;
        this.shelfNum = shelfNum;
    }

    public Locations getLocationName() {
        return locationName;
    }

    public int getAisle() {
        return aisle;
    }

    public int getShelfNum() {
        return shelfNum;
    }

    public String toString() {
        return locationName + "-" + aisle +"-"+shelfNum;
    }
}
