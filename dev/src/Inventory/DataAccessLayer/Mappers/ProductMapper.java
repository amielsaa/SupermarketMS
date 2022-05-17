package Inventory.DataAccessLayer.Mappers;

import Inventory.BuisnessLayer.Objects.Product;
import Inventory.BuisnessLayer.Objects.StoreProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductMapper {

    private List<Product> products;
    private boolean pulled_all_data = false;

    public ProductMapper() {
        this.products = new ArrayList<>();
    }

    public Product addProduct(Product p) {
        if(p==null)
            throw new IllegalArgumentException("Product not found.");
        Product cur = products.stream().filter((s)->s.getId()==p.getId()).findFirst().orElse(null);
        if(cur != null) {
            products.set(products.indexOf(cur),p);
        } else
            products.add(p);
        //products.removeIf((s)->s.getId()==p.getId());

        return p;
    }

    public Product getProduct(int productid) {
        return products.stream().filter((p)->p.getId()==productid).findFirst().orElse(null);
    }


    public boolean isPulled_all_data() {
        return pulled_all_data;
    }

    public void setPulled_all_data(boolean pulled_all_data) {
        this.pulled_all_data = pulled_all_data;
    }

    public List<Product> getProducts() {
        return products;
    }
}
