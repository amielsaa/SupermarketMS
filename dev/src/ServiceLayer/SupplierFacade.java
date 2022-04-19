package ServiceLayer;

import BusinessLayer.*;


import java.util.List;
import java.util.Set;

public class SupplierFacade {
    private SupplierService sSupplier = new SupplierService();
    private OrderService sOrder = new OrderService();

    //todo: 1. all main functions
    //todo: 2. loadData (with turn on/off mechanism)


//    public Response<Boolean> addSupplier(){}
    //------------------------supplierService------------------------
    //addSupplier, removeSupplier, updateSupplierDeliveryDays, updateSupplierSelfDelivery, addSupplierContact, removeSupplierContact,
    //createQuantityAgreement

    //------------------------orderService------------------------
    //makeOrder, getOrder, getAllOrdersFromSupplier, getFinalPrice

}
