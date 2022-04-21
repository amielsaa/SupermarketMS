package ServiceLayer;

import BusinessLayer.Order;
import BusinessLayer.OrderController;
import BusinessLayer.Supplier;
import ServiceLayer.DummyObjects.DOrder;
import ServiceLayer.DummyObjects.DSupplier;
import misc.Pair;

import java.util.*;

public class OrderService {
    private OrderController cOrder;

    public OrderService(){
        cOrder = new OrderController();
    }
    public Response addSupplier(String name, int business_num, int bank_acc_num, String payment_details, String contactName, int contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name, boolean delivery_by_days, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver){
        try {
            cOrder.addSupplier(business_num);
            return Response.makeSuccess(null);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }

    }

    public Response<DOrder> makeOrder(int supplierBN,HashMap <Integer,Integer> order,HashMap<Integer, Pair<String,Double>> fixedOrder){
        try {
            Order actualOrder = cOrder.makeOrder(supplierBN, order, fixedOrder);
            return Response.makeSuccess(new DOrder(actualOrder));
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<DOrder> getOrder(int supplierBN, int orderID){
        try {
            Order actualOrder = cOrder.getOrder(supplierBN, orderID);
            return Response.makeSuccess(new DOrder(actualOrder));
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<List<DOrder>> getAllOrdersFromSupplier(int supplierBN){
        try {
            Collection<Order> actualOrders = cOrder.getAllOrdersFromSupplier(supplierBN);
            List<DOrder> dummyOrders = new LinkedList<>();
            for(Order o: actualOrders) {
                dummyOrders.add(new DOrder(o));
            }
            return Response.makeSuccess(dummyOrders);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }




}
