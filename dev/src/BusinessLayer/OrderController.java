package BusinessLayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class OrderController {
private int Id_Order_Counter=0;
private HashMap<Integer, HashMap<Integer,Order>> BN_To_Orders;

    public OrderController() {
        BN_To_Orders = new HashMap<Integer,HashMap<Integer,Order>>();
    }

    public HashMap<Integer, HashMap<Integer,Order>>getOrder_Num_To_Order() {
        return BN_To_Orders;
    }

    public int getId_Order_Counter() {
        return Id_Order_Counter;
    }

    public void setId_Order_Counter(int id_Order_Counter) {
        Id_Order_Counter = id_Order_Counter;
    }


    //return all the Orders from the specific supplier
    public Collection<Order> getAllOrdersFromSupplier(int supplierBN){
        return BN_To_Orders.get(supplierBN).values();

    }
    //gets order by BN and orderID
    public Order getOrder(int supplierBN,int orderID){
        Check_If_Order_Exists(supplierBN,orderID);
        return BN_To_Orders.get(supplierBN).get(orderID);

    }
    //Private to check if Order exists
    private void Check_If_Order_Exists(int supplierBN,int orderID){
        if(!BN_To_Orders.get(supplierBN).containsKey(orderID)){
            throw new IllegalArgumentException("Order ID was not found");
        }
    }
    public  double getFinalPrice(int supplierBN,int orderID){
        Check_If_Order_Exists(supplierBN,orderID);
        return BN_To_Orders.get(supplierBN).get(orderID).getFinal_Price();

    }
    public void addSupplier(int supplierBN){
        if(!BN_To_Orders.containsKey(supplierBN))
        BN_To_Orders.put(supplierBN,new HashMap<Integer,Order>());
        throw new IllegalArgumentException("supplier already exists");
    }


}
