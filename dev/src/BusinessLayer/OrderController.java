package BusinessLayer;

import DAL.OrderDAO;
import DAL.OrderItemsDAO;
import misc.Pair;
import BusinessLayer.*;
import java.sql.Date;
import java.util.*;
import java.util.zip.DataFormatException;

public class OrderController {
    private int Id_Order_Counter = 0;
    private OrderDAO orderDAO;
    private OrderItemsDAO orderItemsDAO;

    public OrderController() {
        orderDAO = new OrderDAO();
        orderItemsDAO = new OrderItemsDAO();
    }


    public int getId_Order_Counter() {
        return Id_Order_Counter;
    }

    public void setId_Order_Counter(int id_Order_Counter) {
        Id_Order_Counter = id_Order_Counter;
    }


    //return all the Orders from the specific supplier
    public Collection<Order> getAllOrdersFromSupplier(int supplierBN) {
        if (!orderDAO.setAllOrders(supplierBN))
            return new ArrayList<Order>();
        return orderDAO.getAllOrders(supplierBN);

    }

    //gets order by BN and orderID
    public Order getOrder(int supplierBN, int orderID) throws DataFormatException {
        if (!orderDAO.containsOrder(supplierBN, orderID))
            throw new DataFormatException("Order does not exists");
        return orderDAO.getOrder(supplierBN, orderID);


        public void addSupplier ( int supplierBN){
            if (BN_To_Orders.containsKey(supplierBN))
                throw new IllegalArgumentException("supplier already exists");
            BN_To_Orders.put(supplierBN, new HashMap<Integer, Order>());

        }
        public void removeSupplier (int supplierBN){
            BN_To_Orders.remove(supplierBN);
        }

        public Order makeOrder (int supplierBN, HashMap <Integer, Integer > order, HashMap < Integer, Pair < String, Double >> fixedOrder){

            Integer[] orderKeys = new Integer[order.keySet().toArray().length];// keys of the items in array
            for (int i = 0; i < orderKeys.length; i++) {
                orderKeys[i] = (Integer) order.keySet().toArray()[i];
            }

            HashMap<Integer, OrderItem> Item_Num_To_OrderItem = new HashMap<Integer, OrderItem>();//the Parameter that will be inserted into the Order
            double finalPrice = 0;
            for (int i = 0; i < orderKeys.length; i++) {
                OrderItem orderItem = new OrderItem(supplierBN, Id_Order_Counter, fixedOrder.get(orderKeys[i]).getFirst(), orderKeys[i], fixedOrder.get(orderKeys[i]).getSecond(), order.get(orderKeys[i]));
                //todo: remove supplierBN from orderItem constructor ^^^^

                Item_Num_To_OrderItem.put(orderKeys[i], orderItem);
                finalPrice = finalPrice + fixedOrder.get(orderKeys[i]).getSecond();
            }
            Date date = new Date(System.currentTimeMillis());
            Order newOrder = new Order(supplierBN, Id_Order_Counter, Item_Num_To_OrderItem, finalPrice, date);
            BN_To_Orders.get(supplierBN).put(Id_Order_Counter, newOrder);
            Id_Order_Counter++;

            return newOrder;

        }

        public void updateOrderDeliveryDays ( int business_num, Set<Integer > days) throws DataFormatException {
            if (!supplierDAO.containsSupplier(business_num))
                throw new IllegalArgumentException("Supplier was not found");
            supplierDAO.getSupplier(business_num).setDays_To_Deliver(days);
            if (!daysToDeliverDAO.updateDeliveryDays(business_num, days))
                throw new DataFormatException("Error In Database on updateSupplierDeliveryDays");
        }

        public void addOrderDeliveryDay ( int business_num, int orderID, int day) throws DataFormatException {
            if (!supplierDAO.containsSupplier(business_num))
                throw new IllegalArgumentException("Supplier was not found");
            supplierDAO.getSupplier(business_num).addSupplierDeliveryDay(day);
            if (!daysToDeliverDAO.insertDaysToDeliver(business_num, day))
                throw new DataFormatException("Error In Database on addSupplierDeliveryDay");
        }


        public void removeOrderDeliveryDay ( int business_num, int orderID, int day) throws DataFormatException {
            if (!supplierDAO.containsSupplier(business_num))
                throw new IllegalArgumentException("Supplier was not found");
            supplierDAO.getSupplier(business_num).removeSupplierDeliveryDay(day);
            if (!daysToDeliverDAO.deleteDaysToDeliver(business_num, day))
                throw new DataFormatException("Error In Database on removeSupplierDeliveryDay");

        }


    }
}

