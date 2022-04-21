package Inventory.ServiceLayer;

import Inventory.BuisnessLayer.Controller.DataController;
import Inventory.BuisnessLayer.Controller.ProductController;
import Inventory.BuisnessLayer.Controller.ReportController;
import Inventory.ServiceLayer.Objects.Report;

import java.util.List;

public class ReportService {

    private ReportController reportController;

    public ReportService(DataController data) {
        this.reportController = new ReportController(data);
    }

    public Response<Report> ReportByExpired() {
        try {
            Report report = new Report("Expired products report", reportController.reportByExpired());
            return Response.makeSuccess(report);
        } catch (Exception e ) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Report> ReportStockByCategories(List<String> categories) {
        try {
            Report report = new Report("Products by Categories report", reportController.reportByCategories(categories));
            return Response.makeSuccess(report);
        } catch (Exception e ) {
            return Response.makeFailure(e.getMessage());
        }
    }


    public Response<Report> ReportByDefective() {
        return null;
    }
    //TODO: periodical reports

}
