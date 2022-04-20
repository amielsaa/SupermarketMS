package Inventory.BuisnessLayer.Objects;

import java.util.Date;
import java.util.List;

public class StoreProduct {

    private int storeId;
    private int quantityInStore;
    private int quantityInWarehouse;
    private Date expDate;
    private List<Location> locations;

    public StoreProduct(int storeId, int quantityInStore, int quantityInWarehouse, Date expDate, List<Location> locations) {
        this.storeId = storeId;
        this.quantityInStore = quantityInStore;
        this.quantityInWarehouse = quantityInWarehouse;
        this.expDate = expDate;
        this.locations = locations;
    }

    private String getLocationsByString() {
        String res = "";
        for(Location loc : locations) {
            res+= loc.getLocationName() + "-" + loc.getAisle() + "-" + loc.getShelfNum() +"&";
        }
        return res.substring(0,res.length()-1);
    }

    public int getStoreId() {
        return storeId;
    }

    public int getQuantityInStore() {
        return quantityInStore;
    }

    public int getQuantityInWarehouse() {
        return quantityInWarehouse;
    }

    public Date getExpDate() {
        return expDate;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public String toString() {
        return "%d : %d : %d/%d/%d : %s".formatted(quantityInStore,quantityInWarehouse,expDate.getDay(),expDate.getMonth(),expDate.getYear(),getLocationsByString());
    }

}
