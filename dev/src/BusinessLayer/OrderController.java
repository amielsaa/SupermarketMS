package BusinessLayer;

import DAL.OrderDAO;
import misc.Pair;
import BusinessLayer.*;
import java.sql.Date;
import java.util.*;

public class OrderController {
    private int Id_Order_Counter=0;
    private OrderDAO orderDAO;

    public OrderController() {
        orderDAO = new OrderDAO();
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
    public double getFinalPrice(int supplierBN,int orderID){
        Check_If_Order_Exists(supplierBN,orderID);
        return BN_To_Orders.get(supplierBN).get(orderID).getFinal_Price();

    }
    public void addSupplier(int supplierBN){
        if(BN_To_Orders.containsKey(supplierBN))
            throw new IllegalArgumentException("supplier already exists");
        BN_To_Orders.put(supplierBN,new HashMap<Integer,Order>());

    }
    public void removeSupplier(int supplierBN){
        BN_To_Orders.remove(supplierBN);
    }

    public Order makeOrder(int supplierBN,HashMap <Integer,Integer> order,HashMap<Integer, Pair<String,Double>> fixedOrder){

        Integer[] orderKeys= new Integer[order.keySet().toArray().length];// keys of the items in array
        for(int i=0; i<orderKeys.length;i++){
            orderKeys[i] = (Integer)order.keySet().toArray()[i];
        }

        HashMap <Integer,OrderItem> Item_Num_To_OrderItem=new HashMap<Integer,OrderItem>();//the Parameter that will be inserted into the Order
        double finalPrice=0;
        for (int i=0;i<orderKeys.length;i++) {
            OrderItem orderItem=new OrderItem(Id_Order_Counter,fixedOrder.get(orderKeys[i]).getFirst(),orderKeys[i],fixedOrder.get(orderKeys[i]).getSecond(),order.get(orderKeys[i]));

            Item_Num_To_OrderItem.put(orderKeys[i],orderItem);
            finalPrice=finalPrice+fixedOrder.get(orderKeys[i]).getSecond();
        }
        Date date=new Date(System.currentTimeMillis());
        Order newOrder = new Order(supplierBN,Id_Order_Counter,Item_Num_To_OrderItem,finalPrice, date);
        BN_To_Orders.get(supplierBN).put(Id_Order_Counter, newOrder);
        Id_Order_Counter++;

        return newOrder;

    }



}
