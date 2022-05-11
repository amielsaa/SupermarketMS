package ServiceLayer.DummyObjects;

import BusinessLayer.Order;
import BusinessLayer.OrderItem;
import misc.Pair;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class DOrder {
    private int Supplier_BN;
    private int Order_Id;
    private HashMap<Pair<String,String>, DOrderItem> item_Num_To_OrderItem;
    private double PriceBeforeDiscount;
    private double final_Price;
    private Date Order_Date;


    public DOrder(Order o) {
        Supplier_BN = o.getSupplier_BN();
        Order_Id = o.getOrder_Id();
        final_Price = o.getFinal_Price();
        Order_Date = o.getOrder_Date();
        PriceBeforeDiscount = o.getPriceBeforeDiscount();
        item_Num_To_OrderItem = makeDOrderItems(o.getItem_Num_To_OrderItem());
    }

    private HashMap<Pair<String, String>, DOrderItem> makeDOrderItems(HashMap<Pair<String, String>, OrderItem> hm) {
        HashMap<Pair<String, String>, DOrderItem> ret = new HashMap<>();
        for(Pair<String,String> x : hm.keySet())
            ret.put(x,new DOrderItem(hm.get(x)));
        return ret;
    }

    public double getPriceBeforeDiscount() {
        return PriceBeforeDiscount;
    }

    public int getSupplier_BN() {
        return Supplier_BN;
    }

    public int getOrder_Id() {
        return Order_Id;
    }

    public HashMap<Pair<String,String>,DOrderItem> getItem_Num_To_OrderItem() {
        return item_Num_To_OrderItem;
    }

    public double getFinal_Price() {
        return final_Price;
    }

    public Date getOrder_Date() {
        return Order_Date;
    }

    public String toString(){
        return "Supplier BN: "+ getSupplier_BN() + ", OrderID: " + getOrder_Id() +", Order Date: " + getOrder_Date() + "\nItems: "+ itemsToString() + "\nOriginal Price: " + getPriceBeforeDiscount() + "\nFinal Price: " + getFinal_Price();
    }

    private String itemsToString(){
        String ret = "\n";
        HashMap<Pair<String,String>, DOrderItem> items = getItem_Num_To_OrderItem();
        for(DOrderItem x:items.values()){
            ret+=x.toStringForOrderPrint() + ",\n";
        }
        if(ret.length()>2)
            ret = ret.substring(0,ret.length()-2);
        return ret;
    }
}
