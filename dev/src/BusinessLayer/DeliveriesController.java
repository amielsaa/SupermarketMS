package BusinessLayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DeliveriesController {
    private int nextDeliveryId;
    private DriversController driversController;
    private SitesController sitesController;
    private TruckController truckController;
    private LinkedHashMap<Integer,Delivery> upcomingDeliveries;
    private LinkedHashMap<Integer,String> deliveryArchive;

    public DeliveriesController(DriversController driversController, SitesController sitesController, TruckController truckController) {
        this.driversController=driversController;
        this.sitesController=sitesController;
        this.truckController=truckController;
        this.upcomingDeliveries=new LinkedHashMap<>();
        this.deliveryArchive=new LinkedHashMap<>();
        nextDeliveryId=1;
    }

    private void checkAvailability(LocalDateTime startTime,LocalDateTime endTime, int truckId, int driverId) throws Exception {
        for(Delivery delivery: upcomingDeliveries.values()){
            if((delivery.getTruck().getPlateNum()==truckId &&
                    isOverlappedIntervals(startTime,endTime,delivery.getStartTime(),delivery.getEndTime()))){
                throw new Exception(String.format("Tuck %d is not available",truckId));
            }
            if(delivery.getDriver().getId()==driverId &&
                            isOverlappedIntervals(startTime,endTime,delivery.getStartTime(),delivery.getEndTime())){
                throw new Exception(String.format("Driver %d is not available",driverId));
            }
        }
    }

    private boolean isOverlappedIntervals(LocalDateTime startTime1,LocalDateTime endTime1,
                                          LocalDateTime startTime2,LocalDateTime endTime2){
        if(startTime1.isBefore(endTime2)&& endTime1.isAfter(startTime2)||
        startTime1.isBefore(endTime2)&&endTime1.isAfter(endTime2)||
        startTime1.isBefore(startTime2)&&endTime1.isAfter(endTime2)||
        startTime1.isAfter(startTime2)&&endTime1.isBefore(endTime2)){
            return true;
        }
        return false;
    }

    public void addDelivery(LocalDateTime startTime,LocalDateTime endTime,int truckId,int driverId,int originId) throws Exception{
        if(startTime.isBefore(LocalDateTime.now())){
            throw new Exception(String.format("%s has passed",startTime));
        }
        if(endTime.isBefore(LocalDateTime.now())){
            throw new Exception(String.format("%s has passed",endTime));
        }
        if(endTime.isBefore(startTime)){
            throw new Exception("end time cant be earlier than the start time");
        }
        Driver driver=driversController.getDriver(driverId);
        if(!truckController.isAbleToDrive(driver.getLicenseType(),truckId)){
            throw new Exception(String.format("Driver whose id number is %d is not permitted to drive the truck %d",driverId,truckId));
        }
        checkAvailability(startTime,endTime,truckId,driverId);
        Truck truck=truckController.getTruck(truckId);
        Site origin=sitesController.getSite(originId);
        upcomingDeliveries.put(nextDeliveryId,new Delivery(nextDeliveryId,startTime,endTime,driver,truck,origin));
        nextDeliveryId++;
    }
    public String getCompletedDelivery(int deliveryId) throws Exception {
        if(!deliveryArchive.containsKey(deliveryId)){
            throw new Exception(String.format("Delivery %d was not found",deliveryId));
        }
        return deliveryArchive.get(deliveryId);
    }
    public Delivery getUpcomingDelivery(int deliveryId) throws Exception {
        if(!upcomingDeliveries.containsKey(deliveryId)){
            throw new Exception(String.format("Delivery %d was not found",deliveryId));
        }
        return upcomingDeliveries.get(deliveryId);
    }
    public ArrayList<Delivery> getUpcomingDeliveries(){
        return new ArrayList<>(upcomingDeliveries.values());
    }
    public ArrayList<String> getDeliveryArchive(){
        return new ArrayList<>(deliveryArchive.values());
    }
    public void addDestination(int deliveryId,int siteId) throws Exception {
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site destination=sitesController.getSite(siteId);
        if(!destination.canBeADestination()){
            throw new Exception(String.format("Site id %d does not belong to a branch",siteId));
        }
        delivery.addDestination((Branch) destination);
    }


    public void removeDestination(int deliveryId,int siteId) throws Exception {
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site destination=sitesController.getSite(siteId);
        if(!destination.canBeADestination()){
            throw new Exception(String.format("Site id %d does not belong to a branch",siteId));
        }
        delivery.removeDestination((Branch) destination);
    }

    public void addItemToDestination(int deliveryId,int siteId, String item, int quantity) throws Exception {
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site destination=sitesController.getSite(siteId);
        if(!destination.canBeADestination()){
            throw new Exception(String.format("Site id %d does not belong to a branch",siteId));
        }
        delivery.addItemToDestination((Branch)destination,item,quantity);
    }
    public void removeItemFromDestination(int deliveryId,int siteId, String item) throws Exception {
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site destination=sitesController.getSite(siteId);
        if(!destination.canBeADestination()){
            throw new Exception(String.format("Site id %d does not belong to a branch",siteId));
        }
        delivery.removeItemFromDestination((Branch)destination,item);
    }

    public void editItemQuantity(int deliveryId,int siteId, String item, int quantity) throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site destination=sitesController.getSite(siteId);
        if(!destination.canBeADestination()){
            throw new Exception(String.format("Site id %d does not belong to a branch",siteId));
        }
        delivery.editItemQuantity((Branch)destination,item,quantity);
    }

    //why did i to this?
    public String getItemsOfDest(int deliveryId,int siteId) throws Exception {
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site destination=sitesController.getSite(siteId);
        if(!destination.canBeADestination()){
            throw new Exception(String.format("Site id %d does not belong to a branch",siteId));
        }
        return delivery.toStringItemsOfDest((Branch) destination);
    }

    public void editStartTime(int deliveryId,LocalDateTime newStartTime)throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        delivery.setStartTime(newStartTime);
    }
    public void editEndTime(int deliveryId,LocalDateTime newEndTime)throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        delivery.setStartTime(newEndTime);
    }

    public void editDriver(int deliveryId,int newDriverId) throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        checkAvailability(delivery.getStartTime(),delivery.getEndTime(),-1,newDriverId);
        Driver driver=driversController.getDriver(newDriverId);
        delivery.setDriver(driver);
    }

    public void editTruck(int deliveryId,int newTruckId) throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        checkAvailability(delivery.getStartTime(),delivery.getEndTime(),newTruckId,-1);
        Truck truck=truckController.getTruck(newTruckId);
        delivery.setTruck(truck);
    }

    public void setOrigin(int deliveryId,int newOriginId) throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site origin=sitesController.getSite(newOriginId);
        delivery.setOrigin(origin);
    }

    public void setWeight(int deliveryId,int weight) throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        if(weight<0)
            throw new Exception("invalid weight");
        delivery.setWeight(weight);
    }

    public void completeDelivery(int deliveryId) throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        if(delivery.getWeight()<=0){
            throw new Exception(String.format("Before completing delivery num. %d, truck's weight must be updated",deliveryId));
        }
        upcomingDeliveries.remove(deliveryId);
        deliveryArchive.put(deliveryId,delivery.toString());
    }
    public void checkTruckHasUpcomingDelivery(int truckId) throws Exception {
        for (Delivery delivery: upcomingDeliveries.values()){
            if(delivery.getTruck().getPlateNum()==truckId){
                throw new Exception(String.format("Truck id %d has upcoming deliveries",truckId));
            }
        }
    }

    public void checkSiteHasUpcomingDelivery(int siteId) throws Exception {
        String site=sitesController.getSite(siteId).getAddress();
        for (Delivery delivery: upcomingDeliveries.values()){
            if(delivery.getOrigin().getId()==siteId){
                throw new Exception(String.format("Site id %s has upcoming deliveries",site));
            }
        }
    }

    public void checkDriverHasUpcomingDelivery(int driverId) throws Exception {
        for (Delivery delivery: upcomingDeliveries.values()){
            if(delivery.getDriver().getId()==driverId){
                throw new Exception(String.format("Driver id %d has upcoming deliveries",driverId));
            }
        }
    }

}
