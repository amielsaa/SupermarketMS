package Inventory.DataAccessLayer;

import Inventory.DataAccessLayer.DAO.ReportDAO;

public class TestingReport {
    public static void main(String[] args) {
        ReportDAO reportDAO = new ReportDAO("Reports");
    }
}
