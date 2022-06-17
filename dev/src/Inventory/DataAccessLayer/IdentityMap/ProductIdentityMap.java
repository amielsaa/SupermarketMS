package Inventory.DataAccessLayer.IdentityMap;

import Inventory.BuisnessLayer.Objects.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductIdentityMap {

    private List<Product> products;
    private boolean pulled_all_data = false;

    public ProductIdentityMap() {
        this.products = new ArrayList<>();
    }

    public void deleteAll() {
        products = new ArrayList<>();
        pulled_all_data = false;
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

    public boolean ExistsByName(String name, String producer) {
        return products.stream().anyMatch((s)->s.getName().equals(name) && s.getProducer().equals(producer));
    }

    public Product getProduct(int productid) {
        return products.stream().filter((p)->p.getId()==productid).findFirst().orElse(null);
    }

    public void deleteProduct(int productid) {
        products.removeIf((s)->s.getId()==productid);
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
