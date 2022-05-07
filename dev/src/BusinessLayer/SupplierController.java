package BusinessLayer;

import misc.Pair;
import DAL.SupplierDAO;
import java.util.HashMap;
import java.util.Set;
import java.util.zip.DataFormatException;

public class SupplierController {
    private SupplierDAO supplierDAO;

    public SupplierController() {
        supplierDAO = new SupplierDAO();
    }

    public Supplier addSupplier(String name, int business_num, int bank_acc_num, String payment_details, String contactName, String contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver) throws Exception {
        if (supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("supplier Business number " + business_num + " already exists");
        Supplier newSupplier = new Supplier(name, business_num, bank_acc_num, payment_details, contactName, contactPhone, item_num_to_price, item_num_to_discount, item_num_to_name, self_delivery_or_pickup, days_to_deliver);
        if(!supplierDAO.addNewSupplier(newSupplier))
            throw new DataFormatException("Error In Database on addSupplier");
        return newSupplier;
    }

    public Supplier removeSupplier(int business_num) throws DataFormatException {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        Supplier removedSupplier=supplierDAO.getSupplier(business_num);
        if(!supplierDAO.removeSupplier(business_num))
            throw new DataFormatException("Error In Database on RemoveSupplier");
        return removedSupplier;

    }


    public void updateSupplierDeliveryDays(int business_num, Set<Integer> days) throws DataFormatException {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        supplierDAO.getSupplier(business_num).setDays_To_Deliver(days);
        if (!supplierDAO.updateSupplierDeliveryDays( business_num, days))
            throw new DataFormatException("Error In Database on updateSupplierDeliveryDays");
    }

    public void updateSupplierSelfDelivery(int business_num, boolean selfDelivery) throws DataFormatException {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        supplierDAO.getSupplier(business_num).setSelf_Delivery_Or_Pickup(selfDelivery);
        if (!supplierDAO.updateSupplierSelfDelivery( business_num, selfDelivery))
            throw new DataFormatException("Error In Database on updateSupplierSelfDelivery");
    }

    public void addSupplierContact(int business_num, String contactName, String contactNum) {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        BN_To_Supplier.get(business_num).addSupplierContact(contactName, contactNum);


    }

    public void removeSupplierContact(int business_num, String contactNum) {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        BN_To_Supplier.get(business_num).removeSupplierContact(contactNum);
    }

    public HashMap<Integer, Pair<String, Double>> makeOrder(int business_num, HashMap<Integer, Integer> order) {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        return BN_To_Supplier.get(business_num).makeOrder(order);
    }

    public Supplier getSupplier(int business_num) {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        return BN_To_Supplier.get(business_num);
    }

    public QuantityAgreement getSupplierQuantityAgreement(int business_num) {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        return supp.getQuantity_Agreement();
    }

    public void addSupplierDeliveryDay(int business_num, int day) {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        BN_To_Supplier.get(business_num).addSupplierDeliveryDay(day);
    }

    public void removeSupplierDeliveryDay(int business_num, int day) {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        BN_To_Supplier.get(business_num).removeSupplierDeliveryDay(day);
    }

    public void updateSupplierPaymentDetails(int business_num, String paymentDetail) {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        BN_To_Supplier.get(business_num).setPayment_Details(paymentDetail);

    }

    public void updateSupplierBankAccount(int business_num, int bankAcoount_Num) {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        BN_To_Supplier.get(business_num).updateSupplierBankAccount(bankAcoount_Num);

    }

    public void updateContactPhoneNumber(int business_num, String oldPhoneNum, String newPhoneNum) {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        BN_To_Supplier.get(business_num).updateContactPhoneNumber(oldPhoneNum, newPhoneNum);



    }
    //------------------------------ForTests---------------------------------------------//
    public boolean HasSupplier(int businessNum){
        return supplierDAO.containsSupplier(businessNum);


    }
}