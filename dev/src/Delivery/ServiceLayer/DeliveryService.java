package Delivery.ServiceLayer;
import Delivery.BusinessLayer.*;
import Delivery.DataAccessLayer.CreateClearTables;
import Employee.ServiceLayer.Gateway;
import Inventory.ServiceLayer.Service;
import Suppliers.BusinessLayer.Order;
import Suppliers.ServiceLayer.DummyObjects.DOrder;
import Suppliers.ServiceLayer.SupplierFacade;
import Utilities.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

public class DeliveryService {

    private Gateway employeeMod;
    private Service inventoryMod;
    private SupplierFacade supplierMod;

    private SitesController sitesController;
    private TrucksController trucksController;
    private DriversController driversController;
    private DeliveriesController deliveriesController;

    public DeliveryService(Gateway gateway){
        // INIT OTHER SERVICES
        employeeMod = gateway;
        inventoryMod = new Service(); //TODO: create a proper reference
        supplierMod = new SupplierFacade();  //TODO: create a proper reference
        // -------------------

        sitesController = new SitesController();
        trucksController = new TrucksController();
        driversController = new DriversController();
        deliveriesController = new DeliveriesController(driversController,sitesController, trucksController);
    }
    //############################# Loading Data ###################################################

    //for tests and init after db removal
    public void load(){
        try {
            loadSties();
            loadTrucks();
            loadDeliveries();
        }
        catch (Exception e){}
    }
    private void loadSties() throws Exception {sitesController.load();}
    private void loadTrucks() throws Exception{
        trucksController.load();}
    private void loadDeliveries() throws Exception{deliveriesController.load();}


    //############################# Site Logic ###################################################
    public Response addSupplierWarehouse(String address, int zone, String phoneNumber, String contactName) {
        try {
            sitesController.addSupplierWarehouse(address, zone, phoneNumber, contactName);
            return Response.makeSuccess(0);
        }
        catch (Exception ex){
            return Response.makeFailure(ex.getMessage());
        }
    }

    public Response addBranch(String address, int zone, String phoneNumber, String contactName) {
        try {
            sitesController.addBranch(address, zone, phoneNumber, contactName);
            return Response.makeSuccess(0);
        }
        catch (Exception ex){
            return Response.makeFailure(ex.getMessage());
        }
    }

    public Response editSiteAddress(int id, String address){
        try {
            sitesController.editSiteAddress(id, address);
            return Response.makeSuccess(0);
        }
        catch (Exception ex){
            return Response.makeFailure(ex.getMessage());
        }
    }

    public Response editSiteDeliveryZone(int id, int zone){
        try {
            sitesController.editSiteDeliveryZone(id, zone);
            return Response.makeSuccess(0);
        }
        catch (Exception ex){
            return Response.makeFailure(ex.getMessage());
        }
    }

    public Response editSiteContactName(int id, String name){
        try {
            sitesController.editSiteContactName(id, name);
            return Response.makeSuccess(0);
        }
        catch (Exception ex){
            return Response.makeFailure(ex.getMessage());
        }
    }

    public Response editSitePhoneNumber(int id, String number){
        try {
            sitesController.editSitePhoneNumber(id, number);
            return Response.makeSuccess(0);
        }
        catch (Exception ex){
            return Response.makeFailure(ex.getMessage());
        }
    }

    public Response deleteSite(int id){
        try {
            deliveriesController.checkSiteHasUpcomingDelivery(id);
            sitesController.deleteSite(id);
            return Response.makeSuccess(0);
        }
        catch (Exception ex){
            return Response.makeFailure(ex.getMessage());
        }
    }

    public Response deleteSite(String address){
        try {
            deliveriesController.checkSiteHasUpcomingDelivery(address);
            sitesController.deleteSite(address);
            return Response.makeSuccess(0);
        }
        catch (Exception ex){
            return Response.makeFailure(ex.getMessage());
        }
    }

    public Response<Collection<Site>> getAllSites(){
        try {
            Collection<Site> sites = sitesController.getAllSites();
            return Response.makeSuccess(sites);
        }
        catch (Exception ex){
            return Response.makeFailure(ex.getMessage());
        }
    }

    public Response<Collection<Site>> viewSitesPerZone(int zone){
        try {
            Collection<Site> sites = sitesController.viewSitesPerZone(zone);
            return Response.makeSuccess(sites);
        }
        catch (Exception ex){
            return Response.makeFailure(ex.getMessage());
        }
    }

    public Response<Site> getSite(int id){
        try {
            Site site = sitesController.getSite(id);
            return Response.makeSuccess(site);
        }
        catch (Exception ex){
            return Response.makeFailure(ex.getMessage());
        }
    }

    public Response<String> getDeliveryZoneName(int zone){
        try {
            return Response.makeSuccess(sitesController.getDeliveryZoneName(zone));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<ArrayList<String>> getDestList(){
        ArrayList<String> list=new ArrayList<>();
        for(Site site:sitesController.getAllDestinations()){
            list.add(site.toString());
        }
        return Response.makeSuccess(list);
    }


    //#############################Driver Logic###################################################
    public Response<String> getDriver(int id){
        try {
            return Response.makeSuccess(driversController.getDriver(id).toString());
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }
    public Response<ArrayList<String>> getDriverList(){
        ArrayList<String> driverList=new ArrayList<>();
        for(Driver driver:driversController.getAllDrivers()){
            driverList.add(driver.toString());
        }
        return Response.makeSuccess(driverList);
    }

    public Response addDriver(int id, String name, String licenseType){
        try{
            driversController.addDriver(id, name, licenseType);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response deleteDriver(int id){
        try{
            deliveriesController.checkDriverHasUpcomingDelivery(id);
            driversController.deleteDriver(id);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editDriverName(int id, String name){
        try{
            driversController.changeName(id, name);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editDriverLicenseType(int id, String licenseType){
        try{
            deliveriesController.validateLicenseTypeChange(id,licenseType);
            driversController.ChangeLicenseType(id, licenseType);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }


    //############################# Truck Logic ###################################################
    public Response addTruck(int plateNum, String model, int maxWeight){
        try{
            trucksController.addTruck(plateNum, model, maxWeight);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editPlateNum(int oldPlateNum, int newPlateNum){
        try{
            trucksController.editPlateNum(oldPlateNum, newPlateNum);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editModel(int plateNum, String newModel){
        try{
            trucksController.editModel(plateNum, newModel);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }
    public Response editMaxWeight(int plateNum, int maxWeight){
        try{
            deliveriesController.validateMaxWeightChange(plateNum,maxWeight);
            trucksController.editMaxWeight(plateNum, maxWeight);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }


    public Response deleteTruck(int plateNum){
       try{
           deliveriesController.checkTruckHasUpcomingDelivery(plateNum);
            trucksController.deleteTruck(plateNum);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }
    public Response<ArrayList<Truck>> getTrucks(){
        return Response.makeSuccess(trucksController.getTrucks());
    }

    public Response<String> getTruck(int plateNum){
        try{
            Truck truck= trucksController.getTruck(plateNum);
            return Response.makeSuccess(truck.toString());
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    //#############################Delivery Logic###################################################

    //manual delivery creation
    public Response addDelivery(LocalDateTime startTime, LocalDateTime endTime, int truckId, int driverId, int originId, int destinationId){
        try{
            Response driverAvailable=checkDriverAvailability(driverId,startTime);
            if(!driverAvailable.isSuccess())
                return driverAvailable;
            deliveriesController.addDelivery(startTime, endTime,truckId,driverId,originId,destinationId);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    //auto delivery creation
    public Response addDelivery(DOrder order, Collection<LocalDate> days){
        try{
            deliveriesController.addDelivery(order, days, employeeMod);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<String> searchCompletedDelivery(int deliveryId){
        try {
            return Response.makeSuccess(deliveriesController.getCompletedDelivery(deliveryId));
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<String> searchUpcomingDelivery(int deliveryId){
        try {
            return Response.makeSuccess(deliveriesController.getUpcomingDelivery(deliveryId).toString());
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<ArrayList<Delivery>> viewUpcomingDeliveries(){
        try {
            return Response.makeSuccess(deliveriesController.getUpcomingDeliveries());
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<ArrayList<String>> viewDeliveryArchive(){
        try {
            return Response.makeSuccess(deliveriesController.getDeliveryArchive());
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response addDestinationToDelivery(int deliveryId,int siteId){
        try {
            deliveriesController.addDestination(deliveryId,siteId);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response removeDestinationFromDelivery(int deliveryId,int siteId){
        try {
            deliveriesController.removeDestination(deliveryId,siteId);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    //removed
    /*public Response addItemToDeliveryDestination(int deliveryId,int siteId, String item, int quantity){
        try {
            deliveriesController.addItemToDestination(deliveryId,siteId,item,quantity);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }*/

    public Response removeItemFromDeliveryDestination(int deliveryId,int siteId, String item, String producer){
        try {
            deliveriesController.removeItemFromDestination(deliveryId,siteId,item, producer);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editDeliveryItemQuantity(int deliveryId,int siteId, String item, String producer, int quantity){
        try {
            deliveriesController.editItemQuantity(deliveryId,siteId,item,producer, quantity);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editDeliveryStartTime(int deliveryId,LocalDateTime newStartTime){
        try {
            Delivery delivery=deliveriesController.getUpcomingDelivery(deliveryId);
            Response driverAvailable=checkDriverAvailability(delivery.getDriverId(),newStartTime);
            if(!driverAvailable.isSuccess())
                return driverAvailable;
            deliveriesController.editStartTime(deliveryId,newStartTime);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editDeliveryEndTime(int deliveryId,LocalDateTime newEndTime){
        try {
            Delivery delivery=deliveriesController.getUpcomingDelivery(deliveryId);
            Response driverAvailable=checkDriverAvailability(delivery.getDriverId(),newEndTime);
            if(!driverAvailable.isSuccess())
                return driverAvailable;
            deliveriesController.editEndTime(deliveryId,newEndTime);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editDeliveryDriver(int deliveryId,int newDriverId){
        try {
            Delivery delivery=deliveriesController.getUpcomingDelivery(deliveryId);
            Response driverAvailable=checkDriverAvailability(newDriverId,delivery.getStartTime());
            if(!driverAvailable.isSuccess())
                return driverAvailable;
            deliveriesController.editDriver(deliveryId,newDriverId);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editDeliveryTruck(int deliveryId,int newTruckId){
        try {
            deliveriesController.editTruck(deliveryId,newTruckId);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }

    }

    public Response editDeliveryOrigin(int deliveryId,int newOriginId){
        try {
            deliveriesController.setOrigin(deliveryId,newOriginId);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editDeliveryWeight(int deliveryId,int weight){
        try {
            deliveriesController.setWeight(deliveryId,weight);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response completeDelivery(int deliveryId){
        try {
            //call invetory.additems here
            employeeMod.getSIService().getData().ReceiveDelivery(deliveriesController.completeDelivery(deliveryId));
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response deleteDelivery(int deliveryId){
        try{
            deliveriesController.deleteDelivery(deliveryId);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    private Response checkDriverAvailability(int driverId,LocalDateTime date){
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        Response<Boolean> r1 = employeeMod.driverAvailableOnShift(date,driverId);
        if(!r1.isSuccess()) {
            return r1;
        }

        if(!r1.getData())
            return Response.makeFailure(String.format("Driver is unavailable at %s...",date.format(dateTimeFormatter)));
        return Response.makeSuccess(0);
    }

    // DB functions

    public void clearDatabases()
    {
        CreateClearTables.clearTables();
        sitesController.resetNextId();
        deliveriesController.resetNextId();
    }

}

