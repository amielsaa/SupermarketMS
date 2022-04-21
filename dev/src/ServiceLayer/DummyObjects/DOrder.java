package ServiceLayer.DummyObjects;

import BusinessLayer.Order;
import BusinessLayer.OrderItem;

import java.util.Date;
import java.util.HashMap;

public class DOrder {
    private int Supplier_BN;
    private int Order_Id;
    private HashMap<Integer, OrderItem> item_Num_To_OrderItem;
    private double final_Price;
    private Date Order_Date;


    public DOrder(Order o) {
        Supplier_BN = o.getSupplier_BN();
        Order_Id = o.getOrder_Id();
        item_Num_To_OrderItem = o.getItem_Num_To_Quantity();
        final_Price = o.getFinal_Price();
        Order_Date = o.getOrder_Date();
    }

    public int getSupplier_BN() {
        return Supplier_BN;
    }

    public int getOrder_Id() {
        return Order_Id;
    }

    public HashMap<Integer, OrderItem> getItem_Num_To_OrderItem() {
        return item_Num_To_OrderItem;
    }

    public double getFinal_Price() {
        return final_Price;
    }

    public Date getOrder_Date() {
        return Order_Date;
    }

    public String toString(){
        return "DOrder toString is not implemented.";
    }
}
