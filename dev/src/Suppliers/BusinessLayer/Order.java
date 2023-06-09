package Suppliers.BusinessLayer;


import java.sql.Date;
import java.util.HashMap;


import Suppliers.BusinessLayer.*;
import misc.Pair;

public class Order {
    private int Supplier_BN;
    private int Order_Id;
    private HashMap<Pair<String,String>, OrderItem> item_Num_To_OrderItem;//item to its order details
    private double PriceBeforeDiscount;
    private double final_Price;
    private Date Order_Date;
    private boolean hasDelivery;


    public Order(int supplier_bn, int order_id, HashMap<Pair<String,String>, OrderItem> item_num_to_orderItem,double priceBeforeDiscount,double final_price, Date order_date) {
        Supplier_BN = supplier_bn;
        Order_Id = order_id;
        item_Num_To_OrderItem = item_num_to_orderItem;
        PriceBeforeDiscount=priceBeforeDiscount;
        final_Price = final_price;
        Order_Date = order_date;
        hasDelivery=false;
    }

    public Order(int supplier_bn, int order_id, double final_price, String order_date, double originalprice, int hasdelivery) {
        //DAL constructor
        Supplier_BN = supplier_bn;
        Order_Id = order_id;
        final_Price = final_price;
        Order_Date = Date.valueOf(order_date);
        PriceBeforeDiscount = originalprice;
        hasDelivery= hasdelivery == 1;
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

    public int getSupplier_BN() {
        return Supplier_BN;
    }

    public void setFinal_Price(double final_Price) {
        this.final_Price = final_Price;
    }

    public void setOrder_Date(Date order_Date) {
        Order_Date = order_Date;
    }

    public void setSupplier_BN(int supplier_BN) {
        Supplier_BN = supplier_BN;
    }

    public HashMap<Pair<String,String>,OrderItem> getItem_Num_To_OrderItem() {
        return item_Num_To_OrderItem;
    }

    public void setItem_Num_To_Quantity(HashMap<Pair<String,String>, OrderItem> item_Num_To_Quantity) {
        this.item_Num_To_OrderItem = item_Num_To_Quantity;
    }


    public double getPriceBeforeDiscount() {
        return PriceBeforeDiscount;
    }

    public void setPriceBeforeDiscount(double priceBeforeDiscount) {
        this.PriceBeforeDiscount = priceBeforeDiscount;
    }

    public boolean getHasDelivery() {
        return hasDelivery;
    }

    public void setHasDelivery(boolean hasDelivery) {
        this.hasDelivery = hasDelivery;
    }
}
