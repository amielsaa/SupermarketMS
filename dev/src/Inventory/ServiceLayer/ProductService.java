package Inventory.ServiceLayer;

import Inventory.BuisnessLayer.Controller.CategoryController;
import Inventory.BuisnessLayer.Controller.ProductController;
import Inventory.BuisnessLayer.Objects.Category;
import Inventory.ServiceLayer.Objects.Product;

import java.util.List;

public class ProductService {

    private ProductController productController;
    private CategoryController categoryController;

    public ProductService() {
        this.productController = new ProductController();
        this.categoryController = new CategoryController();
    }

    public Response<String> AddProduct(String name, String producer, int price, List<String> categories) {
        return null;
    }

    public Response<Category> AddCategory(String category) {
        return null;
    }

    public Response<String> DeleteCategory(String category) {
        return null;
    }

    public Response<Product> AddDefectiveProduct(String name, String producer) {
        return null;
    }

    public Response<Product> DeleteProduct(String name, String producer) {
        return null;
    }

    public Response<String> AddDiscountByName(String name,String producer, int discount) {
        return null;
    }

    public Response<String> AddDiscountByCategory(String categoryName, int discount) {
        return null;
    }

    //TODO: disable discount

}
