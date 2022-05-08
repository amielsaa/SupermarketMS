package Inventory.ServiceLayer;

import Inventory.BuisnessLayer.Controller.DataController;
import Inventory.BuisnessLayer.Controller.ProductController;
import Inventory.BuisnessLayer.Controller.ReportController;
import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.BuisnessLayer.Objects.StoreProduct;
import Inventory.ServiceLayer.Objects.Report;

import java.util.List;
import java.util.Map;

public class ReportService {

    private ReportController reportController;

    public ReportService(DataController data) {
        this.reportController = new ReportController(data);
    }

    public Response<Report> ReportByExpired(Map<Product,List<StoreProduct>> productListMap) {
        try {
            Report report = new Report("Expired products report", reportController.reportByExpired(productListMap));
            return Response.makeSuccess(report);
        } catch (Exception e ) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Report> ReportStockByCategories(Map<Product,List<StoreProduct>> productListMap, List<Category> categories) {
        try {
            Report report = new Report("Products by Categories report", reportController.reportByCategories(productListMap,categories));
            return Response.makeSuccess(report);
        } catch (Exception e ) {
            return Response.makeFailure(e.getMessage());
        }
    }


    public Response<Report> ReportByDefective() {
        try {
            Report report = new Report("Defective products report", reportController.reportByDefective());
            return Response.makeSuccess(report);
        } catch (Exception e ) {
            return Response.makeFailure(e.getMessage());
        }
    }


}
