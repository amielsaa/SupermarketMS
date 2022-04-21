package ServiceLayer;

import BusinessLayer.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class DeliveryService {
    private SitesController sitesController;
    private TruckController truckController;
    private DriversController driversController;
    private DeliveriesController deliveriesController;
    public DeliveryService(boolean loadAllData){
        sitesController = new SitesController();
        truckController=new TruckController();
        driversController = new DriversController();
        deliveriesController=new DeliveriesController(driversController,sitesController,truckController);
        if (loadAllData)
            load();
        else
            loadDrivers();

    }

    public void load(){
        try {
            loadSties();
            loadTrucks();
            loadDrivers();
            loadDeliveries();
        }
        catch (Exception e){

        }
    }

    private void loadSties() throws Exception {
        sitesController.addSupplierWarehouse("Haifa", 0, "054-0000001", "supplier1");
        sitesController.addSupplierWarehouse("Beer Sheva", 2, "054-0000002", "supplier2");
        sitesController.addBranch("Tiberias", 0,"054-0000003","branch1");
        sitesController.addBranch("Tel Aviv", 1,"054-0000004","branch2");
        sitesController.addBranch("Jerusalem", 1,"054-0000005","branch3");
        sitesController.addBranch("Dimona", 2,"054-0000006","branch4");
    }

    private void loadTrucks() throws Exception{
        truckController.addTruck(1000001, "small truck", 9000);
        truckController.addTruck(1000002, "small truck", 9000);
        truckController.addTruck(1000003, "small truck", 9000);
        truckController.addTruck(1000004, "big truck", 14000);
        truckController.addTruck(1000005, "big truck", 14000);
    }

    public void loadDrivers(){
        driversController.addDriver(200000001, "c1 driver1", "C1");
        driversController.addDriver(200000002, "c1 driver2", "C1");
        driversController.addDriver(200000003, "c1 driver3", "C1");
        driversController.addDriver(200000004, "c driver1", "C");
        driversController.addDriver(200000005, "c driver2", "C");
        driversController.addDriver(200000006, "c driver3", "C");
    }

    private void loadDeliveries() throws Exception{
        deliveriesController.addDelivery(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(4), 1000001, 200000001, 1);
        deliveriesController.setWeight(1, 7000);
        deliveriesController.addDestination(1,4);
        deliveriesController.addDestination(1,5);
        deliveriesController.addItemToDestination(1,4,"milk",10);
        deliveriesController.addItemToDestination(1,5,"milk",20);
        deliveriesController.addDelivery(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(4), 1000004, 200000004, 2);
        deliveriesController.setWeight(2, 12500);
        deliveriesController.addDestination(2,6);
        deliveriesController.addItemToDestination(2,6,"eggs",30);
    }

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

    public Response<Site> getAllSite(int id){
        try {
            Site site = sitesController.getSite(id);
            return Response.makeSuccess(site);
        }
        catch (Exception ex){
            return Response.makeFailure(ex.getMessage());
        }
    }

    //truck logic
    public Response addTruck(int plateNum, String model, int maxWeight){
        try{
            truckController.addTruck(plateNum, model, maxWeight);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editPlateNum(int oldPlateNum, int newPlateNum){
        try{
            truckController.editPlateNum(oldPlateNum, newPlateNum);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editModel(int plateNum, String newModel){
        try{
            truckController.editModel(plateNum, newModel);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }
    public Response editMaxWeight(int plateNum, int maxWeight){
        try{
            truckController.editMaxWeight(plateNum, maxWeight);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }


    public Response deleteTruck(int plateNum){
       try{
           deliveriesController.checkTruckHasUpcomingDelivery(plateNum);
            truckController.deleteTruck(plateNum);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }
    public Response<ArrayList<Truck>> getTrucks(){
        return Response.makeSuccess(truckController.getTrucks());
    }

    //****************DeliveryLogic***********************
    public Response addDelivery(LocalDateTime startTime, LocalDateTime endTime, int truckId, int driverId, int originId){
        try{
            deliveriesController.addDelivery(startTime, endTime,truckId,driverId,originId);
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

    public Response addItemToDeliveryDestination(int deliveryId,int siteId, String item, int quantity){
        try {
            deliveriesController.addItemToDestination(deliveryId,siteId,item,quantity);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response removeItemFromDeliveryDestination(int deliveryId,int siteId, String item){
        try {
            deliveriesController.removeItemFromDestination(deliveryId,siteId,item);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editDeliveryItemQuantity(int deliveryId,int siteId, String item, int quantity){
        try {
            deliveriesController.editItemQuantity(deliveryId,siteId,item,quantity);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editDeliveryStartTime(int deliveryId,LocalDateTime newStartTime){
        try {
            deliveriesController.editStartTime(deliveryId,newStartTime);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editDeliveryEndTime(int deliveryId,LocalDateTime newEndTime){
        try {
            deliveriesController.editEndTime(deliveryId,newEndTime);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editDeliveryDriver(int deliveryId,int newDriverId){
        try {
            deliveriesController.editDriver(deliveryId,newDriverId);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response editDeliveryTruck(int deliveryId,int newDriverId){
        try {
            deliveriesController.editTruck(deliveryId,newDriverId);
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
            deliveriesController.completeDelivery(deliveryId);
            return Response.makeSuccess(0);
        }catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }
    }

