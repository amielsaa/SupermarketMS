package SupplierInventory;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.BuisnessLayer.Objects.StoreProduct;
import Inventory.ServiceLayer.Objects.ProductSL;
import Inventory.ServiceLayer.Objects.Report;
import Inventory.ServiceLayer.Service;
import Suppliers.ServiceLayer.DummyObjects.DOrder;
import Suppliers.ServiceLayer.DummyObjects.DQuantityAgreement;
import Suppliers.ServiceLayer.DummyObjects.DRoutineOrder;
import Suppliers.ServiceLayer.DummyObjects.DSupplier;
import Suppliers.ServiceLayer.Response;
import Suppliers.ServiceLayer.SupplierFacade;
import misc.Pair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
        Response<HashMap<Pair<String, String>, Pair<Double, Double>>> resWithHash = sSupplier.makeOrder(business_num, order);
        if (resWithHash.isSuccess()) {
            Response<DOrder> resFromOrder = sOrder.makeOrder(business_num, order, resWithHash.getData());
            return resFromOrder;
        }
        return resWithHash;
    }

    public Response<DSupplier> getSupplier(int businessNumber) {
        return sSupplier.getSupplier(businessNumber);
    }

    public Response<DQuantityAgreement> getSupplierQuantityAgreement(int businessNumber) {
        return sSupplier.getSupplierQuantityAgreement(businessNumber);
    }

    public Response<DSupplier> removeSupplier(int bn) {
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

    public Response updateSupplierPaymentDetails(int bn, String payment) {
        return sSupplier.updateSupplierPaymentDetails(bn, payment);
    }

    public Response updateSupplierBankAccount(int bn, int bankNumber) {
        return sSupplier.updateSupplierBankAccount(bn, bankNumber);
    }

    public Response updateSupplierSelfDelivery(int bn, boolean selfSelivery) {
        return sSupplier.updateSupplierSelfDelivery(bn, selfSelivery);
    }

    public Response addSupplierContact(int bn, String contactName, String contactPhone) {
        return sSupplier.addSupplierContact(bn, contactName, contactPhone);
    }

    public Response removeSupplierContact(int bn, String contactPhone) {
        return sSupplier.removeSupplierContact(bn, contactPhone);
    }

    public Response<DOrder> getOrder(int bn, int orderID) {
        return sOrder.getOrder(bn, orderID);
    }

    public Response<List<DOrder>> getAllOrdersFromSupplier(int bn) {
        return sOrder.getAllOrdersFromSupplier(bn);
    }

    public Response makeRoutineOrder(int business_num, HashMap<Pair<String, String>, Integer> order, Set<Integer> days) {
        Response<HashMap<Pair<String, String>, Pair<Double, Double>>> resWithHash = sSupplier.makeRoutineOrder(business_num, order, days);
        if (resWithHash.isSuccess()) {
            Response<DRoutineOrder> resFromOrder = sOrder.makeRoutineOrder(business_num, order, resWithHash.getData(), days);
            return resFromOrder;
        }
        return resWithHash;
    }

    public Response<DRoutineOrder> addOrUpdateRoutineOrder(int business_num, int OrderId, String itemName, String ItemProducer, int Quantity) {
        Response<HashMap<Pair<String, String>, Pair<Double, Double>>> newItemToAdd = sSupplier.addOrUpdateRoutineOrder(business_num, itemName, ItemProducer, Quantity);
        if (newItemToAdd.isSuccess()) {
            Response<DRoutineOrder> updatedRoutineOrder = sOrder.addOrUpdateRoutineOrder(business_num, OrderId, newItemToAdd.getData(), Quantity);
            return updatedRoutineOrder;
        }
        return Response.makeFailure(newItemToAdd.getMessage());
    }

    public Response<DRoutineOrder> deleteItemFromRoutineOrder(int business_num, int OrderId, String ItemName, String ItemProducer) {
        Response<DSupplier> supplierExists = getSupplier(business_num);
        if (supplierExists.isSuccess()) {
            Response<DRoutineOrder> updatedRoutineOrder = sOrder.deleteItemFromRoutineOrder(business_num, OrderId, ItemName, ItemProducer);
            return updatedRoutineOrder;
        }
        return Response.makeFailure(supplierExists.getMessage());
    }
    public Response<List<DRoutineOrder>> getAllRoutineOrders(){
        return sOrder.getAllRoutineOrders();
    }

    public Response MakeOrderToSuppliers(Map<Pair<String, String>, Integer> DemandedSupplies) {
        //Hashmap{BN-List of pairs-each pair is <quantity,Order>
        Response<HashMap<Integer, HashMap<Pair<String, String>, Pair<Double, Double>>>> OrdersToExecute = sSupplier.MakeOrderToSuppliers(DemandedSupplies);
        if (OrdersToExecute.isSuccess()) {
            Response<List<DOrder>> toreturn = sOrder.MakeOrderToSuppliers(OrdersToExecute.getData(), DemandedSupplies);
            return toreturn;
        }
        return OrdersToExecute;

    }

    public Response<List<DRoutineOrder>> getAllRoutineOrdersForTomorrow() {
        return sOrder.getAllRoutineOrdersForTomorrow();
    }




    //----------------------------------INVENTORY--------INVENTORY--------INVENTORY--------INVENTORY----------------------------------


    public Inventory.ServiceLayer.Response<Integer> SelectStore(int storeId) {
        return fInventory.SelectStore(storeId);
    }

    public Inventory.ServiceLayer.Response<List<ProductSL>> GetAllProducts() {
        return fInventory.GetAllProducts();
    }


    public Inventory.ServiceLayer.Response<String> AddProduct(String name, String producer, double buyingPrice, double sellingPrice, int minquantity, String categories) {
        return fInventory.AddProduct(name,producer,buyingPrice,sellingPrice,minquantity,categories);
    }

    public Inventory.ServiceLayer.Response<String> AddStoreProduct(int id, int quantityInStore, int quantityInWarehouse, String expDate, String locations) {
        return fInventory.AddStoreProduct(id,quantityInStore,quantityInWarehouse,expDate,locations);
    }

    public Inventory.ServiceLayer.Response<Category> AddCategory(String category) {
        return fInventory.AddCategory(category);
    }

    public Inventory.ServiceLayer.Response<String> ChangeCategory(int productId, int categoryIndex, String newCategory) {
        return fInventory.ChangeCategory(productId,categoryIndex,newCategory);
    }


    public Inventory.ServiceLayer.Response<String> AddDefectiveProduct(int productId) {
        return fInventory.AddDefectiveProduct(productId);
    }

    public Inventory.ServiceLayer.Response<String> DeleteProduct(int productId) {
        return fInventory.DeleteProduct(productId);
    }

    public Inventory.ServiceLayer.Response<String> AddDiscountByName(int productId, int discount, String date) {
        return fInventory.AddDiscountByName(productId,discount,date);
    }

    public Inventory.ServiceLayer.Response<String> AddDiscountByCategory(String categoryName, int discount, String date) {
        return fInventory.AddDiscountByCategory(categoryName,discount,date);
    }

    public Inventory.ServiceLayer.Response<Report> ReportByExpired() {
        return fInventory.ReportByExpired();
    }

    public Inventory.ServiceLayer.Response<Report> ReportByDefective() {
        return fInventory.ReportByDefective();
    }

    public Inventory.ServiceLayer.Response<Report> ReportStockByCategory(List<String> categories) {
        return fInventory.ReportStockByCategory(categories);
    }

    public Inventory.ServiceLayer.Response<Report> ReportMinQuantity() {
        return fInventory.ReportMinQuantity();
    }

    //public Inventory.ServiceLayer.Response<String> stopTimer() {return fInventory.StopTimer();}

    public Inventory.ServiceLayer.Response<String> MakeOrderMinQuantity() {
        return fInventory.MakeOrderMinQuantity();
    }



}
