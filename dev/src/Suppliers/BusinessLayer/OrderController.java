package Suppliers.BusinessLayer;

import Suppliers.DAL.DaysToDeliverDAO;
import Suppliers.DAL.OrderDAO;
import Suppliers.DAL.OrderItemsDAO;
import misc.Pair;

import java.sql.Date;
import java.util.*;
import java.util.zip.DataFormatException;

public class OrderController {
    private int Id_Order_Counter = 0;
    private OrderDAO orderDAO;
    private OrderItemsDAO orderItemsDAO;
    private DaysToDeliverDAO daysToDeliverDAO;

    public OrderController() {
        orderDAO = new OrderDAO();
        orderItemsDAO = new OrderItemsDAO();
        daysToDeliverDAO = new DaysToDeliverDAO();
    }


    public int getId_Order_Counter() {
        return Id_Order_Counter;
    }

    public void setId_Order_Counter(int id_Order_Counter) {
        Id_Order_Counter = id_Order_Counter;
    }

    public void setStartingValues() {
        setId_Order_Counter(orderDAO.getMaxOrderId());
        daysToDeliverDAO.setAllRoutineOrders();
    }
    //return all the Orders from the specific supplier
    public Collection<Order> getAllOrdersFromSupplier(int supplierBN) throws DataFormatException {
        if (!orderDAO.setAllOrders(supplierBN))
            return new ArrayList<Order>();
        Collection<Order> preBuiltOrders=orderDAO.getAllOrders(supplierBN);
        Collection<Order> builtOrders=new ArrayList<>();
        for (Order i:preBuiltOrders){
            builtOrders.add(buildOrder(supplierBN,i.getOrder_Id()));
        }
        return builtOrders;

    }

    //gets order by BN and orderID
    public Order getOrder(int supplierBN, int orderID) throws DataFormatException {
        if (!orderDAO.containsOrder(supplierBN, orderID))
            throw new DataFormatException("Order does not exists");
        return buildOrder(supplierBN, orderID);
    }

    //when added a new supplier-adds him to the map of the database.
    public void addSupplier(int supplierBN) {
        orderDAO.addSupplier(supplierBN);

    }
    //when removing supplier-deletes all his orders from the database.-stupid, needs proof of every order even
    //if supplier is deleted.


    public Order makeOrder(int supplierBN, HashMap<Pair<String, String>, Integer> order, HashMap<Pair<String, String>, Pair<Double, Double>> fixedOrder) throws Exception {

        Pair<String, String>[] orderKeys = new Pair[order.keySet().toArray().length];// keys of the items in array
        for (int i = 0; i < orderKeys.length; i++) {
            orderKeys[i] = (Pair) order.keySet().toArray()[i];
        }
        HashMap<Pair<String, String>, OrderItem> Item_Name_To_OrderItem = new HashMap<Pair<String, String>, OrderItem>();
        //  HashMap<Integer, OrderItem> Item_Num_To_OrderItem = new HashMap<Integer, OrderItem>();//the Parameter that will be inserted into the Order
        double finalPrice = 0;
        double priceBeforeDiscount = 0;

        for (int i = 0; i < orderKeys.length; i++) {
            OrderItem orderItem = new OrderItem(getId_Order_Counter(), orderKeys[i].getFirst(), orderKeys[i].getSecond(), fixedOrder.get(orderKeys[i]).getFirst(), fixedOrder.get(orderKeys[i]).getSecond(), order.get(orderKeys[i]));
            //insert to data
            if (!orderItemsDAO.insertOrderItem(Id_Order_Counter, orderKeys[i].getFirst(), orderKeys[i].getSecond(),fixedOrder.get(orderKeys[i]).getSecond(), fixedOrder.get(orderKeys[i]).getFirst(),  order.get(orderKeys[i])))
                throw new DataFormatException("failed to Insert Item to data on makeOrder");


            Item_Name_To_OrderItem.put(orderKeys[i], orderItem);
            finalPrice = finalPrice + fixedOrder.get(orderKeys[i]).getSecond();
            priceBeforeDiscount = priceBeforeDiscount + fixedOrder.get(orderKeys[i]).getFirst();
        }
        Date date = new Date(System.currentTimeMillis());
        String dateForData = date.toString();
        Order newOrder = new Order(supplierBN, Id_Order_Counter, Item_Name_To_OrderItem, priceBeforeDiscount, finalPrice, date);
        //insert to data
        if (!orderDAO.insertOrders(supplierBN, Id_Order_Counter, finalPrice, dateForData, priceBeforeDiscount,0))
            throw new DataFormatException("cound not insert Order into data on makeOrder");
        Id_Order_Counter++;

        return newOrder;

    }

    public RoutineOrder makeRoutineOrder(int business_num, HashMap<Pair<String, String>, Integer> order, HashMap<Pair<String, String>, Pair<Double, Double>> fixedOrder, Set<Integer> days) throws Exception {
        Order orderforRoutine = makeOrder(business_num, order, fixedOrder);
        RoutineOrder routineOrder = new RoutineOrder(orderforRoutine, days);
        for (Integer i : days) {
            daysToDeliverDAO.insertDaysToDeliver(business_num, routineOrder.getOrder_Id(), i);
        }
        return routineOrder;
    }

    public RoutineOrder addOrUpdateRoutineOrder(int business_num, int orderId, HashMap<Pair<String, String>, Pair<Double, Double>> data, int quantity) throws DataFormatException {
        if (!orderDAO.containsOrder(business_num, orderId))
            throw new DataFormatException("Order does not exists");
        if (daysToDeliverDAO.CheckIfOrderIsRoutineOrder(business_num,orderId)) {

                RoutineOrder routineOrder = buildRoutineOrder(business_num, orderId);
                Set<Pair<String, String>> key = data.keySet();
                for (Pair i : key) {
                    boolean updateOrAdd = routineOrder.addOrUpdateRoutineOrder(i, data.get(i).getFirst(), data.get(i).getSecond(),quantity);
                    //update
                    if(!updateOrAdd){
                        if(!orderItemsDAO.updateItem(orderId,i.getFirst().toString(),i.getSecond().toString(),data.get(i).getSecond(),data.get(i).getFirst(),quantity))
                            throw new DataFormatException("Error in orderItem update");
                    }
                    else{
                        if(!orderItemsDAO.insertOrderItem(orderId,i.getFirst().toString(),i.getSecond().toString(),data.get(i).getSecond(),data.get(i).getFirst(),quantity))
                            throw new DataFormatException("Error in orderItem insert");

                    }
                    if(!orderDAO.updateOrderPrice(business_num,orderId,routineOrder.getPriceBeforeDiscount(),routineOrder.getFinal_Price()))
                        throw new DataFormatException("Error in Order update");


                }
                orderDAO.updateOrderInHM(business_num,routineOrder);
                return routineOrder;


        } else
            throw new DataFormatException("Order is not RoutineOrder");


    }
    public RoutineOrder deleteItemFromRoutineOrder(int business_num, int orderId, String itemName, String itemProducer) throws Exception {
        if (!orderDAO.containsOrder(business_num, orderId))
            throw new DataFormatException("Order does not exists");
        if (daysToDeliverDAO.CheckIfOrderIsRoutineOrder(business_num,orderId)) {
                RoutineOrder routineOrder = buildRoutineOrder(business_num, orderId);
                boolean isSuccess=routineOrder.deleteItemFromRoutineOrder(itemName,itemProducer);
                if(!isSuccess)
                    throw new IllegalArgumentException("Could not find the item in the order");
                else{
                   if (!orderItemsDAO.deleteOrderItem(orderId,itemName,itemProducer))
                       throw new DataFormatException("Error in orderItem delete");

                    if(!orderDAO.updateOrderPrice(business_num,orderId,routineOrder.getPriceBeforeDiscount(),routineOrder.getFinal_Price()))
                        throw new DataFormatException("Error in order update");
                    if(routineOrder.getFinal_Price()==0){
                        if(orderDAO.deleteOrder(business_num,orderId)){
                            if(!daysToDeliverDAO.deleteAllDaysToDeliver(business_num,orderId))
                                throw new DataFormatException("Error in Order delete or in daysToDeliver Delete");
                        }
                        else throw new DataFormatException("Error in Order delete or in Orders Delete");
                    }

                }
                return routineOrder;



        }
        else
            throw new DataFormatException("Order is not RoutineOrder");
    }





      /*  public void updateOrderDeliveryDays ( int business_num, Set<Integer > days) throws DataFormatException {
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

       */


    private RoutineOrder buildRoutineOrder(int bn,int orderId) throws DataFormatException {
        Order order =buildOrder(bn,orderId);
        Collection<Integer> days=daysToDeliverDAO.selectAllDays(bn,orderId);
        Set<Integer> setdays=new HashSet<>();
        for(Integer i:days){
           setdays.add(i);
        }
        return new RoutineOrder(order,setdays);
    }

    private Order buildOrder(int bn,int orderId) throws DataFormatException {
        if(!orderDAO.containsOrder(bn,orderId))
            throw new DataFormatException("order was not found");
        Order order= orderDAO.getOrder(bn,orderId);
        Collection<OrderItem> orderItems=orderItemsDAO.selectAllOrderItems(orderId);
        HashMap<Pair<String,String>, OrderItem> orderItemHashMap=new HashMap<>();
        for(OrderItem i:orderItems){
            orderItemHashMap.put(new Pair(i.getItem_Name(),i.getItem_Producer()),i);
        }
        order.setItem_Num_To_Quantity(orderItemHashMap);
        return order;
    }

    public List<Order> MakeOrderToSuppliers(HashMap<Integer, HashMap<Pair<String, String>, Pair<Double, Double>>> data, Map<Pair<String, String>, Integer> demandedSupplies) throws Exception {
        List<Order> OrderList=new ArrayList<>();
        Set<Integer> keysetOfBN=data.keySet();
        for(Integer i: keysetOfBN){
            //preparing the Order
            HashMap<Pair<String, String>, Integer> order=new HashMap<>();
            Set<Pair<String, String>> OrderKeys=data.get(i).keySet();
            for(Pair j:OrderKeys){
                order.put(j,demandedSupplies.get(j));
            }
            Order completeOrder= makeOrder(i, order, data.get(i));
            OrderList.add(completeOrder);
        }
        return OrderList;
    }



    public List<RoutineOrder> getAllRoutineOrders() throws DataFormatException {
        //daysToDeliverDAO.setAllRoutineOrders(); //already does this in setStartingValues
        HashMap<Integer,List<Integer>> BN_To_listOfRountineOrdersId=daysToDeliverDAO.getBN_to_routineOrder();
        List<RoutineOrder> toreturn=new ArrayList<>();
        Set<Integer> keySet=BN_To_listOfRountineOrdersId.keySet();
        for(Integer i:keySet){
            List<Integer> RountineOrderList=BN_To_listOfRountineOrdersId.get(i);
            for(Integer j:RountineOrderList){
                toreturn.add(buildRoutineOrder(i,j));
            }
        }
        return toreturn;
    }

    public void DeleteAll() {
        daysToDeliverDAO.deleteAll();
        daysToDeliverDAO.clearAll();
        orderItemsDAO.deleteAll();
        orderDAO.deleteAll();
        orderDAO.clearAll();
        setId_Order_Counter(0);
    }

    public void removeSupplier(int bn) {
        daysToDeliverDAO.deleteAllSupplierDaysToDeliver(bn);
        orderDAO.setAllOrders(bn);
        Collection<Order> collection= orderDAO.getAllOrders(bn);
        for(Order i:collection)
            orderItemsDAO.deleteAllOrderItemsFromOrder(i.getOrder_Id());
        orderDAO.deleteAllSupplierOrders(bn);


    }

    public Boolean setIfHasDeliveryToOrder(int bn, int orderId) throws DataFormatException {
        Order order=buildOrder(bn,orderId);
        if(order.getHasDelivery()){
            throw new IllegalArgumentException("this Order has a delivery already");
        }
        order.setHasDelivery(true);
        orderDAO.updateHasDelivery(bn,orderId,1);
        return true;
    }

    public Boolean OrderArrivedAndAccepted(int bn,int orderId) throws DataFormatException {
        if(orderDAO.containsOrder(bn,orderId)){
            if(daysToDeliverDAO.CheckIfOrderIsRoutineOrder(bn,orderId)){
                RoutineOrder order=buildRoutineOrder(bn,orderId);
                order.setHasDelivery(false);
                orderDAO.updateHasDelivery(bn,orderId,0);
                return true;
            }
            else{
                orderDAO.deleteOrder(bn,orderId);
                return true;
            }
        }
        throw new DataFormatException("Order does not exists");


}


    //TODO:I NEED A DATA FUNCTION TO GIVE ME ALL ORDERS WITH HASDELIVERY=0
    public Collection<Order> getAllRegularOrdersWithoutDeliveries() {
        Collection<Order> orders=new ArrayList<>();
        return orders;
    }

    public Boolean checkIfHasDelivery(int bn, int orderId) throws DataFormatException {
        return buildOrder(bn,orderId).getHasDelivery();


    }
}


