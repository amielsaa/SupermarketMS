package ServiceLayer;

import BusinessLayer.Order;
import BusinessLayer.OrderController;
import BusinessLayer.RoutineOrder;
import BusinessLayer.Supplier;
import ServiceLayer.DummyObjects.DOrder;
import ServiceLayer.DummyObjects.DRoutineOrder;
import ServiceLayer.DummyObjects.DSupplier;
import misc.Pair;

import java.util.*;

public class OrderService {
    private OrderController cOrder;

    public OrderService(){
        cOrder = new OrderController();
    }
    public Response addSupplier(String name, int business_num, int bank_acc_num, String payment_details, String contactName, String contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name, boolean delivery_by_days, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver){
        try {
            cOrder.addSupplier(business_num);
            return Response.makeSuccess(null);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }

    }

    public Response<DOrder> makeOrder(int supplierBN,HashMap <Pair<String,String>,Integer> order,HashMap<Pair<String,String>, Pair<Double,Double>> fixedOrder){
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


    public Response<DRoutineOrder> makeRoutineOrder(int business_num, HashMap<Pair<String, String>, Integer> order, HashMap<Pair<String, String>, Pair<Double, Double>> fixedOrder, Set<Integer> days) {
        try {
            RoutineOrder actualOrder = cOrder.makeRoutineOrder(business_num, order, fixedOrder,days);
            return Response.makeSuccess(new DRoutineOrder(actualOrder));
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<RoutineOrder> addOrUpdateRoutineOrder(int business_num, int orderId, HashMap<Pair<String, String>, Pair<Double, Double>> data) {
        try{
            RoutineOrder updatedRoutineOrder=cOrder.addOrUpdateRoutineOrder(business_num,orderId,data);
            return Response.makeSuccess(updatedRoutineOrder);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());

        }
    }
}
