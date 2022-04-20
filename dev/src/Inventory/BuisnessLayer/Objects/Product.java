package Inventory.BuisnessLayer.Objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Product {
    private int Id;
    private String name;
    private String producer;
    private double buyingPrice;
    private double sellingPrice;
    private double discount;
    private Date discountExpDate;
    private List<Category> categories;
    private int minQuantity;


    public Product(int Id, String name, String producer, double sellingPrice,double buyingPrice, List<Category> categories) {
        this.Id = Id;
        this.name = name;
        this.producer = producer;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.discount = 0;
        this.discountExpDate = new Date();
        this.categories = categories;
        this.minQuantity = -1;
    }

    public Product(String name, String producer, double sellingPrice,double buyingPrice, double discount) {
        this.name = name;
        this.producer = producer;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
        this.categories = new ArrayList<>();
        this.minQuantity = -1;
    }



    public String getName() {
        return name;
    }

    public String getProducer() {
        return producer;
    }

    private String categoriesToString() {
        String toString = "";
        for(Category cat : this.categories)
            toString+= cat.getCategoryName() + ",";
        toString = toString.substring(0,toString.length()-1);
        return toString;
    }

    public String toString() {
        return " %s : %s : %.2f : %.2f : %s".formatted(name,producer,sellingPrice,buyingPrice, categoriesToString());
    }

}
