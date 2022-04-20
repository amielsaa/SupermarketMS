package ServiceLayer;

import BusinessLayer.Contact;
import BusinessLayer.QuantityAgreement;
import BusinessLayer.Supplier;
import BusinessLayer.SupplierController;

import java.util.HashMap;
import java.util.Set;

public class SupplierService {
    private SupplierController cSupplier = new SupplierController();


    public Response<Supplier> addSupplier(String name, int business_num, int bank_acc_num, String payment_details, String contactName, int contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name, boolean delivery_by_days, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver){ //todo: change it to response
        //todo: replace it with SupplierDummy
        try {
            Supplier actualSupplier = cSupplier.addSupplier(name, business_num, bank_acc_num, payment_details, contactName, contactPhone, item_num_to_price, item_num_to_discount, item_num_to_name, delivery_by_days, self_delivery_or_pickup, days_to_deliver);
//            SupplierDummy ret = new SupplierDummy();
//            return Response.makeSuccess(ret);
            //todo: build supplierDummy and return it in response
            return Response.makeSuccess(actualSupplier);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }

    }
}
