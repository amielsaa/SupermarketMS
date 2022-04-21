package Inventory.BuisnessLayer.Controller;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.BuisnessLayer.Objects.StoreProduct;
import Inventory.PresentationLayer.CommandLineTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ReportController {

    private DataController data;

    public ReportController(DataController data) {
        this.data = data;
    }

    public CommandLineTable reportByCategories(List<String> categories){
        List<Category> catList = data.getCategoriesByName(categories);
        Map<Product, List<StoreProduct>> map = data.getProductListMap();
        CommandLineTable table = new CommandLineTable();
        table.setShowVerticalLines(true);
        table.setHeaders("name", "producer" ,"selling price", "buyingPrice", "categories","quantity in store", "quantity in warehouse", "expiration date", "locations");
        for(Category cat: catList){
            for(Map.Entry<Product,List<StoreProduct>> mapSet: map.entrySet())
            {
                if(mapSet.getKey().getCategories().contains(cat)){
                    for (StoreProduct storeProduct: mapSet.getValue()) {
                        table.addRow(mergeArray(mapSet.getKey().toArrayString(),storeProduct.toArrayString()));}
                }
            }
        }
        return table;
    }
    public CommandLineTable reportByExpired(){
        data.checkExpired();
        List<Product> expList = data.getExpiredProducts();
        Map<Product, List<StoreProduct>> map = data.getProductListMap();
        CommandLineTable table = new CommandLineTable();
        Date now = new Date();
        table.setShowVerticalLines(true);
        table.setHeaders("name", "producer" ,"selling price", "buyingPrice", "categories","quantity in store", "quantity in warehouse", "expiration date", "locations");
        for(Product product: expList){
            List<StoreProduct> prodList = map.get(product);
            for(StoreProduct storeProduct: prodList)
                if(!(storeProduct.getExpDate()).after(now)){
                    table.addRow(mergeArray(product.toArrayString(),storeProduct.toArrayString()));
                }
        }

        return table;
    }

    public CommandLineTable reportByDefective(){
        
        return null;
    }







    private String[] mergeArray(String[] arr1, String[] arr2) {
        return (String[])Stream.of(arr1, arr2).flatMap(Stream::of).toArray();
    }
}
