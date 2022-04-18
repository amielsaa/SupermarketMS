package Inventory.ServiceLayer;

import Inventory.BuisnessLayer.Controller.ProductController;
import Inventory.BuisnessLayer.Controller.ReportController;
import Inventory.ServiceLayer.Objects.Report;

public class ReportService {

    private ReportController reportController;

    public ReportService(ProductController productController) {
        this.reportController = new ReportController(productController);
    }

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
