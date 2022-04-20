package ServiceLayer;

import BusinessLayer.Contact;
import BusinessLayer.QuantityAgreement;
import BusinessLayer.Supplier;
import BusinessLayer.SupplierController;

import java.util.HashMap;
import java.util.Set;

public class SupplierService {
    private SupplierController cSupplier = new SupplierController();

    public QuantityAgreement createQuantityAgreement(HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name){ //todo: create dummy
        QuantityAgreement ret = cSupplier.createQuantityAgreement(item_num_to_price, item_num_to_discount, item_num_to_name);
        return ret;
    }

    public Supplier addSupplier(String name, int business_num, int bank_acc_num, String payment_details, Contact contact, QuantityAgreement quantity_agreement, boolean delivery_by_days, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver){ //todo: change it to response
//        Supplier ret = cSupplier.addSupplier(name, business_num, bank_acc_num, payment_details, contact, quantity_agreement, delivery_by_days, self_delivery_or_pickup, days_to_deliver);
        Supplier ret = new Supplier();
        return ret;
    }
}
