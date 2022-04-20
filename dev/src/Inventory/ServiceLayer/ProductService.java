package Inventory.ServiceLayer;

import Inventory.BuisnessLayer.Controller.CategoryController;
import Inventory.BuisnessLayer.Controller.DataController;
import Inventory.BuisnessLayer.Controller.ProductController;
import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.BuisnessLayer.Objects.StoreProduct;

import java.util.List;

public class ProductService {

    public ProductController productController;
    public CategoryController categoryController;

    public ProductService(DataController data) {
        this.productController = new ProductController(data);
        this.categoryController = new CategoryController(data);
    }

    public Response<Integer> SelectStore(int storeId) {
        try{
            productController.setStoreId(storeId);
            return Response.makeSuccess(storeId);
        } catch (Exception e) {
            return Response.makeFailure("Failed");
        }
    }

    public Response<String> GetAllProducts() {
        try {
            String products = productController.getAllProducts();
            return Response.makeSuccess(products);
        } catch(Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }


    public Response<String> AddProduct(String name, String producer, double buyingPrice,double sellingPrice, List<String> categories) {
        try{
            Product product = productController.addProduct(name,producer,buyingPrice,sellingPrice,categories);
            return Response.makeSuccess(product.toString());
        } catch(Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<String> AddStoreProduct(String prodName, String prodProducer, int quantityInStore, int quantityInWarehouse, String expDate, String locations ) {
        try{
            StoreProduct sp = productController.addStoreProduct(prodName,prodProducer,quantityInStore,quantityInWarehouse,expDate,locations);
            return Response.makeSuccess(sp.toString());
        }catch(Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Category> AddCategory(String category) {
        try{
            Category cat = categoryController.addCategory(category);
            return Response.makeSuccess(cat);
        } catch(Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }


    //TODO: change category

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
