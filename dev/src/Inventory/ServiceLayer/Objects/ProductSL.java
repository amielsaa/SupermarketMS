package Inventory.ServiceLayer.Objects;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Location;

import java.util.Date;
import java.util.List;

public class ProductSL {
    private String Id;
    private String name;
    private String producer;
    private String buyingPrice;
    private String sellingPrice;
    private String discount;
    private String discountExpDate;
    private String categories;
    private String minQuantity;
    private String quantityInStore;
    private String quantityInWarehouse;
    private String expDate;
    private String locations;

    public ProductSL(int id, String name, String producer, double buyingPrice,
                     double sellingPrice, double discount, Date discountExpDate,
                     List<Category> categories, int minQuantity, int quantityInStore,
                     int quantityInWarehouse, Date expDate, List<Location> locations) {
        this.Id = String.valueOf(id);
        this.name = name;
        this.producer = producer;
        this.buyingPrice = String.valueOf(buyingPrice);
        this.sellingPrice = String.valueOf(sellingPrice);
        this.discount = String.valueOf(discount);
        this.discountExpDate = getDateAsString(discountExpDate);
        this.categories = getCategoriesAsString(categories);
        this.minQuantity = String.valueOf(minQuantity);
        this.quantityInStore = String.valueOf(quantityInStore);
        this.quantityInWarehouse = String.valueOf(quantityInWarehouse);
        this.expDate = getDateAsString(expDate);
        this.locations = getLocationsAsString(locations);
    }



    private String getLocationsAsString(List<Location> locations) {
        if(locations==null)
            return "Unknown";
        String acc = "";
        for(Location item : locations) {
            acc += item.toString() + "&";
        }
        return acc.substring(0,acc.length()-1);
    }

    private String getCategoriesAsString(List<Category> categories) {
        if(categories==null)
            return "Unknown";
        String acc = "";
        for(Category item : categories) {
            acc += item.getCategoryName() + ",";
        }
        return acc.substring(0,acc.length()-1);
    }

    private String getDateAsString(Date date) {
        if(date==null)
            return "Unknown";
        return "%d/%d/%d".formatted(date.getDate(),date.getMonth(),date.getYear()+1900);
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getProducer() {
        return producer;
    }

    public String getBuyingPrice() {
        return buyingPrice;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public String getDiscountExpDate() {
        return discountExpDate;
    }

    public String getCategories() {
        return categories;
    }

    public String getMinQuantity() {
        return minQuantity;
    }

    public String getQuantityInStore() {
        return quantityInStore;
    }

    public String getQuantityInWarehouse() {
        return quantityInWarehouse;
    }

    public String getExpDate() {
        return expDate;
    }

    public String getLocations() {
        return locations;
    }


}
