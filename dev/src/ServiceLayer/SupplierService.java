package ServiceLayer;

import BusinessLayer.*;
import ServiceLayer.DummyObjects.DOrder;
import ServiceLayer.DummyObjects.DQuantityAgreement;
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

    public Response<DSupplier> getSupplier(int business_num){
        try {
            Supplier actualSupplier = cSupplier.getSupplier(business_num);
            return Response.makeSuccess(new DSupplier(actualSupplier));
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }

    }

    public Response<DQuantityAgreement> getSupplierQuantityAgreement(int business_num){
        try {
            QuantityAgreement actualAgreement = cSupplier.getSupplierQuantityAgreement(business_num);
            return Response.makeSuccess(new DQuantityAgreement(actualAgreement));
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }

    }

    public Response<DSupplier> removeSupplier(int business_num){
        try {
            Supplier actualSupplier = cSupplier.removeSupplier(business_num);
            return Response.makeSuccess(new DSupplier(actualSupplier));
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response addSupplierDeliveryDay(int business_num, int day){
        try {
            cSupplier.addSupplierDeliveryDay(business_num, day);
            return Response.makeSuccess(null);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response removeSupplierDeliveryDay(int business_num, int day){
        try {
            cSupplier.removeSupplierDeliveryDay(business_num, day);
            return Response.makeSuccess(null);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response updateSupplierDeliveryDays(int business_num, Set<Integer> days){
        try {
            cSupplier.updateSupplierDeliveryDays(business_num, days);
            return Response.makeSuccess(null);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response updateSupplierPaymentDetails(int business_num, String payment){
        try {
            cSupplier.updateSupplierPaymentDetails(business_num, payment);
            return Response.makeSuccess(null);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response updateSupplierBankAccount(int business_num, int bankNumber){
        try {
            cSupplier.updateSupplierBankAccount(business_num, bankNumber);
            return Response.makeSuccess(null);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response updateSupplierSelfDelivery(int business_num, boolean selfDelivery){
        try {
            cSupplier.updateSupplierSelfDelivery(business_num, selfDelivery);
            return Response.makeSuccess(null);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response addSupplierContact(int business_num, String contactName, int contactPhone){
        try {
            cSupplier.addSupplierContact(business_num, contactName, contactPhone);
            return Response.makeSuccess(null);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response removeSupplierContact(int business_num, int contactPhone){
        try {
            cSupplier.removeSupplierContact(business_num, contactPhone);
            return Response.makeSuccess(null);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response updateContactPhoneNumber(int business_num, int oldPhone, int newPhone){
        try {
            cSupplier.updateContactPhoneNumber(business_num, oldPhone, newPhone);
            return Response.makeSuccess(null);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }















}
