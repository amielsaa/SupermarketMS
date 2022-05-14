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
    public Response addSupplier(int business_num){
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

    public Response<DRoutineOrder> addOrUpdateRoutineOrder(int business_num, int orderId, HashMap<Pair<String, String>, Pair<Double, Double>> data,int quantity) {
        try{
            RoutineOrder updatedRoutineOrder=cOrder.addOrUpdateRoutineOrder(business_num,orderId,data,quantity);
            return Response.makeSuccess(new DRoutineOrder(updatedRoutineOrder));
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());

        }
    }

    public Response<DRoutineOrder> deleteItemFromRoutineOrder(int business_num, int orderId, String itemName, String itemProducer) {
        try{
            RoutineOrder updatedRoutineOrder=cOrder.deleteItemFromRoutineOrder(business_num,orderId,itemName,itemProducer);
            return Response.makeSuccess(new DRoutineOrder(updatedRoutineOrder));
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());

        }
    }

    public Response<List<DOrder>> MakeOrderToSuppliers(HashMap<Integer, HashMap<Pair<String, String>, Pair<Double, Double>>> data, Map<Pair<String, String>, Integer> demandedSupplies) {
        try{
            List<Order> Orders=cOrder.MakeOrderToSuppliers(data,demandedSupplies);
            List<DOrder> OrdersForService=new ArrayList<>();
            for(Order i:Orders){
                OrdersForService.add(new DOrder(i));
            }
            return Response.makeSuccess(OrdersForService);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());

        }

    }
}
