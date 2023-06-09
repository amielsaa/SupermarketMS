package SupplierInventory;

import Employee.ServiceLayer.Gateway;
import Inventory.BuisnessLayer.Objects.Category;
import Inventory.ServiceLayer.Objects.ProductSL;
import Inventory.ServiceLayer.Objects.Report;
import Inventory.ServiceLayer.Service;
import Suppliers.ServiceLayer.DummyObjects.DOrder;
import Suppliers.ServiceLayer.DummyObjects.DQuantityAgreement;
import Suppliers.ServiceLayer.DummyObjects.DRoutineOrder;
import Suppliers.ServiceLayer.DummyObjects.DSupplier;
import Suppliers.ServiceLayer.SupplierFacade;
import Utilities.Response;
import misc.Pair;

import javax.swing.text.Utilities;
import java.time.LocalDate;
import java.util.*;

public class SIService {
    SupplierFacade fSupplier;
    Service fInventory;
    Gateway gateway;

    public SIService(Gateway gateway) {
        this.gateway = gateway;
        // This comment is added with the gateway field, you can pass it forward to the sub-services or use it as you like.
        // Note: the Gateway will have a pointer to the SIService class
        this.fSupplier = new SupplierFacade();
        this.fInventory = new Service();

    }

    //----------------------------------SUPPLIERS--------SUPPLIERS--------SUPPLIERS--------SUPPLIERS----------------------------------
    //public Response<String> LoadSuppliersData() {fSupplier.}
    public void SetStartingValues () {
        fSupplier.SetStartingValues();
    }
    public Response<List<DSupplier>> getAllSuppliers() {
        return fSupplier.getAllSuppliers();
    }
    public Response<String> addDeliveryToOrder(int bn,int orderId){
        String s="";
        Response<Set<LocalDate>> dates=fSupplier.getDatesForDelivery(bn);
        if(dates.isSuccess()){
        Response<DOrder> order=getOrder(bn,orderId);
            if(order.isSuccess()){
            }
            Response<Boolean> check=fSupplier.checkIfHasDelivery(bn,orderId);
            if(!check.getData()) {
                Response<String> address=fSupplier.getSupplierAddress(bn);
                Response delivery = gateway.getDeliveryService().getData().addDelivery(order.getData(), dates.getData(), address.getData());
                if(delivery.isSuccess()) {
                    fSupplier.setIfHasDeliveryToOrder(bn, orderId);
                }
                else{
                    s= delivery.getMessage()+" on Order Id number "+orderId+" please alert The HR";
                }
            }
            else{
                s="this Order already has a delivery set Or Order Id doesnt Exists";
                return Response.makeFailure(s);
            }

            }
        else{
            s=dates.getMessage();
            return Response.makeFailure(s);
        }
        s="a new delivery is set on Order Id number "+orderId;
        return Response.makeSuccess(s);

    }

    public Response<DSupplier> addSupplier(String name, int business_num, int bank_acc_num, String payment_details, Set<Integer> days, String contactName, String contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, boolean selfdelivery, String deliveryzone, String address) {
        int zone = 0;
        if (deliveryzone.equals("Center"))
            zone = 1;
        if (deliveryzone.equals("North"))
            zone = 2;
        Response a=gateway.getDeliveryService().getData().addSupplierWarehouse(address,zone,contactPhone,contactName);
        if(a.isSuccess())
            return fSupplier.addSupplier(name, business_num, bank_acc_num, payment_details, days, contactName, contactPhone, item_num_to_price, item_num_to_discount, selfdelivery, deliveryzone, address);
        else
            return Response.makeFailure(a.getMessage());
    }

    public Response makeOrder(int business_num, HashMap<Pair<String, String>, Integer> order) {
        Response<String> address=fSupplier.getSupplierAddress(business_num);
        if(address.isSuccess()) {
            Response<Set<LocalDate>> dates=fSupplier.getDatesForDelivery(business_num);
            Response<DOrder> madeorder=fSupplier.makeOrder(business_num, order);
            if(madeorder.isSuccess()){
                Response delivery=gateway.getDeliveryService().getData().addDelivery(madeorder.getData(),dates.getData(),address.getData());
                if(!delivery.isSuccess()){
                   return fSupplier.setIfHasDeliveryToOrder(madeorder.getData().getSupplier_BN(),madeorder.getData().getOrder_Id());
                }
                return Response.makeFailure(delivery.getMessage());
            }
            return madeorder;
        }
        return address;
    }

    public Response<DSupplier> getSupplier(int businessNumber) {
        return fSupplier.getSupplier(businessNumber);
    }

    public Response<DQuantityAgreement> getSupplierQuantityAgreement(int businessNumber) {
        return fSupplier.getSupplierQuantityAgreement(businessNumber);
    }

    public Response<DSupplier> removeSupplier(int bn) {
        Response<String> address=fSupplier.getSupplierAddress(bn);
        if(address.isSuccess()) {
            gateway.getDeliveryService().getData().deleteSite(address.getData());
            return fSupplier.removeSupplier(bn);
        }
        return Response.makeFailure(address.getMessage());
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

    public Response<String> makeRoutineOrder(int business_num, HashMap<Pair<String, String>, Integer> order, Set<Integer> days) {
        Response<String> address=fSupplier.getSupplierAddress(business_num);
        if(address.isSuccess()) {
            Response<DRoutineOrder> madeorder=fSupplier.makeRoutineOrder(business_num, order, days);
            if(madeorder.isSuccess()){
                Response<Set<LocalDate>> date=fSupplier.getNextDateForDelivery(business_num,madeorder.getData().getOrder_Id());
                Response delivery = gateway.getDeliveryService().getData().addDelivery(madeorder.getData(), date.getData(), address.getData());
                if (delivery.isSuccess()) {
                    fSupplier.setIfHasDeliveryToOrder(madeorder.getData().getSupplier_BN(), madeorder.getData().getOrder_Id());
                }
                else
                return Response.makeFailure(delivery.getMessage()+"on Order Id number "+madeorder.getData().getOrder_Id()+ " please alert the HR");

            }
            else
            return Response.makeFailure(madeorder.getMessage());
        }
        else
        return Response.makeFailure(address.getMessage());

        return Response.makeSuccess("RoutineOrder has been made and delivery has been set");
    }
    public Response AddDeliveryForRoutineOrder(int bn,int orderId){
        Response<String> address=fSupplier.getSupplierAddress(bn);
        if(address.isSuccess()) {
            Response<DRoutineOrder> routineOrder = fSupplier.getRoutineOrder(bn, orderId);
            if (routineOrder.isSuccess()) {
                Response<Boolean> check1 = fSupplier.checkIfHasDelivery(bn, orderId);
                Response<Set<LocalDate>> check2 = fSupplier.getNextDateForDelivery(bn, orderId);

                if (!check1.getData()) {
                    Response delivery = gateway.getDeliveryService().getData().addDelivery(routineOrder.getData(), check2.getData(), address.getData());
                    if (delivery.isSuccess()) {
                        fSupplier.setIfHasDeliveryToOrder(bn, orderId);
                        return Response.makeSuccess("RoutineOrder num " + orderId + " was set a delivery for its next date");
                    }
                    return Response.makeFailure(delivery.getMessage() + " on RoutineOrder num Id  " + orderId + " please alert the HR");

                }
                return Response.makeFailure(check1.getMessage());
            }
            return Response.makeFailure(routineOrder.getMessage());

        }
        return Response.makeFailure(address.getMessage());

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

    public void DeleteAll(){
        fSupplier.DeleteAll();
    }
    //-----------------------INVENTORY----------INVENTORY----------INVENTORY---------INVENTORY----------------------------------


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

    public Response<Pair<String,List<DOrder>>> MakeOrderMinQuantity() {
        Response<List<DOrder>> orders= fSupplier.MakeOrderToSuppliers(fInventory.MakeOrderMinQuantity().getData());
        List<DOrder> actualOrders=new ArrayList<>();
        String errors = "";
        if(orders.isSuccess()){
            for(DOrder i: orders.getData()){
                Response<String> address=fSupplier.getSupplierAddress(i.getSupplier_BN());
                Response<Set<LocalDate>> days=fSupplier.getDatesForDelivery(i.getSupplier_BN());
                Response delivery=gateway.getDeliveryService().getData().addDelivery(i,days.getData(),address.getData());
                if(delivery.isSuccess()){
                    fSupplier.setIfHasDeliveryToOrder(i.getSupplier_BN(),i.getOrder_Id());
                    actualOrders.add(i);
                    i.setHasDelivery(true);
                } else{
                    errors+=delivery.getMessage()+" on order id number "+i.getOrder_Id()+"\n";
                }
            }

        }

        return Response.makeSuccess(new Pair(errors,actualOrders));
    }



    // DELIVERY - INVENTORY INTEGRATION
    public Inventory.ServiceLayer.Response<String> ReceiveDelivery(Map<Pair<String,String>,Pair<Double,Integer>> delivery, int bn, int orderID) {
        Response<String> message1 = fSupplier.OrderArrivedAndAccepted(bn, orderID);
        Inventory.ServiceLayer.Response<String> message2 = fInventory.ReceiveDelivery(delivery);
        if(message2.isSuccess())
            return Inventory.ServiceLayer.Response.makeSuccess(message1.getData() + "\n" + message2.getData());
        return Inventory.ServiceLayer.Response.makeFailure(message1.getMessage() + "\n" + message2.getMessage());


    }

    public Inventory.ServiceLayer.Response<Report> ReportPending() {
        return fInventory.ReportPending();
    }

    public Inventory.ServiceLayer.Response<String> AddPendingProducts() {
        return fInventory.AddPendingProducts();
    }

    public Inventory.ServiceLayer.Response<String> AddPendingDefective(String name, String producer, int quantityToReduce) {
        return fInventory.AddPendingDefective(name,producer,quantityToReduce);
    }

    public Inventory.ServiceLayer.Response<String> InsertData() {
        return fInventory.InsertData();
    }

    public Inventory.ServiceLayer.Response<String> LoadProducts() {
        return fInventory.LoadProducts();
    }
    public Inventory.ServiceLayer.Response<String> deleteAllData(){
        return fInventory.deleteAllData();
    }

    public Gateway getGateway(){
        return gateway;
    }
}
