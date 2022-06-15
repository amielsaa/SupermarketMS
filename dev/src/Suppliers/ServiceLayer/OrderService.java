package Suppliers.ServiceLayer;

import Suppliers.BusinessLayer.Order;
import Suppliers.BusinessLayer.OrderController;
import Suppliers.BusinessLayer.RoutineOrder;
import Suppliers.ServiceLayer.DummyObjects.DOrder;
import Suppliers.ServiceLayer.DummyObjects.DRoutineOrder;
import misc.Days;
import misc.Pair;

import java.time.LocalDate;
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

    public void setStartingValues() {
        cOrder.setStartingValues();
    }

    public Response<List<DRoutineOrder>> getAllRoutineOrders() {
        try{
            List<RoutineOrder> rountineOrders=cOrder.getAllRoutineOrders();
            List<DRoutineOrder> dRountineOrders=new ArrayList<>();
            for(RoutineOrder i:rountineOrders){
                dRountineOrders.add(new DRoutineOrder(i));
            }
            return Response.makeSuccess(dRountineOrders);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());


        }
    }

    public Response<List<DRoutineOrder>> getAllRoutineOrdersForTomorrow() {
        try{
            int day = (LocalDate.now().getDayOfWeek().getValue() + 2) % 7;
            Days tomorrow = dayConvertor(day);
            List<RoutineOrder> allRoutines = cOrder.getAllRoutineOrders();
            List<DRoutineOrder> newlist = new LinkedList<>();
            for (RoutineOrder o : allRoutines){
                if(o.getDays_To_Deliver().contains(tomorrow))
                    newlist.add(new DRoutineOrder(o));
            }
            return Response.makeSuccess(newlist);
        }
        catch(Exception e){
            return Response.makeFailure(e.getMessage());
        }

    }

    private Days dayConvertor(int day){
        if(day==1)
            return Days.sunday;
        else if(day==2)
            return Days.monday;
        else if(day==3)
            return Days.tuesday;
        else if(day==4)
            return Days.wednesday;
        else if(day==5)
            return Days.thursday;
        else if(day==6)
            return Days.friday;
        else if(day==0)
            return Days.saturday;
        else
            throw new IllegalArgumentException("day is not valid");

    }

    public void DeleteAll() {
        cOrder.DeleteAll();
    }

    public void removeSupplier(int bn) {
        cOrder.removeSupplier(bn);
    }

    public Response<Boolean> OrderArrivedAndAccepted(int bn,int orderId) {
        try{
            Boolean accepted=cOrder.OrderArrivedAndAccepted(bn,orderId);
                    return Response.makeSuccess(accepted);
        }
        catch(Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }
/*
    public Response<Boolean> setIfHasDeliveryToOrder(int bn,int orderId) {
        try{
            Boolean response=cOrder.setIfHasDeliveryToOrder(bn,orderId);
            return Response.makeSuccess(response);
        }
        catch(Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

 */
}
