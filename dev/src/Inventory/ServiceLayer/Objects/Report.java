package Inventory.ServiceLayer.Objects;

import Inventory.PresentationLayer.CommandLineTable;

public class Report {
    private String headline;
    private CommandLineTable table;
    public Report(String headline, CommandLineTable table){
        this.headline = headline;
        this.table=table;
    }
}
