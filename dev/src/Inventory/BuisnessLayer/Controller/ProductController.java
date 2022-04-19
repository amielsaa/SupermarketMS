package Inventory.BuisnessLayer.Controller;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.BuisnessLayer.Objects.StoreProduct;

import java.util.*;

public class ProductController {
    private List<Product> defectiveProducts;
    private List<Product> expiredProducts;
    private Map<Product,List<StoreProduct>> productListMap;
    private List<Category> categories;
    private int productId; //TODO: add to diagram

    public ProductController() {
        this.defectiveProducts = new ArrayList<>();
        this.expiredProducts = new ArrayList<>();
        this.productListMap = new HashMap<>();
        this.categories = new ArrayList<>();
        this.productId = 0;
        //adding categories for testing
        Collections.addAll(this.categories,new Category("Diary"),new Category("Milk"),new Category("Size"));

    }

    public Product addProduct(String name, String producer, int buyingPrice,int sellingPrice, List<String> categories) {
        //check for base conditions that satisfy the requirements - else throw exception
        boolean isCategoriesExists = categories.containsAll(categories);
        if(!isCategoriesExists)
            throw new IllegalArgumentException("Categories doesn't exists.");
        //TODO: capitalize all categories names
        List<Category> productCategories = getCategoriesByName(categories);
        Product product = new Product(productId++,name,producer,sellingPrice,buyingPrice,productCategories);
        //TODO: do we need to implement AddProduct() for StoreProduct or Product?
        //TODO: do we assume all products are already in the data base?
        productListMap.put(product,new ArrayList<>());
        return product;

    }

    private List<Category> getCategoriesByName(List<String> cat) {
        List<Category> categoryList = new ArrayList<>();
        for (String it: cat) {
            categoryList.add(this.categories.stream().filter(category-> it.equals(category.getCategoryName()))
                    .findFirst().orElse(null));
        }
        return categoryList;
    }






}
