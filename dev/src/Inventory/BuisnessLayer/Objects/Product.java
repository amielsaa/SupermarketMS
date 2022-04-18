package Inventory.BuisnessLayer.Objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Product {
    private String name;
    private String producer;
    private int buyingPrice;
    private int sellingPrice;
    private int discount;
    private Date discountExpDate;
    private List<Category> categories;
    private int minQuantity;

    public Product(String name, String producer, int sellingPrice,int buyingPrice, List<Category> categories) {
        this.name = name;
        this.producer = producer;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.discount = 0;
        this.discountExpDate = new Date();
        this.categories = categories;
        this.minQuantity = -1;
    }

    public Product(String name, String producer, int sellingPrice,int buyingPrice, int discount) {
        this.name = name;
        this.producer = producer;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
        this.categories = new ArrayList<>();
        this.minQuantity = -1;
    }

}
