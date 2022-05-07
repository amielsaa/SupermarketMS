package BusinessLayer;

import misc.Pair;
import DAL.SupplierDAO;
import java.util.HashMap;
import java.util.Set;

public class SupplierController {
    private SupplierDAO supplierDAO;

    public SupplierController() {
        supplierDAO = new SupplierDAO();
    }

    public Supplier addSupplier(String name, int business_num, int bank_acc_num, String payment_details, String contactName, String contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver) throws Exception {
        if (supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("supplier Business number " + business_num + " already exists");
        Supplier newSupplier = new Supplier(name, business_num, bank_acc_num, payment_details, contactName, contactPhone, item_num_to_price, item_num_to_discount, item_num_to_name, self_delivery_or_pickup, days_to_deliver);
        supplierDAO.addSupplierToMap(newSupplier);//adds the supplier to the hashmap of the DAO
        supplierDAO.insertSupplier(newSupplier.getBusiness_Num(),newSupplier.getName(),newSupplier.getBank_Acc_Num(),newSupplier.getPayment_Details().toString(),false,newSupplier.isSelf_Delivery_Or_Pickup());// Adds supplier to the supplier List
        return newSupplier;
    }

    public Supplier removeSupplier(int business_num) {
        Check_If_Supplier_Exists(business_num);
        return BN_To_Supplier.remove(business_num);
    }

    private void Check_If_Supplier_Exists(int business_num) {
        if (!BN_To_Supplier.containsKey(business_num)) {
            throw new IllegalArgumentException("Supplier ID was not found");
        }

    }

    public void updateSupplierDeliveryDays(int business_num, Set<Integer> days) {
        Check_If_Supplier_Exists(business_num);
        BN_To_Supplier.get(business_num).setDays_To_Deliver(days);
    }

    public void updateSupplierSelfDelivery(int business_num, boolean selfDelivery) {
        Check_If_Supplier_Exists(business_num);
        BN_To_Supplier.get(business_num).setSelf_Delivery_Or_Pickup(selfDelivery);
    }

    public void addSupplierContact(int business_num, String contactName, String contactNum) {
        Check_If_Supplier_Exists(business_num);
        BN_To_Supplier.get(business_num).addSupplierContact(contactName, contactNum);


    }

    public void removeSupplierContact(int business_num, String contactNum) {
        Check_If_Supplier_Exists(business_num);
        BN_To_Supplier.get(business_num).removeSupplierContact(contactNum);
    }

    public HashMap<Integer, Pair<String, Double>> makeOrder(int business_num, HashMap<Integer, Integer> order) {
        Check_If_Supplier_Exists(business_num);
        return BN_To_Supplier.get(business_num).makeOrder(order);
    }

    public Supplier getSupplier(int business_num) {
        Check_If_Supplier_Exists(business_num);
        return BN_To_Supplier.get(business_num);
    }

    public QuantityAgreement getSupplierQuantityAgreement(int business_num) {
        Supplier supp = getSupplier(business_num);
        return supp.getQuantity_Agreement();
    }

    public void addSupplierDeliveryDay(int business_num, int day) {
        Check_If_Supplier_Exists(business_num);
        BN_To_Supplier.get(business_num).addSupplierDeliveryDay(day);
    }

    public void removeSupplierDeliveryDay(int business_num, int day) {
        Check_If_Supplier_Exists(business_num);
        BN_To_Supplier.get(business_num).removeSupplierDeliveryDay(day);
    }

    public void updateSupplierPaymentDetails(int business_num, String paymentDetail) {
        Check_If_Supplier_Exists(business_num);
        BN_To_Supplier.get(business_num).setPayment_Details(paymentDetail);

    }

    public void updateSupplierBankAccount(int business_num, int bankAcoount_Num) {
        Check_If_Supplier_Exists(business_num);
        BN_To_Supplier.get(business_num).updateSupplierBankAccount(bankAcoount_Num);

    }

    public void updateContactPhoneNumber(int business_num, String oldPhoneNum, String newPhoneNum) {
        Check_If_Supplier_Exists(business_num);
        BN_To_Supplier.get(business_num).updateContactPhoneNumber(oldPhoneNum, newPhoneNum);



    }
    //------------------------------ForTests---------------------------------------------//
    public boolean HasSupplier(int businessNum){
        return BN_To_Supplier.containsKey(businessNum);


    }
}
