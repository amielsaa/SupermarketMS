package Suppliers.BusinessLayer;


import Suppliers.DAL.*;
import misc.Days;
import misc.Pair;

import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.zip.DataFormatException;

public class SupplierController {
    private SupplierDAO supplierDAO;
    private ContactDAO contactDAO;
//    private DaysToDeliverDAO daysToDeliverDAO;
    private QuantityAgreementDAO quantityAgreementDAO;
    private DiscountsDAO discountsDAO;
    private SupplierDaysDAO supplierDaysDAO;

    public SupplierController() {
        supplierDAO = new SupplierDAO();
        contactDAO=new ContactDAO();
//        daysToDeliverDAO=new DaysToDeliverDAO();
        quantityAgreementDAO=new QuantityAgreementDAO();
        discountsDAO=new DiscountsDAO();
        supplierDaysDAO=new SupplierDaysDAO();
    }

    public Supplier addSupplier(String name, int business_num, int bank_acc_num, String payment_details,Set<Integer> days, String contactName, String contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, boolean self_delivery_or_pickup, String deliveryzone, String address) throws Exception {
        if (supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("supplier Business number " + business_num + " already exists");
        Supplier newSupplier = new Supplier(name, business_num, bank_acc_num, payment_details, days, contactName, contactPhone, item_num_to_price, item_num_to_discount, self_delivery_or_pickup, deliveryzone, address);
        if(!supplierDAO.addNewSupplier(newSupplier))
            throw new DataFormatException("Error In Database on addSupplier");
        if(!insertQAAndContactsAndDays(business_num,newSupplier.getQuantity_Agreement(),newSupplier.getContacts(),days))
            throw new DataFormatException("Error In Database on addSupplier on adding QA and contacts");
        return newSupplier;
    }

    public Supplier removeSupplier(int business_num) throws DataFormatException {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        Supplier removedSupplier=supplierDAO.getSupplier(business_num);
        if(!supplierDAO.removeSupplier(business_num)&&deleteSupplier(business_num))
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

    public HashMap<Pair<String,String>, Pair<Double, Double>> makeOrder(int business_num, HashMap<Pair<String,String>,Integer> order) {
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
    //------------------------------------------------------------------------------------//

    }
    private Supplier buildSupplier(int businessNum){
        Supplier supplier=supplierDAO.getSupplier(businessNum);
        QuantityAgreement quantityAgreement=new QuantityAgreement();
        quantityAgreement.setItem_To_Price(quantityAgreementDAO.selectAllItems(businessNum));
        quantityAgreement.setItem_Num_To_Discount(discountsDAO.selectAllDiscounts(businessNum));
        supplier.setQuantity_Agreement(quantityAgreement);
        supplier.setContacts(contactDAO.selectAllContacts(businessNum));
        Collection<Days> days=supplierDaysDAO.selectAllSupplierDays(businessNum);
        Set<Days> daystoSet=new HashSet<>();
        for(Days i:days){
            daystoSet.add(i);
        }
        supplier.normalDaysSetter(daystoSet);
        return supplier;
    }
    private boolean insertQAAndContactsAndDays (int bn,QuantityAgreement quantityAgreement, List<Contact> contacts,Set<Integer> days) throws DataFormatException {

            //setting to get the item-price in data.
            HashMap<Pair<String,String>, Double> item_To_Price=quantityAgreement.getItem_To_Price();
            Pair<String,String>[] itemKeys = new Pair[item_To_Price.keySet().toArray().length];
            for(int i=0; i<itemKeys.length;i++) {
                itemKeys[i] = (Pair) item_To_Price.keySet().toArray()[i];
            }
            //gets in the data
            for (Pair i:itemKeys) {
                if(!quantityAgreementDAO.insertQuantityAgreement(bn,i.getFirst().toString(),i.getSecond().toString(),item_To_Price.get(i)))
                return false;
            }
            //setting to get item-discounts in data.
            //first is the keys to the discounts
             HashMap<Pair<String,String>,HashMap<Integer,Integer>> item_Num_To_Quantity_To_Discount=quantityAgreement.getItem_Num_To_Discount();
             Pair<String,String>[] discountKeys = new Pair[item_Num_To_Quantity_To_Discount.keySet().toArray().length];
             if(discountKeys.length!=0) {
                 int k=0;
                 for (int i = 0; i < itemKeys.length; i++) {
                    if(item_Num_To_Quantity_To_Discount.containsKey(itemKeys[i])) {
                        discountKeys[k] = (Pair) item_Num_To_Quantity_To_Discount.keySet().toArray()[k];
                        k++;
                    }
                 }
                 for (Pair i : discountKeys) {
                     //getting all the discounts for the pair key.
                     Integer[] discounts = new Integer[item_Num_To_Quantity_To_Discount.get(i).keySet().toArray().length];
                     for (int j = 0; j < discounts.length; j++) {
                         discounts[j] = (Integer) item_Num_To_Quantity_To_Discount.get(i).keySet().toArray()[j];
                         //gets in the data.
                         if (!discountsDAO.insertDiscount(bn, i.getFirst().toString(), i.getSecond().toString(), discounts[j], item_Num_To_Quantity_To_Discount.get(i).get(discounts[j])))
                             return false;
                     }

                 }
             }
             for(Contact i:contacts){
                 if(!contactDAO.insertContact(bn,i.getName(),i.getPhone_Num()))
                     return false;
             }
             for(Integer i:days){
                 if(!supplierDaysDAO.insertSupplierDays(bn,i))
                     return false;
             }

    return true;
    }

    public HashMap<Pair<String, String>, Pair<Double, Double>> makeRoutineOrder(int business_num, HashMap<Pair<String, String>, Integer> order, Set<Integer> days) {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        HashMap<Pair<String,String>, Pair<Double, Double>> toreturn= buildSupplier(business_num).makerRoutineOrder(order,days);
        return toreturn;
    }

    public List<Supplier> getAllSuppliers() {
    supplierDAO.loadAllSuppliers();
    Collection<Supplier> suppliersToBuild=supplierDAO.getAllSuppliers();
    List<Supplier> toreturn=new ArrayList<>();
    for(Supplier i:suppliersToBuild){
        toreturn.add(buildSupplier(i.getBusiness_Num()));
    }
    return toreturn;
    }

    public HashMap<Pair<String, String>, Pair<Double, Double>> addOrUpdateRoutineOrder(int business_num, String itemName, String itemProducer, int quantity) {
        if(!supplierDAO.containsSupplier(business_num))
            throw new IllegalArgumentException("Supplier was not found");
        Supplier supplier=buildSupplier(business_num);
        HashMap<Pair<String,String>,Integer> itemToAdd=new HashMap<>();
        itemToAdd.put(new Pair<>(itemName,itemProducer),quantity);
        return supplier.makeOrder(itemToAdd);

    }

    public HashMap<Integer, HashMap<Pair<String, String>, Pair<Double, Double>>> MakeOrderToSuppliers(Map<Pair<String, String>, Integer> demandedSupplies) {
        //complicated function needs an Explanation by steps
        //1.getting all the suppliers in a List--------------------------------------
        supplierDAO.loadAllSuppliers();
        Collection<Supplier> UnbulitSuppliers=supplierDAO.getAllSuppliers();
        List<Supplier> builtSuppliers=new ArrayList<>();
        for(Supplier i:UnbulitSuppliers){
            builtSuppliers.add(buildSupplier(i.getBusiness_Num()));
        }
        //---------------------------------------------------------------------------
        //2.building a List of all the keys of demanded supplies
        Pair[] orderKeys = new Pair[demandedSupplies.keySet().toArray().length];// array of the keys of the order
        for(int i=0; i<orderKeys.length;i++) {
            orderKeys[i] = (Pair) demandedSupplies.keySet().toArray()[i];
        }
        //----------------------------------------------------------------------------
        //3.bullding a Hashmap of <Item,List of suppliers>
        HashMap<Pair<String,String>,List<Supplier>> itemToSuppliers=new HashMap<>();
        for(Pair i:orderKeys){
            List<Supplier> suppliersForItem=new ArrayList<>();
            for(Supplier j:builtSuppliers){
                if(j.getQuantity_Agreement().getItem_To_Price().containsKey(i))
                    suppliersForItem.add(j);
            }
            itemToSuppliers.put(i,suppliersForItem);
        }
        //----------------------------------------------------------------------------
        //4.Making a hashmap of <Supplier,Hashmap Order he will preform>
        HashMap<Integer,HashMap<Pair<String, String>, Pair<Double, Double>>> suplliertoTheirOrder=new HashMap<>();
        for(Pair i:orderKeys){
            if(!itemToSuppliers.get(i).isEmpty()) {
                Double finalPrice = Double.MAX_VALUE;
                int supplierBn = 0;
                HashMap<Pair<String, String>, Pair<Double, Double>> currentLeadingOrder = new HashMap<>();
                List<Supplier> suppliersThatDeliver = itemToSuppliers.get(i);
                //4.1 finding the Best supplier to supply a specific item
                for (Supplier j : suppliersThatDeliver) {
                    HashMap<Pair<String, String>, Integer> HashmapforOrderCheck = new HashMap<>();
                    HashmapforOrderCheck.put(i, demandedSupplies.get(i));
                    HashMap<Pair<String, String>, Pair<Double, Double>> order = j.makeOrder(HashmapforOrderCheck);
                    if (order.get(i).getSecond() < finalPrice) {
                        finalPrice = order.get(i).getSecond();
                        supplierBn = j.getBusiness_Num();
                        currentLeadingOrder = order;
                    }
                }
                //4.2 after finding the best supplier adding him to  supplierToTheirOrder Hashmap
                if (!suplliertoTheirOrder.containsKey(supplierBn)) {
                    suplliertoTheirOrder.put(supplierBn, currentLeadingOrder);
                } else {
                    suplliertoTheirOrder.get(supplierBn).put(i, currentLeadingOrder.get(i));
                }
            }

        }
        return suplliertoTheirOrder;


    }


    public void DeleteAll() {
        contactDAO.deleteAll();
        quantityAgreementDAO.deleteAll();
        discountsDAO.deleteAll();
        supplierDaysDAO.deleteAll();
        supplierDAO.deleteAll();
        supplierDAO.clearAll();
    }

    private boolean deleteSupplier(int bn){
        if(!contactDAO.deleteAllContactsForSupplier(bn))
            return false;
        if (!discountsDAO.deleteAllSupplierDiscounts(bn))
            return false;
        if (!quantityAgreementDAO.deleteSupplierQuantityAgreement(bn))
            return false;
        if(!supplierDaysDAO.deleteAllSupplierDays(bn))
            return false;

        return true;



    }
    public Set<LocalDate> getDatesForDelivery(int bn) {
        Supplier supplier=getSupplier(bn);
        return supplier.getDatesForDelivery();

    }
}

