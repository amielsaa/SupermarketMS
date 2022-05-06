package Inventory.BuisnessLayer.Controller;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.BuisnessLayer.Objects.StoreProduct;
import Inventory.BuisnessLayer.Objects.CommandLineTable;
import Inventory.DataAccessLayer.ReportDAO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportController {

    //private DataController data;

    /**
     * this dao contains following functions:
     * SelectExpiredProducts()
     * SelectDefectiveProducts()
     * InsertDefectiveProducts(List<Integer> productIDs)
     * InsertExpiredProducts(List<Integer> productIDs)
     *
     */
    private ReportDAO reportDAO;

    public ReportController(DataController data) {
        //this.data = data;
        reportDAO = new ReportDAO("Report");
    }

    //TODO: change return type to whatever
    public void addDefectiveProduct(int productId) {
        throw new NotImplementedException();
    }

    //TODO: change return type to whatever
    public void reportByMinimumQuantity(Map<Product,List<StoreProduct>> productListMap) {
        throw new NotImplementedException();
    }

    //TODO:
    public CommandLineTable reportByCategories(Map<Product,List<StoreProduct>> productListMap, List<Category> categories){
//        List<Category> catList = data.getCategoriesByName(categories);
//        Map<Product, List<StoreProduct>> map = data.getProductListMap();
//        CommandLineTable table = new CommandLineTable();
//        table.setShowVerticalLines(true);
//        table.setHeaders("Id","name", "producer" ,"selling price", "buyingPrice", "categories","quantity in store", "quantity in warehouse", "expiration date", "locations");
//        for(Category cat: catList){
//            for(Map.Entry<Product,List<StoreProduct>> mapSet: map.entrySet())
//            {
//                if(mapSet.getKey().getCategories().contains(cat)){
//                    for (StoreProduct storeProduct: mapSet.getValue()) {
//                        table.addRow(mergeArray(mapSet.getKey().toArrayString(),storeProduct.toString()));}
//                }
//            }
//        }
//        return table;
        throw new NotImplementedException();
    }

    //TODO:
    public CommandLineTable reportByExpired(Map<Product,List<StoreProduct>> productListMap){
//        data.checkExpired();
//        List<Product> expList = data.getExpiredProducts();
//        Map<Product, List<StoreProduct>> map = data.getProductListMap();
//        CommandLineTable table = new CommandLineTable();
//        Date now = new Date();
//        table.setShowVerticalLines(true);
//        table.setHeaders("Id", "name", "producer" ,"selling price", "buyingPrice", "categories","quantity in store", "quantity in warehouse", "expiration date", "locations");
//        for(Product product: expList){
//            List<StoreProduct> prodList = map.get(product);
//            for(StoreProduct storeProduct: prodList)
//                if(!(storeProduct.getExpDate()).after(now)){
//                    table.addRow(mergeArray(product.toArrayString(),storeProduct.toString()));
//                }
//        }
//        return table;
        throw new NotImplementedException();
    }

    //TODO:
    public CommandLineTable reportByDefective(){
//        List<Product> defList = data.getDefectiveProducts();
//        CommandLineTable table = new CommandLineTable();
//        table.setShowVerticalLines(true);
//        table.setHeaders("Id", "name", "producer" ,"selling price", "buyingPrice", "categories");
//        for(Product defective:defList)
//        {
//            table.addRow(defective.toArrayString().split("\\:"));
//        }
//        return table;
        throw new IllegalArgumentException();
    }




    private String[] mergeArray(String arr1, String arr2) {
        String s = arr1+" : "+arr2;
        String[] array = s.split("\\:");
        return array;
    }
}
