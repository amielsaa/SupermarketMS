package Inventory.ServiceLayer;

import Inventory.BuisnessLayer.Controller.DataController;
import Inventory.BuisnessLayer.Objects.Category;
import Inventory.ServiceLayer.Objects.Pair;
import Inventory.ServiceLayer.Objects.ProductSL;
import Inventory.ServiceLayer.Objects.Report;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;

public class Service {

    private ProductService productService;
    private ReportService reportService;
    private DataController data;

    public Service() {
        this.data = new DataController();
        this.productService = new ProductService(data);
        this.reportService = new ReportService(data);
    }

    // product service

    public Response<Integer> SelectStore(int storeId) {
        return productService.SelectStore(storeId);
    }

    public Response<List<ProductSL>> GetAllProducts() {
        return productService.GetAllProducts();
    }

    public Response<String> AddProduct(String name, String producer, double buyingPrice,double sellingPrice, String categories) {
        return productService.AddProduct(name,producer,buyingPrice,sellingPrice,categories);
    }

    public Response<String> AddStoreProduct(int id, int quantityInStore, int quantityInWarehouse, String expDate, String locations) {
        return productService.AddStoreProduct(id,quantityInStore,quantityInWarehouse,expDate,locations);
    }

    public Response<Category> AddCategory(String category) {
        return productService.AddCategory(category);
    }

    public Response<String> ChangeCategory(int productId, int categoryIndex, String newCategory) {
        return productService.ChangeCategory(productId,categoryIndex,newCategory);
    }


    public Response<String> AddDefectiveProduct(int productId) {
        return productService.AddDefectiveProduct(productId);
    }

    public Response<String> DeleteProduct(int productId) {
        return productService.DeleteProduct(productId);
    }

    public Response<String> AddDiscountByName(String name,String producer, int discount,String date) {
        return productService.AddDiscountByName(name,producer,discount,date);
    }

    public Response<String> AddDiscountByCategory(String categoryName, int discount, String date) {
        return productService.AddDiscountByCategory(categoryName,discount,date);
    }


    //report service

//    public Response<Report> ReportByExpired() {
//        return reportService.ReportByExpired();
//    }
//
//    public Response<Report> ReportByDefective() {
//        return reportService.ReportByDefective();
//    }
//
//    public Response<Report> ReportStockByCategory(List<String> categories) {
//        return reportService.ReportStockByCategories(categories);
//    }
    public Response<String> stopTimer() {return productService.StopTimer();}

    //integration between suppliers

    public Response<String> MakeOrderMinQuantity() {

        //MakeOrderToSuppliers()
        throw new NotImplementedException();
    }

    public Response<String> MakeOrderToSuppliers(Map<Pair<String,String>,Integer> pairOfBalls) {
        throw new NotImplementedException();
    }





}
