package Inventory.ServiceLayer;

import Inventory.BuisnessLayer.Controller.DataController;
import Inventory.BuisnessLayer.Objects.Category;
import Inventory.ServiceLayer.Objects.ProductSL;
import Inventory.ServiceLayer.Objects.Report;

import java.util.List;

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
        return null;
    }

    public Response<String> DeleteCategory(String category) {
        return null;
    }

    public Response<ProductSL> AddDefectiveProduct(String name, String producer) {
        return null;
    }

    public Response<ProductSL> DeleteProduct(String name, String producer) {
        return null;
    }

    public Response<String> AddDiscountByName(String name,String producer, int discount) {
        return null;
    }

    public Response<String> AddDiscountByCategory(String categoryName, int discount) {
        return null;
    }

    //TODO: disable discount

    //report service

    public Response<Report> ReportByExpired() {
        return null;
    }

    public Response<Report> ReportByDefective() {
        return null;
    }

    public Response<Report> ReportStockByCategory() {
        return null;
    }

    //TODO: periodical reports




}
