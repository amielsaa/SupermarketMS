package BusinessLayer;

import java.util.HashMap;

public class OrderController {
private int Id_Order_Counter=0;
private HashMap<Integer,Order> Order_Num_To_Order;

    public OrderController( HashMap<Integer, Order> order_num_to_order) {
        Order_Num_To_Order = new HashMap<Integer,Order>();
    }

    public HashMap<Integer, Order> getOrder_Num_To_Order() {
        return Order_Num_To_Order;
    }

    public int getId_Order_Counter() {
        return Id_Order_Counter;
    }

    public void setId_Order_Counter(int id_Order_Counter) {
        Id_Order_Counter = id_Order_Counter;
    }

    public void setOrder_Num_To_Order(HashMap<Integer, Order> order_Num_To_Order) {
        Order_Num_To_Order = order_Num_To_Order;
    }
}
