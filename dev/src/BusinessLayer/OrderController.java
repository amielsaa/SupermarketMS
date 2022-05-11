package BusinessLayer;

import DAL.DaysToDeliverDAO;
import DAL.OrderDAO;
import DAL.OrderItemsDAO;
import misc.Days;
import misc.Pair;
import BusinessLayer.*;
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
        daysToDeliverDAO=new DaysToDeliverDAO();
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
    }

        //when added a new supplier-adds him to the map of the database.
        public void addSupplier ( int supplierBN){
            orderDAO.addSupplier(supplierBN);

        }
        //when removing supplier-deletes all his orders from the database.-stupid, needs proof of every order even
        //if supplier is deleted.


        public Order makeOrder ( int supplierBN, HashMap <Pair<String,String>, Integer > order, HashMap < Pair<String,String>, Pair < Double, Double >> fixedOrder) throws Exception {

            Pair<String,String>[] orderKeys = new Pair[order.keySet().toArray().length];// keys of the items in array
            for (int i = 0; i < orderKeys.length; i++) {
                orderKeys[i] = (Pair) order.keySet().toArray()[i];
            }
            HashMap<Pair<String,String>,OrderItem> Item_Name_To_OrderItem=new HashMap<Pair<String,String>,OrderItem>();
          //  HashMap<Integer, OrderItem> Item_Num_To_OrderItem = new HashMap<Integer, OrderItem>();//the Parameter that will be inserted into the Order
            double finalPrice = 0;
            double priceBeforeDiscount=0;

            for (int i = 0; i < orderKeys.length; i++) {
                OrderItem orderItem = new OrderItem(getId_Order_Counter(),orderKeys[i].getFirst(),orderKeys[i].getSecond(),fixedOrder.get(orderKeys[i]).getFirst(),fixedOrder.get(orderKeys[i]).getSecond(),order.get(orderKeys[i]));
                //insert to data
               if(!orderItemsDAO.insertOrderItem(Id_Order_Counter,orderKeys[i].getFirst(),orderKeys[i].getSecond(),fixedOrder.get(orderKeys[i]).getFirst(),fixedOrder.get(orderKeys[i]).getSecond(),order.get(orderKeys[i])))
                   throw new DataFormatException("failed to Insert Item to data on makeOrder");
                //todo: remove supplierBN from orderItem constructor ^^^^

                Item_Name_To_OrderItem.put(orderKeys[i], orderItem);
                finalPrice = finalPrice + fixedOrder.get(orderKeys[i]).getSecond();
                priceBeforeDiscount=priceBeforeDiscount+fixedOrder.get(orderKeys[i]).getFirst();
            }
            Date date = new Date(System.currentTimeMillis());
            String dateForData=date.toString();
            Order newOrder = new Order(supplierBN, Id_Order_Counter, Item_Name_To_OrderItem,priceBeforeDiscount, finalPrice, date);
            //insert to data
            if(!orderDAO.insertOrders(supplierBN,Id_Order_Counter,finalPrice,dateForData,priceBeforeDiscount))
                throw new DataFormatException("cound not insert Order into data on makeOrder");
            Id_Order_Counter++;

            return newOrder;

        }

    public RoutineOrder makeRoutineOrder(int business_num, HashMap<Pair<String, String>, Integer> order, HashMap<Pair<String, String>, Pair<Double, Double>> fixedOrder, Set<Integer> days) throws Exception {
        Order orderforRoutine=makeOrder(business_num,order,fixedOrder);
        RoutineOrder routineOrder=new RoutineOrder(orderforRoutine,days);
        for(Integer i:days){
            daysToDeliverDAO.insertDaysToDeliver(business_num,routineOrder.getOrder_Id(),i);
        }
        return routineOrder;
    }

    public RoutineOrder addOrUpdateRoutineOrder(int business_num, int orderId, HashMap<Pair<String, String>, Pair<Double, Double>> data) throws DataFormatException {
        if (!orderDAO.containsOrder(business_num, orderId))
            throw new DataFormatException("Order does not exists");
        if(daysToDeliverDAO.getBN_to_routineOrder().contains(business_num)){
            if (daysToDeliverDAO.getBN_to_routineOrder().get(business_num).contains(orderId))
                {

                }
            }
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


    private RoutineOrder buildRoutineOrder(int bn,int orderId){
        Order order =orderDAO.getOrder(bn,orderId);
        Collection<Days> days=daysToDeliverDAO.selectAllDays(bn,orderId);
        Set<Days> setdays=new HashSet<>();
        for(Days i:days){
           setdays.add(i);
        }
        return new RoutineOrder(order,setdays);
    }
    }


