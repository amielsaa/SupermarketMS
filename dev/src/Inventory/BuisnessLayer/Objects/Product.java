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


    public Product(int Id, String name, String producer,double buyingPrice, double sellingPrice, List<Category> categories) {
        this.Id = Id;
        this.name = name;
        this.producer = producer;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.discount = 0;
        this.discountExpDate = null;
        this.categories = categories;
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

    public int getId() {
        return Id;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public Date getDiscountExpDate() {
        return discountExpDate;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void removeCategory(int categoryIndex) {
        categories.remove(categoryIndex);
    }

    public void addCategory(Category category, int categoryIndex) {
        categories.add(categoryIndex,category);
    }

    public String toString() {
        return String.format("%s : %s : %.2f : %.2f : %s",name,producer,sellingPrice,buyingPrice, categoriesToString());
    }
    public String toArrayString(){
        return String.format("%d : %s : %s : %.2f : %.2f : %s",Id,name,producer,sellingPrice,buyingPrice, categoriesToString());
    }

    public void setDiscount(int discount, Date discountExpDate){
        this.discount = discount;
        this.discountExpDate = discountExpDate;
    }
    public void setMinQuantity(int minQuantity){
        this.minQuantity = minQuantity;
    }
}
