package Inventory.ServiceLayer;

import Inventory.BuisnessLayer.Controller.DataController;
import Inventory.BuisnessLayer.Objects.Category;
import Inventory.ServiceLayer.Objects.Product;
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
