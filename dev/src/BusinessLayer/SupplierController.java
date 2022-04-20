package BusinessLayer;

import java.util.HashMap;
import java.util.Set;

public class SupplierController {
    private HashMap<Integer, Supplier> BN_To_Supplier;

    public SupplierController() {
        BN_To_Supplier = new HashMap<Integer, Supplier>();
    }

    public Supplier addSupplier(String name, int business_num, int bank_acc_num, String payment_details, String contactName, int contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name, boolean delivery_by_days, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver) {
        //todo: return dummy supplier?
        Supplier newSupplier = new Supplier(name, business_num, bank_acc_num, payment_details, contactName, contactPhone, item_num_to_price, item_num_to_discount, item_num_to_name, delivery_by_days, self_delivery_or_pickup, days_to_deliver);
        BN_To_Supplier.put(business_num, newSupplier);
        return newSupplier;
    }

    public Supplier removeSupplier(int business_num) {
        Check_If_Supplier_Exists(business_num);
        return BN_To_Supplier.remove(business_num);
    }

    private void Check_If_Supplier_Exists(int business_num) {
        if (!BN_To_Supplier.containsKey(business_num)) {
            throw new IllegalArgumentException("Order ID was not found");
        }

    }

    private void updateSupplierDeliveryDays(int business_num, Set<Integer> days) {
        Check_If_Supplier_Exists(business_num);
        BN_To_Supplier.get(business_num).setDays_To_Deliver(days);
    }

    private void updateSupplierSelfDelivery(int business_num, boolean selfDelivery) {
        Check_If_Supplier_Exists(business_num);
        BN_To_Supplier.get(business_num).setSelf_Delivery_Or_Pickup(selfDelivery);
    }

    private void addSupplierContact(int business_num, String contactName, int contactNum) {
        Check_If_Supplier_Exists(business_num);
        BN_To_Supplier.get(business_num).addSupplierContact(contactName, contactNum);


    }

    private void removeSupplierContact(int business_num, int contactNum) {
        Check_If_Supplier_Exists(business_num);
        BN_To_Supplier.get(business_num).removeSupplierContact(contactNum);
    }
    private HashMap<Integer,Pair<String,Double>> makeOrder(int business_num,HashMap<Integer,Integer> order){
        Check_If_Supplier_Exists(business_num);
        return  BN_To_Supplier.get(business_num).makeOrder(order);
    }
}
