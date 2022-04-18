package Inventory.ServiceLayer;

import Inventory.BuisnessLayer.Controller.ReportController;
import Inventory.ServiceLayer.Objects.Report;

public class ReportService {

    private ReportController reportController;

    public ReportService() {
        this.reportController = new ReportController();
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
