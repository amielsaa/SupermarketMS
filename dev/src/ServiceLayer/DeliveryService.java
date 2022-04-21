package ServiceLayer;

import BusinessLayer.*;

import java.util.ArrayList;
import java.util.Collection;

public class DeliveryService {
    private SitesController sitesController;
    private TruckController truckController;
    private DriversController driversController;
    private DeliveriesController deliveriesController;
    public DeliveryService(){
        sitesController = new SitesController();
        truckController=new TruckController();
        driversController = new DriversController();
        deliveriesController=new DeliveriesController(driversController,sitesController,truckController);
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

    //needs to check is a delivery with this site exists!!!!!!
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
}

