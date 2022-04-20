package ServiceLayer;

import BusinessLayer.OrderController;
import BusinessLayer.Supplier;
import ServiceLayer.DummyObjects.DSupplier;
import misc.Pair;

import java.util.HashMap;
import java.util.Set;

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

    public Response makeOrder(int supplierBN,HashMap <Integer,Integer> order,HashMap<Integer, Pair<String,Double>> fixedOrder){
        try {
            cOrder.makeOrder(supplierBN, order, fixedOrder);
            return Response.makeSuccess(null);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }

    }


}
