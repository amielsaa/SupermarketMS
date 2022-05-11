package ServiceLayer;

import BusinessLayer.*;
import ServiceLayer.DummyObjects.DOrder;
import ServiceLayer.DummyObjects.DQuantityAgreement;
import ServiceLayer.DummyObjects.DRoutineOrder;
import ServiceLayer.DummyObjects.DSupplier;
import misc.Pair;


import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SupplierFacade {
    private SupplierService sSupplier;
    private OrderService sOrder;


    public SupplierFacade(){
        sSupplier = new SupplierService();
        sOrder = new OrderService();
    }

    public Response<DSupplier> addSupplier(String name, int business_num, int bank_acc_num, String payment_details,Set<Integer> days, String contactName, String contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name, boolean delivery_by_days, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver){
         Response<DSupplier> res = sSupplier.addSupplier(name, business_num, bank_acc_num, payment_details,days, contactName, contactPhone, item_num_to_price, item_num_to_discount, item_num_to_name, delivery_by_days, self_delivery_or_pickup, days_to_deliver);
         if(res.isSuccess()) {
             //todo: pass on only BN
             sOrder.addSupplier(name, business_num, bank_acc_num, payment_details, contactName, contactPhone, item_num_to_price, item_num_to_discount, item_num_to_name, delivery_by_days, self_delivery_or_pickup, days_to_deliver);
         }
         return res;
    }

    public Response makeOrder(int business_num, HashMap<Pair<String,String>,Integer> order){
        Response<HashMap<Pair<String,String>, Pair<Double,Double>>> resWithHash = sSupplier.makeOrder(business_num, order);
        if(resWithHash.isSuccess()) {
            Response<DOrder> resFromOrder = sOrder.makeOrder(business_num, order, resWithHash.getData());
            return resFromOrder;
        }
        return resWithHash;
    }

    public Response<DSupplier> getSupplier(int businessNumber){
        return sSupplier.getSupplier(businessNumber);
    }

    public Response<DQuantityAgreement> getSupplierQuantityAgreement(int businessNumber){
        return sSupplier.getSupplierQuantityAgreement(businessNumber);
    }

    public Response<DSupplier> removeSupplier(int bn){
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

    public Response updateSupplierPaymentDetails(int bn, String payment){
        return sSupplier.updateSupplierPaymentDetails(bn, payment);
    }

    public Response updateSupplierBankAccount(int bn, int bankNumber){
        return sSupplier.updateSupplierBankAccount(bn, bankNumber);
    }

    public Response updateSupplierSelfDelivery(int bn, boolean selfSelivery){
        return sSupplier.updateSupplierSelfDelivery(bn, selfSelivery);
    }

    public Response addSupplierContact(int bn, String contactName, String contactPhone){
        return sSupplier.addSupplierContact(bn, contactName, contactPhone);
    }

    public Response removeSupplierContact(int bn, String contactPhone){
        return sSupplier.removeSupplierContact(bn, contactPhone);
    }

    public Response<DOrder> getOrder(int bn, int orderID){
        return sOrder.getOrder(bn, orderID);
    }

    public Response<List<DOrder>> getAllOrdersFromSupplier(int bn){
        return sOrder.getAllOrdersFromSupplier(bn);
    }

   /* public Response<List<DOrder>> updateContactPhoneNumber(int bn, String oldPhone, String newPhone){
        return sSupplier.updateContactPhoneNumber(bn, oldPhone, newPhone);
    }

    */
   public Response makeRoutineOrder(int business_num, HashMap<Pair<String,String>,Integer> order,Set<Integer> days){
       Response<HashMap<Pair<String,String>, Pair<Double,Double>>> resWithHash = sSupplier.makeRoutineOrder(business_num, order,days);
       if(resWithHash.isSuccess()) {
           Response<DRoutineOrder> resFromOrder = sOrder.makeRoutineOrder(business_num, order, resWithHash.getData(),days);
           return resFromOrder;
       }
       return resWithHash;
   }
   public Response<List<DSupplier>> getAllSuppliers(){
       return sSupplier.getAllSuppliers();
    }
    //todo: add a function of RoutineOrder-done
    //todo:add a function of amielzz

















}
