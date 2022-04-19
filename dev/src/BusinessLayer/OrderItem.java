package BusinessLayer;

public class OrderItem {
    private int Order_Id;
    private String Item_Name;
    private int Item_Num;
    private double Item_Price;
    private int Item_Amount;

    public OrderItem(int order_id, String item_name, int item_num, double item_price, int item_amount) {
        Order_Id = order_id;
        Item_Name = item_name;
        Item_Num = item_num;
        Item_Price = item_price;
        Item_Amount = item_amount;
    }

    public double getItem_Price() {
        return Item_Price;
    }

    public int getItem_Amount() {
        return Item_Amount;
    }

    public int getItem_Num() {
        return Item_Num;
    }

    public int getOrder_Id() {
        return Order_Id;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public void setItem_Amount(int item_Amount) {
        Item_Amount = item_Amount;
    }
    public void setItem_Name(String item_Name) {
        Item_Name = item_Name;
    }

    public void setItem_Num(int item_Num) {
        Item_Num = item_Num;
    }

    public void setItem_Price(double item_Price) {
        Item_Price = item_Price;
    }

    public void setOrder_Id(int order_Id) {
        Order_Id = order_Id;
    }
}
