package PresentationLayer;

import BusinessLayer.QuantityAgreement;
import BusinessLayer.Supplier;
import DAL.DalController;
import DAL.SupplierDAO;
import ServiceLayer.DummyObjects.DRoutineOrder;
import ServiceLayer.Response;
import ServiceLayer.SupplierFacade;
import misc.Pair;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
       // PresentationMain fMain = new PresentationMain();
       // fMain.main();
        SupplierFacade supplierFacade=new SupplierFacade();
        HashMap<Pair<String,String>,Double> item_To_Price=new HashMap<>();
        Pair pair=new Pair("banana","tnuva");//QA
        item_To_Price.put(pair,(double)1);
        HashMap<Pair<String,String>,HashMap<Integer,Integer>> item_Num_To_Quantity_To_Discount=new HashMap<>();//Discounts
        item_Num_To_Quantity_To_Discount.put(pair,new HashMap<Integer,Integer>());
        item_Num_To_Quantity_To_Discount.get(pair).put(10,5);
        item_Num_To_Quantity_To_Discount.get(pair).put(100,10);
        item_Num_To_Quantity_To_Discount.get(pair).put(500,20);
        String name="ari";
        String phone="123";
        Set<Integer> a=new LinkedHashSet<>();
        a.add(1);
        supplierFacade.addSupplier(name,111111111,1,"check",a,name,phone,item_To_Price,item_Num_To_Quantity_To_Discount,true);
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        order.put(pair,500);
        supplierFacade.makeRoutineOrder(111111111,order,a);
        Response<DRoutineOrder> response=supplierFacade.deleteItemFromRoutineOrder(111111111,0,"banana","tnuva");



    }
}
