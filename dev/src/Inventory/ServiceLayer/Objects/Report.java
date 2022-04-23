package Inventory.ServiceLayer.Objects;

import Inventory.BuisnessLayer.Objects.CommandLineTable;

public class Report {
    private String headline;
    private CommandLineTable table;
    public Report(String headline, CommandLineTable table){
        this.headline = headline;
        this.table=table;
    }

    public String getHeadline() {
        return headline;
    }

    public CommandLineTable getTable() {
        return table;
    }
}
