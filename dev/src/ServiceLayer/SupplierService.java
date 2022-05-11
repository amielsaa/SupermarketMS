package ServiceLayer;

import BusinessLayer.*;
import ServiceLayer.DummyObjects.DOrder;
import ServiceLayer.DummyObjects.DQuantityAgreement;
import ServiceLayer.DummyObjects.DSupplier;
import misc.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SupplierService {
    private SupplierController cSupplier = new SupplierController();


    public Response<DSupplier> addSupplier(String name, int business_num, int bank_acc_num, String payment_details, Set<Integer> days, String contactName, String contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name, boolean delivery_by_days, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver) {
        try {
            Supplier actualSupplier = cSupplier.addSupplier(name, business_num, bank_acc_num, payment_details, days, contactName, contactPhone, item_num_to_price, item_num_to_discount, item_num_to_name, self_delivery_or_pickup);
            return Response.makeSuccess(new DSupplier(actualSupplier));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }

    }

    public Response<HashMap<Pair<String, String>, Pair<Double, Double>>> makeOrder(int business_num, HashMap<Pair<String, String>, Integer> order) {
        try {
            HashMap<Pair<String, String>, Pair<Double, Double>> itemsAfterDiscount = cSupplier.makeOrder(business_num, order);
            return Response.makeSuccess(itemsAfterDiscount);
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }

    }

    public Response<DSupplier> getSupplier(int business_num) {
        try {
            Supplier actualSupplier = cSupplier.getSupplier(business_num);
            return Response.makeSuccess(new DSupplier(actualSupplier));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }

    }

    public Response<DQuantityAgreement> getSupplierQuantityAgreement(int business_num) {
        try {
            QuantityAgreement actualAgreement = cSupplier.getSupplierQuantityAgreement(business_num);
            return Response.makeSuccess(new DQuantityAgreement(actualAgreement));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }

    }

    public Response<DSupplier> removeSupplier(int business_num) {
        try {
            Supplier actualSupplier = cSupplier.removeSupplier(business_num);
            return Response.makeSuccess(new DSupplier(actualSupplier));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    /*  public Response addSupplierDeliveryDay(int business_num, int day){
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
  */
    public Response updateSupplierPaymentDetails(int business_num, String payment) {
        try {
            cSupplier.updateSupplierPaymentDetails(business_num, payment);
            return Response.makeSuccess(null);
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response updateSupplierBankAccount(int business_num, int bankNumber) {
        try {
            cSupplier.updateSupplierBankAccount(business_num, bankNumber);
            return Response.makeSuccess(null);
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response updateSupplierSelfDelivery(int business_num, boolean selfDelivery) {
        try {
            cSupplier.updateSupplierSelfDelivery(business_num, selfDelivery);
            return Response.makeSuccess(null);
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response addSupplierContact(int business_num, String contactName, String contactPhone) {
        try {
            cSupplier.addSupplierContact(business_num, contactName, contactPhone);
            return Response.makeSuccess(null);
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response removeSupplierContact(int business_num, String contactPhone) {
        try {
            cSupplier.removeSupplierContact(business_num, contactPhone);
            return Response.makeSuccess(null);
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    /* public Response updateContactPhoneNumber(int business_num, String oldPhone, String newPhone){
         try {
             cSupplier.updateContactPhoneNumber(business_num, oldPhone, newPhone);
             return Response.makeSuccess(null);
         }
         catch (Exception e){
             return Response.makeFailure(e.getMessage());
         }
     }
 */
    public Response<HashMap<Pair<String, String>, Pair<Double, Double>>> makeRoutineOrder(int business_num, HashMap<Pair<String, String>, Integer> order, Set<Integer> days) {
        try {
            HashMap<Pair<String, String>, Pair<Double, Double>> itemsAfterDiscount = cSupplier.makeRoutineOrder(business_num, order, days);
            return Response.makeSuccess(itemsAfterDiscount);
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }

    }


    public Response<List<DSupplier>> getAllSuppliers() {
        try {
            List<DSupplier> toreturn = new ArrayList<>();
            List<Supplier> supplierList=cSupplier.getAllSuppliers();
            for(Supplier i:supplierList){
                toreturn.add(new DSupplier(i));
            }
            return Response.makeSuccess(toreturn);
        }
        catch(Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<HashMap<Pair<String, String>, Pair<Double, Double>>> addOrUpdateRoutineOrder(int business_num, String itemName, String itemProducer, int quantity) {
        try{
            HashMap<Pair<String, String>, Pair<Double, Double>> itemtoAdd=cSupplier.addOrUpdateRoutineOrder(business_num,itemName,itemProducer,quantity);
            return Response.makeSuccess(itemtoAdd);
        }
        catch (Exception e){
            return Response.makeFailure(e.getMessage());

        }
    }
}
