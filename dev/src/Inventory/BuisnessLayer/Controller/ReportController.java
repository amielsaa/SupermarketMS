package Inventory.BuisnessLayer.Controller;

import Inventory.BuisnessLayer.Objects.Product;
import Inventory.BuisnessLayer.Objects.StoreProduct;
import Inventory.PresentationLayer.CommandLineTable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ReportController {

    private DataController data;

    public ReportController(DataController data) {
        this.data = data;
    }
    public String reportByCategory(String category){

        return null;
    }
    public CommandLineTable reportByExpired(){
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









    private String[] mergeArray(String[] arr1, String[] arr2) {
        return (String[])Stream.of(arr1, arr2).flatMap(Stream::of).toArray();
    }
}
