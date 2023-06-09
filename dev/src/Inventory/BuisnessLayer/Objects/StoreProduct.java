package Inventory.BuisnessLayer.Objects;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    public boolean isNull() {
        return expDate == null;
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

    public boolean isExpired(){
        Date now = new Date();
        if(expDate.after(now))
            return false;
        else
            return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreProduct)) return false;
        StoreProduct that = (StoreProduct) o;
        return getStoreId() == that.getStoreId() &&
                Objects.equals(getExpDate(), that.getExpDate());
    }

    public String toString() {
        return String.format("%d : %d : %d/%d/%d : %s",quantityInStore,quantityInWarehouse,expDate.getDate(),expDate.getMonth()+1,expDate.getYear()+1900,getLocationsByString());
    }




}
