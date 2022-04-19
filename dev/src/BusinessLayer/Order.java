package BusinessLayer;

import java.util.Date;
import java.util.HashMap;

public class Order {
    private int Supplier_BN;
    private int Order_Id;
    private HashMap<Integer,OrderItem> item_Num_To_OrderItem;
    private double final_Price;
    private Date Order_Date;


    public Order(int supplier_bn, int order_id, HashMap<Integer, OrderItem> item_num_to_orderItem, double final_price, Date order_date) {
        Supplier_BN = supplier_bn;
        Order_Id = order_id;
        item_Num_To_OrderItem = item_num_to_orderItem;
        final_Price = final_price;
        Order_Date = order_date;
    }

    public int getOrder_Id() {
        return Order_Id;
    }

    public void setOrder_Id(int order_Id) {
        Order_Id = order_Id;
    }

    public Date getOrder_Date() {
        return Order_Date;
    }

    public double getFinal_Price() {
        return final_Price;
    }

    public HashMap<Integer, OrderItem> getItem_Num_To_OrderItem() {
        return item_Num_To_OrderItem;
    }

    public int getSupplier_BN() {
        return Supplier_BN;
    }

    public void setFinal_Price(double final_Price) {
        this.final_Price = final_Price;
    }

    public void setItem_Num_To_OrderItem(HashMap<Integer, OrderItem> item_Num_To_OrderItem) {
        this.item_Num_To_OrderItem = item_Num_To_OrderItem;
    }

    public void setOrder_Date(Date order_Date) {
        Order_Date = order_Date;
    }

    public void setSupplier_BN(int supplier_BN) {
        Supplier_BN = supplier_BN;
    }
}
