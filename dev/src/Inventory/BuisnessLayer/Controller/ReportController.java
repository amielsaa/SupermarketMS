package Inventory.BuisnessLayer.Controller;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.BuisnessLayer.Objects.StoreProduct;
import Inventory.BuisnessLayer.Objects.CommandLineTable;
import Inventory.DataAccessLayer.DAO.PendingDAO;
import Inventory.DataAccessLayer.DAO.ReportDAO;
import misc.Pair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

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
    private PendingDAO pendingDAO;
    private int storeId;

    public ReportController() {
        reportDAO = new ReportDAO("Reports");
        pendingDAO = new PendingDAO("Pending");
    }
    public void setStoreId(int storeId){this.storeId =storeId;}
    public void addDefectiveProduct(int productId, int storeId) {
        reportDAO.InsertDefectiveProducts(productId,storeId);
    }

    //after we insert all products (accepting delivery) -> delete all pendings
    public void deleteAllPendingProducts() {
        pendingDAO.DeleteAllPending();
    }

    public void removeDefectiveFromPending(String name, String producer, int defectiveQuantity) {
        if(!pendingDAO.DeleteDefectiveProduct(name,producer,defectiveQuantity))
            throw new IllegalArgumentException("Delete defective pending product failed.");
    }

    public Map<Pair<String,String>,Pair<Integer,Integer>> getPendingMap() {
        return pendingDAO.SelectAll();
    }

    public void receiveDelivery(Map<Pair<String,String>,Pair<Integer,Integer>> delivery) {
        if(delivery==null)
            throw new IllegalArgumentException("delivery can't be empty");
        for (Map.Entry <Pair<String,String>,Pair<Integer,Integer>> product:delivery.entrySet() ) {
            String name = product.getKey().getFirst(); String producer = product.getKey().getSecond(); int price =product.getValue().getFirst(); int quantity = product.getValue().getSecond();
            pendingDAO.InsertPending(name,producer,price,quantity);
        }
    }

    public CommandLineTable reportPending() {
        CommandLineTable table = new CommandLineTable();
        table.setShowVerticalLines(true);
        table.setHeaders("name", "producer" ,"buyingPrice","quantity", "totalPrice");
        Map <Pair<String,String>,Pair<Integer,Integer>> pending = getPendingMap();
        for (Map.Entry <Pair<String,String>,Pair<Integer,Integer>> product:pending.entrySet()){
            String name = product.getKey().getFirst(); String producer = product.getKey().getSecond(); int price =product.getValue().getFirst(); int quantity = product.getValue().getSecond();
            String [] array = {name,producer,price+"",quantity+"", (price*quantity)+""};
            table.addRow(array);
        }
        return table;
    }


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
        List<Integer> defList = reportDAO.SelectDefectiveProducts(storeId);
        List<Product> defProdList = new ArrayList<>();
        for (Map.Entry<Product,List<StoreProduct>> mapset: productListMap.entrySet()){
            if(defList.contains(mapset.getKey().getId()))
                defProdList.add(mapset.getKey());
        }
        CommandLineTable table = new CommandLineTable();
        table.setShowVerticalLines(true);
        table.setHeaders("Id", "name", "producer" ,"selling price", "buyingPrice", "categories");
        for(Product defective:defProdList)
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
