package BusinessLayer;

import DAL.*;
import misc.Pair;

import java.util.HashMap;
import java.util.Set;
import java.util.zip.DataFormatException;

public class SupplierController {
    private SupplierDAO supplierDAO;
    private ContactDAO contactDAO;
    private DaysToDeliverDAO daysToDeliverDAO;
    private QuantityAgreementDAO quantityAgreementDAO;
    private DiscountsDAO discountsDAO;

    public SupplierController() {
        supplierDAO = new SupplierDAO();
        contactDAO=new ContactDAO();
        daysToDeliverDAO=new DaysToDeliverDAO();
        quantityAgreementDAO=new QuantityAgreementDAO();
        discountsDAO=new DiscountsDAO();
    }

    public Supplier addSupplier(String name, int business_num, int bank_acc_num, String payment_details, String contactName, String contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name, boolean self_delivery_or_pickup) throws Exception {
        if (supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("supplier Business number " + business_num + " already exists");
        Supplier newSupplier = new Supplier(name, business_num, bank_acc_num, payment_details, contactName, contactPhone, item_num_to_price, item_num_to_discount, item_num_to_name, self_delivery_or_pickup);
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



    public void updateSupplierSelfDelivery(int business_num, boolean selfDelivery) throws DataFormatException {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        supplierDAO.getSupplier(business_num).setSelf_Delivery_Or_Pickup(selfDelivery);
        if (!supplierDAO.updateSupplierSelfDelivery( business_num, selfDelivery))
            throw new DataFormatException("Error In Database on updateSupplierSelfDelivery");
    }

    public void addSupplierContact(int business_num, String contactName, String contactNum) throws DataFormatException {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");

        buildSupplier(business_num).addSupplierContact(contactName, contactNum);
        if (!contactDAO.insertContact(business_num, contactName, contactNum))
            throw new DataFormatException("Error In Database on addSupplierContact");
    }

    public void removeSupplierContact(int business_num, String contactNum) throws DataFormatException {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        buildSupplier(business_num).removeSupplierContact(contactNum);
        if (!contactDAO.deleteContact(business_num, contactNum))
            throw new DataFormatException("Error In Database on removeSupplierContact");
    }

    public HashMap<Pair<String,String>, Pair<Double, Double>> makeOrder(int business_num, HashMap<Pair<String,String>, Integer> order) {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        HashMap<Pair<String,String>, Pair<Double, Double>> toreturn= buildSupplier(business_num).makeOrder(order);
        return toreturn;
    }

    public Supplier getSupplier(int business_num) {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        return  buildSupplier(business_num);
    }

    public QuantityAgreement getSupplierQuantityAgreement(int business_num) {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        return  buildSupplier(business_num).getQuantity_Agreement();
    }


    public void updateSupplierPaymentDetails(int business_num, String paymentDetail) throws DataFormatException {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        buildSupplier(business_num).setPayment_Details(paymentDetail);
        if(!supplierDAO.updateSupplierPaymentDetails(business_num,paymentDetail))
            throw new DataFormatException("Error In Database on updateSupplierPaymentDetails");



    }

    public void updateSupplierBankAccount(int business_num, int bankAcoount_Num) throws DataFormatException {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        buildSupplier(business_num).setBank_Acc_Num(bankAcoount_Num);
        if(!supplierDAO.updateSupplierBankAccount(business_num,bankAcoount_Num))
            throw new DataFormatException("Error In Database on updateSupplierBankAccount");


    }

    /*
    public void updateContactPhoneNumber(int business_num, String oldPhoneNum, String newPhoneNum) throws DataFormatException {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        supplierDAO.getSupplier(business_num).updateContactPhoneNumber(oldPhoneNum, newPhoneNum);
        if(!supplierDAO.updateContactPhoneNumber(business_num,oldPhoneNum,newPhoneNum))
            throw new DataFormatException("Error In Database on updateContactPhoneNumber");
    }
     */





    //------------------------------ForTests---------------------------------------------//
    public boolean HasSupplier(int businessNum){
        return supplierDAO.containsSupplier(businessNum);


    }
    private Supplier buildSupplier(int businessNum){
        Supplier supplier=supplierDAO.getSupplier(businessNum);
        QuantityAgreement quantityAgreement=new QuantityAgreement();
        quantityAgreement.setItem_To_Price(quantityAgreementDAO.selectAllItems(businessNum));
        quantityAgreement.setItem_Num_To_Discount(discountsDAO.selectAllDiscounts(businessNum));
        supplier.setQuantity_Agreement(quantityAgreement);
        supplier.setContacts(contactDAO.selectAllContacts(businessNum));
        return supplier;
    }
}
