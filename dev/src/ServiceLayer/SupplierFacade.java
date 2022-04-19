package ServiceLayer;

import BusinessLayer.*;


import java.util.List;
import java.util.Set;

public class SupplierFacade {
    private SupplierService sSupplier = new SupplierService();
    private OrderService sOrder = new OrderService();


    public Response<Boolean> addSupplier(){}
    //------------------------supplierService------------------------
    //addSupplier, removeSupplier, updateSupplierDeliveryDays, updateSupplierSelfDelivery, addSupplierContact, removeSupplierContact,
    //createQuantityAgreement

    //------------------------orderService------------------------
    //makeOrder, getOrder, getAllOrdersFromSupplier, getFinalPrice

}
