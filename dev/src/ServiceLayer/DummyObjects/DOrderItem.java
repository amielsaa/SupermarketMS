package ServiceLayer.DummyObjects;

import BusinessLayer.OrderItem;

public class DOrderItem {

    private Integer Order_Id;
    private String Item_Name;
    private String Item_Producer;
    private Double Item_Original_Price;
    private Double Item_Price;
    private Integer Item_Amount;

    public DOrderItem(OrderItem orderItem) {
        Order_Id = orderItem.getOrder_Id();
        Item_Name = orderItem.getItem_Name();
        Item_Producer = orderItem.getItem_Producer();
        Item_Original_Price = orderItem.getItem_Original_Price();
        Item_Price = orderItem.getItem_Price();
        Item_Amount = orderItem.getItem_Amount();
    }

    public Integer getOrder_Id() {
        return Order_Id;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public String getItem_Producer() {
        return Item_Producer;
    }

    public Double getItem_Original_Price() {
        return Item_Original_Price;
    }

    public Double getItem_Price() {
        return Item_Price;
    }

    public Integer getItem_Amount() {
        return Item_Amount;
    }

    public String toStringForOrderPrint(){
        return "Item name: "+getItem_Name() + " ,Producer: "+ getItem_Producer() + " ,Item amount: "+getItem_Amount() + " ,Original price: " + getItem_Original_Price() + " ,Item final price (): " + getItem_Price();
    }
}
