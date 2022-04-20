package ServiceLayer;

import BusinessLayer.*;
import ServiceLayer.DummyObjects.DOrder;
import ServiceLayer.DummyObjects.DSupplier;
import misc.Pair;

import java.util.HashMap;
import java.util.Set;

public class SupplierService {
    private SupplierController cSupplier = new SupplierController();


    public Response<DSupplier> addSupplier(String name, int business_num, int bank_acc_num, String payment_details, String contactName, int contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name, boolean delivery_by_days, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver){
        try {
            Supplier actualSupplier = cSupplier.addSupplier(name, business_num, bank_acc_num, payment_details, contactName, contactPhone, item_num_to_price, item_num_to_discount, item_num_to_name, delivery_by_days, self_delivery_or_pickup, days_to_deliver);
            return Response.makeSuccess(new DSupplier(actualSupplier));
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }

    }

    public Response<HashMap<Integer, Pair<String,Double>>> makeOrder(int business_num, HashMap<Integer,Integer> order){
        try {
            HashMap<Integer, Pair<String,Double>> itemsAfterDiscount = cSupplier.makeOrder(business_num, order);
            return Response.makeSuccess(itemsAfterDiscount);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }

    }
}
