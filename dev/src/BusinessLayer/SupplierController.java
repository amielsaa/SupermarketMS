package BusinessLayer;

import java.util.HashMap;
import java.util.Set;

public class SupplierController {
    private HashMap<Integer,Supplier> BN_To_Supplier;

    public SupplierController(){
        BN_To_Supplier=new HashMap<Integer,Supplier>();
    }
    public Supplier addSupplier(String name, int business_num, int bank_acc_num, String payment_details, Contact contact, QuantityAgreement quantity_agreement, boolean delivery_by_days, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver){
        //todo: return dummy supplier?
        Supplier newSupplier=new Supplier(name,business_num,bank_acc_num,payment_details,contact,quantity_agreement,delivery_by_days,self_delivery_or_pickup,days_to_deliver);
        BN_To_Supplier.put(business_num,newSupplier);
    }
    public QuantityAgreement createQuantityAgreement(HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name){
        QuantityAgreement ret = new QuantityAgreement(item_num_to_price, item_num_to_discount, item_num_to_name);
        return ret;
    }
}
