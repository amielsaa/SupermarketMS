package BusinessLayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DeliveriesController {
    private int nextDeliveryId;
    private DriversController driversController;
    private SitesController sitesController;
    private TrucksController trucksController;
    private LinkedHashMap<Integer,Delivery> upcomingDeliveries;
    private LinkedHashMap<Integer,String> deliveryArchive;

    public DeliveriesController(DriversController driversController, SitesController sitesController, TrucksController trucksController) {
        this.driversController=driversController;
        this.sitesController=sitesController;
        this.trucksController = trucksController;
        this.upcomingDeliveries=new LinkedHashMap<>();
        this.deliveryArchive=new LinkedHashMap<>();
        nextDeliveryId=1;
    }

    public void load() throws Exception {
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
       addDelivery(LocalDateTime.parse("13-10-2023 13:10",dateTimeFormatter),
               LocalDateTime.parse("13-10-2023 15:10",dateTimeFormatter),
               1000001,
               200000001,
               1,5);
        setWeight(1, 7000);
        addDestination(1,4);
        addItemToDestination(1,4,"milk",10);
        addItemToDestination(1,5,"milk",20);
        addDelivery(LocalDateTime.parse("14-10-2023 13:10",dateTimeFormatter),LocalDateTime.parse("14-10-2023 15:10",dateTimeFormatter), 1000004, 200000004, 2,6);
        setWeight(2, 12500);
        addDestination(2,4);
        addItemToDestination(2,6,"eggs",30);
        addItemToDestination(2,6,"milk",30);
        addItemToDestination(2,4,"coffee",30);
        addItemToDestination(2,4,"tea",30);

        //completeDelivery(2);
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
        return startTime1.isBefore(endTime2) && endTime1.isAfter(startTime2) ||
                startTime1.isBefore(endTime2) && endTime1.isAfter(endTime2) ||
                startTime1.isBefore(startTime2) && endTime1.isAfter(endTime2) ||
                startTime1.isAfter(startTime2) && endTime1.isBefore(endTime2);
    }

    public void addDelivery(LocalDateTime startTime,LocalDateTime endTime,int truckId,int driverId,int originId,int destinationId) throws Exception{
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        if(startTime.isBefore(LocalDateTime.now())){
            throw new Exception(String.format("%s has passed",startTime.format(dateTimeFormatter)));
        }
        if(endTime.isBefore(LocalDateTime.now())){
            throw new Exception(String.format("%s has passed",endTime.format(dateTimeFormatter)));
        }
        if(endTime.isBefore(startTime)){
            throw new Exception("end time cant be earlier than the start time");
        }
        Driver driver=driversController.getDriver(driverId);
        if(!trucksController.isAbleToDrive(driver.getLicenseType(),truckId)){
            throw new Exception(String.format("Driver whose id number is %d is not permitted to drive the truck %d",driverId,truckId));
        }
        LocalDateTime midDay=startTime.withMinute(0).withHour(12);
        LocalDateTime midNight=startTime.withMinute(59).withHour(23);
        if(startTime.isBefore(midDay) && !endTime.isBefore(midDay)){
            throw new Exception(String.format("Delivery must be between %s and %s..",startTime.format(dateTimeFormatter),midDay.format(dateTimeFormatter)));
        }else if(startTime.isAfter(midDay) && endTime.isAfter(midNight)){
            throw new Exception(String.format("Delivery must be between %s and %s..",startTime.format(dateTimeFormatter),midNight.format(dateTimeFormatter)));
        }
        checkAvailability(startTime,endTime,truckId,driverId);
        Truck truck= trucksController.getTruck(truckId);
        Site origin=sitesController.getSite(originId);
        Site destination=sitesController.getSite(destinationId);
        if(!destination.canBeADestination()){
            throw new Exception(String.format("Site %d is not a destination...",destinationId));
        }

        upcomingDeliveries.put(nextDeliveryId,new Delivery(nextDeliveryId,startTime,endTime,driver,truck,origin,(Branch)destination));
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
            throw new Exception(String.format("Site id %d is not a destination...",siteId));
        }
        delivery.addDestination((Branch) destination);
    }


    public void removeDestination(int deliveryId,int siteId) throws Exception {
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site destination=sitesController.getSite(siteId);
        if(!(destination instanceof Branch)) throw new Exception(String.format("Site id %d is not a destination...",siteId));
        delivery.removeDestination((Branch) destination);
    }

    public void addItemToDestination(int deliveryId,int siteId, String item, int quantity) throws Exception {
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site destination=sitesController.getSite(siteId);
        if(!destination.canBeADestination()){
            throw new Exception(String.format("Site id %d is not a destination...",siteId));
        }
        delivery.addItemToDestination((Branch)destination,item,quantity);
    }
    public void removeItemFromDestination(int deliveryId,int siteId, String item) throws Exception {
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site destination=sitesController.getSite(siteId);
        if(!destination.canBeADestination()){
            throw new Exception(String.format("Site id %d is not a destination...",siteId));
        }
        delivery.removeItemFromDestination((Branch)destination,item);
    }

    public void editItemQuantity(int deliveryId,int siteId, String item, int quantity) throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site destination=sitesController.getSite(siteId);
        if(!destination.canBeADestination()){
            throw new Exception(String.format("Site id %d is not a destination...",siteId));
        }
        delivery.editItemQuantity((Branch)destination,item,quantity);
    }

    //why did i to this?
    public String getItemsOfDest(int deliveryId,int siteId) throws Exception {
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site destination=sitesController.getSite(siteId);
        if(!destination.canBeADestination()){
            throw new Exception(String.format("Site id %d is not a destination...",siteId));
        }
        return delivery.toStringItemsOfDest((Branch) destination);
    }

    public void editStartTime(int deliveryId,LocalDateTime newStartTime)throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        delivery.setStartTime(newStartTime);
    }
    public void editEndTime(int deliveryId,LocalDateTime newEndTime)throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        delivery.setEndTime(newEndTime);
    }

    public void editDriver(int deliveryId,int newDriverId) throws Exception{
        Driver driver = driversController.getDriver(newDriverId);
        Delivery delivery=getUpcomingDelivery(deliveryId);
        checkAvailability(delivery.getStartTime(),delivery.getEndTime(),-1,newDriverId);
        if (!trucksController.isAbleToDrive(driver.getLicenseType(),delivery.getTruck().getPlateNum()))
            throw new Exception("The truck is not free at this date...");
        delivery.setDriver(driver);
    }

    public void editTruck(int deliveryId,int newTruckId) throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Truck truck= trucksController.getTruck(newTruckId);
        checkAvailability(delivery.getStartTime(),delivery.getEndTime(),newTruckId,-1);
        if (!trucksController.isAbleToDrive(delivery.getDriver().getLicenseType(),truck.getPlateNum()))
            throw new Exception("The driver is not free at this date...");
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
            throw new Exception("invalid weight...");
        if(weight > delivery.getTruck().getMaxWeight())
            throw new Exception(
                    String.format("Actual weight exceeds the max weight of truck %d..",
                            delivery.getTruck().getMaxWeight()));
        delivery.setWeight(weight);
    }

    public void completeDelivery(int deliveryId) throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        if(delivery.getWeight()<=0){
            throw new Exception(String.format("Before completing delivery num. %d, truck's weight must be updated...",deliveryId));
        }
        upcomingDeliveries.remove(deliveryId);
        deliveryArchive.put(deliveryId,delivery.toString());
    }
    public void checkTruckHasUpcomingDelivery(int truckId) throws Exception {
        for (Delivery delivery: upcomingDeliveries.values()){
            if(delivery.getTruck().getPlateNum()==truckId){
                throw new Exception(String.format("Truck id %d has upcoming deliveries...",truckId));
            }
        }
    }

    public void checkSiteHasUpcomingDelivery(int siteId) throws Exception {
        String site=sitesController.getSite(siteId).getAddress();
        for (Delivery delivery: upcomingDeliveries.values()){
            if(delivery.getOrigin().getId()==siteId){
                throw new Exception(String.format("Site %s has upcoming deliveries...",site));
            }
            for (Branch branch: delivery.getDestinations())
                if(branch.getId()==siteId)
                    throw new Exception(String.format("Site %s has upcoming deliveries...",site));
        }
    }

    public void checkDriverHasUpcomingDelivery(int driverId) throws Exception {
        for (Delivery delivery: upcomingDeliveries.values()){
            if(delivery.getDriver().getId()==driverId){
                throw new Exception(String.format("Driver id %d has upcoming deliveries...",driverId));
            }
        }
    }

    public void deleteDelivery(int deliveryId) throws Exception{
        if(!upcomingDeliveries.containsKey(deliveryId)){
            throw new Exception(String.format("Delivery %d was not found...",deliveryId));
        }
        upcomingDeliveries.remove(deliveryId);
    }

}
