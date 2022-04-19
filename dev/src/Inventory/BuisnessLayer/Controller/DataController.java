package Inventory.BuisnessLayer.Controller;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.BuisnessLayer.Objects.StoreProduct;

import java.util.*;

public class DataController {

    private List<Product> defectiveProducts;
    private List<Product> expiredProducts;
    private Map<Product,List<StoreProduct>> productListMap;
    private List<Category> categories;

    public DataController() {
        this.defectiveProducts = new ArrayList<>();
        this.expiredProducts = new ArrayList<>();
        this.productListMap = new HashMap<>();
        this.categories = new ArrayList<>();
        Collections.addAll(this.categories,new Category("Diary"),new Category("Milk"),new Category("Size")
        ,new Category("Shampoo"),new Category("Wash"));

    }

    public List<Product> getDefectiveProducts() {
        return defectiveProducts;
    }

    public List<Product> getExpiredProducts() {
        return expiredProducts;
    }

    public Map<Product, List<StoreProduct>> getProductListMap() {
        return productListMap;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
