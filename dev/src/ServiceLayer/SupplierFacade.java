package ServiceLayer;

import BusinessLayer.*;
import ServiceLayer.DummyObjects.DOrder;
import ServiceLayer.DummyObjects.DQuantityAgreement;
import ServiceLayer.DummyObjects.DRoutineOrder;
import ServiceLayer.DummyObjects.DSupplier;
import misc.Pair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SupplierFacade {
    private SupplierService sSupplier;
    private OrderService sOrder;


    public SupplierFacade() {
        sSupplier = new SupplierService();
        sOrder = new OrderService();
    }

//    public Response<DSupplier> addSupplier(String name, int business_num, int bank_acc_num, String payment_details,Set<Integer> days, String contactName, String contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, boolean self_delivery_or_pickup) {
//        return sSupplier.addSupplier(name, business_num, bank_acc_num, payment_details, days, contactName, contactPhone, item_num_to_price, item_num_to_discount, self_delivery_or_pickup);
//    }

    //--------------------------------------------------------Suppliers----------------------------------------------------------//
    public Response<List<DSupplier>> getAllSuppliers() {
        return sSupplier.getAllSuppliers();
    }

    public Response<DSupplier> addSupplier(String name, int business_num, int bank_acc_num, String payment_details, Set<Integer> days, String contactName, String contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, boolean selfdelivery) {
        Response<DSupplier> res = sSupplier.addSupplier(name, business_num, bank_acc_num, payment_details, days, contactName, contactPhone, item_num_to_price, item_num_to_discount, selfdelivery);
        if (res.isSuccess()) {
            sOrder.addSupplier(business_num);
        }
        return res;
    }

    public Response makeOrder(int business_num, HashMap<Pair<String, String>, Integer> order) {
        Response<HashMap<Pair<String, String>, Pair<Double, Double>>> resWithHash = sSupplier.makeOrder(business_num, order);
        if (resWithHash.isSuccess()) {
            Response<DOrder> resFromOrder = sOrder.makeOrder(business_num, order, resWithHash.getData());
            return resFromOrder;
        }
        return resWithHash;
    }

    public Response<DSupplier> getSupplier(int businessNumber) {
        return sSupplier.getSupplier(businessNumber);
    }

    public Response<DQuantityAgreement> getSupplierQuantityAgreement(int businessNumber) {
        return sSupplier.getSupplierQuantityAgreement(businessNumber);
    }

    public Response<DSupplier> removeSupplier(int bn) {
        return sSupplier.removeSupplier(bn);
    }

    /* public Response addSupplierDeliveryDay(int bn, int day){
        return sSupplier.addSupplierDeliveryDay(bn, day);
    }

    public Response removeSupplierDeliveryDay(int bn, int day){
        return sSupplier.removeSupplierDeliveryDay(bn, day);
    }

    public Response updateSupplierDeliveryDays(int bn, Set<Integer> days){
        return sSupplier.updateSupplierDeliveryDays(bn, days);
    }

    */

    public Response updateSupplierPaymentDetails(int bn, String payment) {
        return sSupplier.updateSupplierPaymentDetails(bn, payment);
    }

    public Response updateSupplierBankAccount(int bn, int bankNumber) {
        return sSupplier.updateSupplierBankAccount(bn, bankNumber);
    }

    public Response updateSupplierSelfDelivery(int bn, boolean selfSelivery) {
        return sSupplier.updateSupplierSelfDelivery(bn, selfSelivery);
    }

    public Response addSupplierContact(int bn, String contactName, String contactPhone) {
        return sSupplier.addSupplierContact(bn, contactName, contactPhone);
    }

    public Response removeSupplierContact(int bn, String contactPhone) {
        return sSupplier.removeSupplierContact(bn, contactPhone);
    }

    public Response<DOrder> getOrder(int bn, int orderID) {
        return sOrder.getOrder(bn, orderID);
    }

    public Response<List<DOrder>> getAllOrdersFromSupplier(int bn) {
        return sOrder.getAllOrdersFromSupplier(bn);
    }

/* public Response<List<DOrder>> updateContactPhoneNumber(int bn, String oldPhone, String newPhone){
    return sSupplier.updateContactPhoneNumber(bn, oldPhone, newPhone);
}

*/


    //----------------------------------------------------------RoutineOrders-----------------------------------------------------------
    public Response makeRoutineOrder(int business_num, HashMap<Pair<String, String>,
            Integer> order, Set<Integer> days) {
        Response<HashMap<Pair<String, String>, Pair<Double, Double>>> resWithHash = sSupplier.makeRoutineOrder(business_num, order, days);
        if (resWithHash.isSuccess()) {
            Response<DRoutineOrder> resFromOrder = sOrder.makeRoutineOrder(business_num, order, resWithHash.getData(), days);
            return resFromOrder;
        }
        return resWithHash;
    }

    public Response addOrUpdateRoutineOrder(int business_num, int OrderId, String itemName, String ItemProducer,
                                            int Quantity) {
        Response<HashMap<Pair<String, String>, Pair<Double, Double>>> newItemToAdd = sSupplier.addOrUpdateRoutineOrder(business_num, itemName, ItemProducer, Quantity);
        if (newItemToAdd.isSuccess()) {
            Response<DRoutineOrder> updatedRoutineOrder = sOrder.addOrUpdateRoutineOrder(business_num, OrderId, newItemToAdd.getData(), Quantity);
            return updatedRoutineOrder;
        }
        return newItemToAdd;
    }

    public Response deleteItemFromRoutineOrder(int business_num, int OrderId, String ItemName, String ItemProducer) {
        Response<DSupplier> supplierExists = getSupplier(business_num);
        if (supplierExists.isSuccess()) {
            Response<DRoutineOrder> updatedRoutineOrder = sOrder.deleteItemFromRoutineOrder(business_num, OrderId, ItemName, ItemProducer);
            return updatedRoutineOrder;
        }
        return supplierExists;
    }

    //-----------------------------------------------------getting In Touch With Supplies--------------------------------------------------//
    public Response MakeOrderToSuppliers(Map<Pair<String, String>, Integer> DemandedSupplies) {
        //Hashmap{BN-List of pairs-each pair is <quantity,Order>
        Response<HashMap<Integer, HashMap<Pair<String, String>, Pair<Double, Double>>>> OrdersToExecute = sSupplier.MakeOrderToSuppliers(DemandedSupplies);
        if (OrdersToExecute.isSuccess()) {
            Response<List<DOrder>> toreturn = sOrder.MakeOrderToSuppliers(OrdersToExecute.getData(), DemandedSupplies);
            return toreturn;
        }
        return OrdersToExecute;





    }
}
