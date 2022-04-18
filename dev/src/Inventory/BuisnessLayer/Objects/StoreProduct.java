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
}
