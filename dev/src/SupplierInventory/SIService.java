package SupplierInventory;

import Inventory.ServiceLayer.Service;
import Suppliers.ServiceLayer.DummyObjects.DOrder;
import Suppliers.ServiceLayer.DummyObjects.DQuantityAgreement;
import Suppliers.ServiceLayer.DummyObjects.DRoutineOrder;
import Suppliers.ServiceLayer.DummyObjects.DSupplier;
import Suppliers.ServiceLayer.Response;
import Suppliers.ServiceLayer.SupplierFacade;
import misc.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SIService {
    SupplierFacade fSupplier;
    Service fInventory;

    public SIService() {
        this.fSupplier = new SupplierFacade();
        this.fInventory = new Service();
    }

    //----------------------------------SUPPLIERS--------SUPPLIERS--------SUPPLIERS--------SUPPLIERS----------------------------------
    public void SetStartingValues () {
        fSupplier.SetStartingValues();
    }
    public Response<List<DSupplier>> getAllSuppliers() {
        return fSupplier.getAllSuppliers();
    }

    public Response<DSupplier> addSupplier(String name, int business_num, int bank_acc_num, String payment_details, Set<Integer> days, String contactName, String contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, boolean selfdelivery) {
        return fSupplier.addSupplier(name, business_num, bank_acc_num, payment_details, days, contactName, contactPhone, item_num_to_price, item_num_to_discount, selfdelivery);
    }

    public Response makeOrder(int business_num, HashMap<Pair<String, String>, Integer> order) {
        return fSupplier.makeOrder(business_num,order);
    }

    public Response<DSupplier> getSupplier(int businessNumber) {
        return fSupplier.getSupplier(businessNumber);
    }

    public Response<DQuantityAgreement> getSupplierQuantityAgreement(int businessNumber) {
        return fSupplier.getSupplierQuantityAgreement(businessNumber);
    }

    public Response<DSupplier> removeSupplier(int bn) {
        return fSupplier.removeSupplier(bn);
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
        return fSupplier.updateSupplierPaymentDetails(bn, payment);
    }

    public Response updateSupplierBankAccount(int bn, int bankNumber) {
        return fSupplier.updateSupplierBankAccount(bn, bankNumber);
    }

    public Response updateSupplierSelfDelivery(int bn, boolean selfSelivery) {
        return fSupplier.updateSupplierSelfDelivery(bn, selfSelivery);
    }

    public Response addSupplierContact(int bn, String contactName, String contactPhone) {
        return fSupplier.addSupplierContact(bn, contactName, contactPhone);
    }

    public Response removeSupplierContact(int bn, String contactPhone) {
        return fSupplier.removeSupplierContact(bn, contactPhone);
    }

    public Response<DOrder> getOrder(int bn, int orderID) {
        return fSupplier.getOrder(bn, orderID);
    }

    public Response<List<DOrder>> getAllOrdersFromSupplier(int bn) {
        return fSupplier.getAllOrdersFromSupplier(bn);
    }

    public Response makeRoutineOrder(int business_num, HashMap<Pair<String, String>, Integer> order, Set<Integer> days) {
        return fSupplier.makeRoutineOrder(business_num, order, days);
    }

    public Response<DRoutineOrder> addOrUpdateRoutineOrder(int business_num, int OrderId, String itemName, String ItemProducer, int Quantity) {
        return fSupplier.addOrUpdateRoutineOrder(business_num, OrderId, itemName, ItemProducer, Quantity);
    }

    public Response<DRoutineOrder> deleteItemFromRoutineOrder(int business_num, int OrderId, String ItemName, String ItemProducer) {
        return fSupplier.deleteItemFromRoutineOrder(business_num, OrderId, ItemName, ItemProducer);
    }
    public Response<List<DRoutineOrder>> getAllRoutineOrders(){
        return fSupplier.getAllRoutineOrders();
    }

    public Response MakeOrderToSuppliers(Map<Pair<String, String>, Integer> DemandedSupplies) {
        return fSupplier.MakeOrderToSuppliers(DemandedSupplies);

    }

    public Response<List<DRoutineOrder>> getAllRoutineOrdersForTomorrow() {
        return fSupplier.getAllRoutineOrdersForTomorrow();
    }


    //----------------------------------INVENTORY--------INVENTORY--------INVENTORY--------INVENTORY----------------------------------

}
