package ServiceLayer;

import BusinessLayer.*;


import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SupplierFacade {
    private SupplierService sSupplier;
    private OrderService sOrder;

    //todo: 1. all main functions
    //todo: 2. loadData (with turn on/off mechanism)

    public SupplierFacade(){
        sSupplier = new SupplierService();
        sOrder = new OrderService();
    }

    public Response<Supplier> addSupplier(String name, int business_num, int bank_acc_num, String payment_details, String contactName, int contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name, boolean delivery_by_days, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver){ //todo: change it to response
        return sSupplier.addSupplier(name, business_num, bank_acc_num, payment_details, contactName, contactPhone, item_num_to_price, item_num_to_discount, item_num_to_name, delivery_by_days, self_delivery_or_pickup, days_to_deliver);
    }

//    public Supplier addSupplier(String name, int business_num, int bank_acc_num, String payment_details, Contact contact, QuantityAgreement quantity_agreement, boolean delivery_by_days, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver){ //todo: change it to response
//        Supplier ret = sSupplier.createQuantityAgreement(name, business_num, bank_acc_num, payment_details, contact, quantity_agreement, delivery_by_days, self_delivery_or_pickup, days_to_deliver);
//        return ret;
//    }





//    public Response<Boolean> addSupplier(){}
    //------------------------supplierService------------------------
    //addSupplier, removeSupplier, updateSupplierDeliveryDays, updateSupplierSelfDelivery, addSupplierContact, removeSupplierContact,
    //createQuantityAgreement

    //------------------------orderService------------------------
    //makeOrder, getOrder, getAllOrdersFromSupplier, getFinalPrice

}
