package Inventory.BuisnessLayer.Objects;

import java.util.Date;
import java.util.List;

public class StoreProduct {

    private String storeName;
    private int quantityInStore;
    private int quantityInWarehouse;
    private Date expDate;
    private List<Location> locations;

    public StoreProduct(String storeName, int quantityInStore, int quantityInWarehouse, Date expDate, List<Location> locations) {
        this.storeName = storeName;
        this.quantityInStore = quantityInStore;
        this.quantityInWarehouse = quantityInWarehouse;
        this.expDate = expDate;
        this.locations = locations;
    }
}
