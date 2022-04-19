package Inventory.ServiceLayer;

import Inventory.BuisnessLayer.Controller.DataController;
import Inventory.BuisnessLayer.Controller.ProductController;
import Inventory.BuisnessLayer.Controller.ReportController;
import Inventory.ServiceLayer.Objects.Report;

public class ReportService {

    private ReportController reportController;

    public ReportService(DataController data) {
        this.reportController = new ReportController(data);
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
