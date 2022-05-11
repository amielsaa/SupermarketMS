package BusinessLayer;

public class OrderItem {
//    private Integer Business_Number;
    private Integer Order_Id;
    private String Item_Name;
    private String Item_Producer;
    private Double Item_Original_Price;
    private Double Item_Price;
    private Integer Item_Amount;

    public OrderItem(/*Integer business_Number,*/ Integer order_id, String item_name, String item_producer,Double item_Original_Price, Double item_price, Integer item_amount) {
//        Business_Number=business_Number;
        Order_Id = order_id;
        Item_Name = item_name;
        Item_Producer = item_producer;
        Item_Price = item_price;
        Item_Amount = item_amount;
        Item_Original_Price=item_Original_Price;

    }

    public Double getItem_Original_Price() {
        return Item_Original_Price;
    }

    public double getItem_Price() {
        return Item_Price;
    }

    public int getItem_Amount() {
        return Item_Amount;
    }

    public String getItem_Producer() {
        return Item_Producer;
    }

    public void setItem_Producer(String item_Producer) {
        Item_Producer = item_Producer;
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


    public void setItem_Price(double item_Price) {
        Item_Price = item_Price;
    }

    public void setOrder_Id(int order_Id) {
        Order_Id = order_Id;
    }




}
