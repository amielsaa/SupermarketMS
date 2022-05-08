package BusinessLayer;

public class OrderItem {
    private Integer Business_Number;
    private Integer Order_Id;
    private String Item_Name;
    private Integer Item_Num;
    private Double Item_Price;
    private Integer Item_Amount;

    public OrderItem(Integer business_Number,Integer order_id, String item_name, Integer item_num, Double item_price, Integer item_amount) {
        business_Number=business_Number;
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

    public String toStringForOrderPrint(){
        return "Item name: "+getItem_Name() + " ,Item ID: "+getItem_Num() + " ,Item amount: "+getItem_Amount() + " ,Item price: " + getItem_Price();
    }
}
