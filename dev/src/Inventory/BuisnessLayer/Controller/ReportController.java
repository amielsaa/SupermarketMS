package Inventory.BuisnessLayer.Controller;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.BuisnessLayer.Objects.StoreProduct;
import Inventory.BuisnessLayer.Objects.CommandLineTable;
import Inventory.DataAccessLayer.DAO.ReportDAO;
import Inventory.ServiceLayer.Objects.Pair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportController {


    /**
     * this dao contains following functions:
     * SelectExpiredProducts()
     * SelectDefectiveProducts()
     * InsertDefectiveProducts(List<Integer> productIDs)
     * InsertExpiredProducts(List<Integer> productIDs)
     *
     */
    private ReportDAO reportDAO;

    public ReportController() {
        reportDAO = new ReportDAO("Reports");
    }

    //TODO: change return type to whatever
    public void addDefectiveProduct(int productId, int storeId) {
        reportDAO.InsertDefectiveProducts(productId,storeId);
    }

    //TODO: change return type to whatever <name & producer , quantity>
    // done.
    public Map<Pair<String,String>,Integer> reportByMinimumQuantity(Map<Product,List<StoreProduct>> productListMap) {
        Map<Pair<String, String>, Integer> report = new HashMap<>();
        for(Map.Entry<Product,List<StoreProduct>> mapSet: productListMap.entrySet()){
            int quantity =0;
            for(StoreProduct storeProduct: mapSet.getValue()){
                if(!storeProduct.isNull()&&!storeProduct.isExpired())
                    quantity+=storeProduct.getQuantityInStore()+storeProduct.getQuantityInWarehouse();
            }
            if(quantity<=mapSet.getKey().getMinQuantity()){
                int orderQuantity = mapSet.getKey().getMinQuantity()*7;
                Pair<String,String> product = new Pair(mapSet.getKey().getName(),mapSet.getKey().getProducer());
                report.put(product,orderQuantity);
            }
        }
        return report;
    }

    public CommandLineTable reportMinQuantityTable(Map<Product,List<StoreProduct>> productListMap) {
        CommandLineTable table = new CommandLineTable();
        table.setShowVerticalLines(true);
        table.setHeaders("Id","name", "producer" ,"selling price", "buyingPrice", "categories","quantity");

        for(Map.Entry<Product,List<StoreProduct>> mapSet: productListMap.entrySet()){
            int quantity =0;
            for(StoreProduct storeProduct: mapSet.getValue()){
                if(!storeProduct.isNull()&&!storeProduct.isExpired())
                    quantity+=storeProduct.getQuantityInStore()+storeProduct.getQuantityInWarehouse();
            }
            if(quantity<=mapSet.getKey().getMinQuantity()){
                int orderQuantity = mapSet.getKey().getMinQuantity();
                table.addRow(mergeArray(mapSet.getKey().toArrayString(),String.valueOf(orderQuantity)));
            }
        }
        return table;
    }

    //TODO:
    // done.
    public CommandLineTable reportByCategories(Map<Product,List<StoreProduct>> productListMap, List<Category> categories){

            CommandLineTable table = new CommandLineTable();
            table.setShowVerticalLines(true);
            table.setHeaders("Id","name", "producer" ,"selling price", "buyingPrice", "categories","quantity in store", "quantity in warehouse", "expiration date", "locations");
            for(Category cat: categories){
                for(Map.Entry<Product,List<StoreProduct>> mapSet: productListMap.entrySet())
                {
                    if(mapSet.getKey().getCategories().contains(cat)){
                        for (StoreProduct storeProduct: mapSet.getValue()) {
                            if(!storeProduct.isNull())
                                table.addRow(mergeArray(mapSet.getKey().toArrayString(),storeProduct.toString()));}
                    }
                }
           }
           return table;
    }


    public CommandLineTable reportByExpired(Map<Product,List<StoreProduct>> productListMap){

        CommandLineTable table = new CommandLineTable();
        table.setShowVerticalLines(true);
        table.setHeaders("Id", "name", "producer" ,"selling price", "buyingPrice", "categories","quantity in store", "quantity in warehouse", "expiration date", "locations");
        for(Map.Entry<Product,List<StoreProduct>> mapset: productListMap.entrySet())
        {
            for(StoreProduct storeProduct: mapset.getValue()){
                if(!storeProduct.isNull() && storeProduct.isExpired()){
                    table.addRow(mergeArray(mapset.getKey().toArrayString(),storeProduct.toString()));
                }
            }
        }

        return table;
    }


    public CommandLineTable reportByDefective(Map<Product,List<StoreProduct>> productListMap){
        List<Product> defList = reportDAO.SelectDefectiveProducts();
        CommandLineTable table = new CommandLineTable();
        table.setShowVerticalLines(true);
        table.setHeaders("Id", "name", "producer" ,"selling price", "buyingPrice", "categories");
        for(Product defective:defList)
        {
            table.addRow(defective.toArrayString().split("\\:"));
        }
        return table;
    }




    private String[] mergeArray(String arr1, String arr2) {
        String s = arr1+" : "+arr2;
        String[] array = s.split("\\:");
        return array;
    }
}
