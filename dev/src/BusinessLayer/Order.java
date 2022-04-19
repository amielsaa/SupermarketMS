package BusinessLayer;

import java.util.Date;
import java.util.HashMap;

public class Order {
    private int Supplier_BN;
    private int Order_Id;
    private HashMap<Integer,Integer> item_Num_To_Quantity;
    private double final_Price;
    private Date Order_Date;


    public Order(int supplier_bn, int order_id, HashMap<Integer, Integer> item_num_to_orderItem, Date order_date) {
        Supplier_BN = supplier_bn;
        Order_Id = order_id;
        item_Num_To_Quantity = item_num_to_orderItem;
//        final_Price = final_price; //todo: update final price after discounts
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

    public HashMap<Integer, Integer> getItem_Num_To_Quantity() {
        return item_Num_To_Quantity;
    }

    public void setItem_Num_To_Quantity(HashMap<Integer, Integer> item_Num_To_Quantity) {
        this.item_Num_To_Quantity = item_Num_To_Quantity;
    }


}
