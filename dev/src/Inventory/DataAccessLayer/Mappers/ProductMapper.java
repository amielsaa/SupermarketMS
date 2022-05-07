package Inventory.DataAccessLayer.Mappers;

import Inventory.BuisnessLayer.Objects.Product;
import Inventory.BuisnessLayer.Objects.StoreProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductMapper {

    private List<Product> products;

    public ProductMapper() {
        this.products = new ArrayList<>();
    }

    public Product addProduct(Product p) {
        products.removeIf((s)->s.getId()==p.getId());
        products.add(p);
        return p;
    }

}