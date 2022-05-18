package Suppliers.ServiceLayer.DummyObjects;

import Suppliers.BusinessLayer.OrderItem;
import Suppliers.BusinessLayer.RoutineOrder;
import misc.Days;
import misc.Pair;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class DRoutineOrder extends DOrder{

    private Set<Days> days;



    public DRoutineOrder(RoutineOrder o) {
        super(o);
        days=o.getDays_To_Deliver();
    }

    private HashMap<Pair<String, String>, DOrderItem> makeDOrderItems(HashMap<Pair<String, String>, OrderItem> hm) {
        HashMap<Pair<String, String>, DOrderItem> ret = new HashMap<>();
        for(Pair<String,String> x : hm.keySet())
            ret.put(x,new DOrderItem(hm.get(x)));
        return ret;
    }

    public Set<Days> getDays() {
        return days;
    }


    public String toString(){
        return "Supplier BN: "+getSupplier_BN() + ", OrderID: " + getOrder_Id() +", Order Date: " + getOrder_Date() + "\nItems: "+ itemsToString() + "\nFinal Price: " + getFinal_Price() + "\nDays: " + getDays();
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
