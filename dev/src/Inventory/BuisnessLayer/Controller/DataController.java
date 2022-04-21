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
        addProducts();
        Collections.addAll(this.categories,new Category("Diary"),new Category("Milk"),new Category("Size"));
    }

    private void addProducts() {
        List<Category> categories1 = Arrays.asList(new Category("Wash"),new Category("Shampoo"),new Category("Weight"));
        List<Category> categories2 = Arrays.asList(new Category("Snacks"),new Category("Salty"),new Category("Gram"));
        this.categories.addAll(categories1);
        this.categories.addAll(categories2);
        this.productListMap.put(new Product(0,"Shampoo","Kef",10.20,12.50,categories1),new ArrayList<>());
        this.productListMap.put(new Product(1,"Chips","Osem",7.20,10.50,categories2),new ArrayList<>());

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
