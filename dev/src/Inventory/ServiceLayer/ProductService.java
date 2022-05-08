package Inventory.ServiceLayer;

import Inventory.BuisnessLayer.Controller.CategoryController;
import Inventory.BuisnessLayer.Controller.DataController;
import Inventory.BuisnessLayer.Controller.ProductController;
import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.BuisnessLayer.Objects.StoreProduct;
import Inventory.ServiceLayer.Objects.ProductSL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductService {

    public ProductController productController;
    public CategoryController categoryController;

    public ProductService(DataController data) {
        this.categoryController = new CategoryController(data);
        this.productController = new ProductController(data);
    }

    public Response<Integer> SelectStore(int storeId) {
        try{
            productController.setStoreId(storeId);
            return Response.makeSuccess(storeId);
        } catch (Exception e) {
            return Response.makeFailure("Failed");
        }
    }

    public Response<List<ProductSL>> GetAllProducts() {
        try {
            Map<Product,StoreProduct> products = productController.getAllProducts();
            List<ProductSL> slproducts = new ArrayList<>();
            for(Map.Entry<Product,StoreProduct> entry : products.entrySet()) {
                Product curProd = entry.getKey();
                StoreProduct curSP = entry.getValue();
                slproducts.add(new ProductSL(curProd.getId(),curProd.getName(),
                        curProd.getProducer(),curProd.getBuyingPrice(),
                        curProd.getSellingPrice(),curProd.getDiscount(),
                        curProd.getDiscountExpDate(),curProd.getCategories(),
                        curProd.getMinQuantity(),curSP.getQuantityInStore(),
                        curSP.getQuantityInWarehouse(),curSP.getExpDate(),
                        curSP.getLocations()));
            }
            return Response.makeSuccess(slproducts);
        } catch(Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }


    public Response<String> AddProduct(String name, String producer, double buyingPrice,double sellingPrice, String categories) {
        try{
            Response<List<Category>> categoriesRes = GetCategories(categories);
            if(!categoriesRes.isSuccess())
                return Response.makeFailure(categoriesRes.getMessage());
            Product product = productController.addProduct(name,producer,buyingPrice,sellingPrice,categoriesRes.getData());
            return Response.makeSuccess(product.toString());
        } catch(Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<String> AddStoreProduct(int id, int quantityInStore, int quantityInWarehouse, String expDate, String locations ) {
        try{
            StoreProduct sp = productController.addStoreProduct(id,quantityInStore,quantityInWarehouse,expDate,locations);
            return Response.makeSuccess(sp.toString());
        }catch(Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }


    //TODO: may need to change response type
    public Response<List<Category>> GetCategories(String categories) {
        try{
            return Response.makeSuccess(categoryController.getCategoriesByString(categories));
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

    public Response<String> ChangeCategory(int productId, int categoryIndex, String newCategory) {
        try{
            String resCategory = productController.changeCategory(productId,categoryIndex,categoryController.getCategory(newCategory));
            return Response.makeSuccess(resCategory);
        }catch(Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }


    public Response<String> AddDefectiveProduct(int productId) {
        try{
            Product product = productController.addDefectiveProduct(productId);
            return Response.makeSuccess(product.toString());
        }catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<String> DeleteProduct(int productId) {
        try{
            String res = productController.deleteProduct(productId);
            return Response.makeSuccess(res);
        }catch(Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<String> AddDiscountByName(int productId, int discount, String date) {
        try{
            String s = productController.addDiscountByName(productId,discount,date);
            return Response.makeSuccess(s.toString());
        }catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<String> AddDiscountByCategory(String categoryName, int discount,String date) {
        try{
            String s = productController.addDiscountByCategory(categoryName,discount,date);
            return Response.makeSuccess(s.toString());
        }catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<String> StopTimer(){
        try {
            String s = productController.turnOffTimer();
            return Response.makeSuccess(s.toString());
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

}
