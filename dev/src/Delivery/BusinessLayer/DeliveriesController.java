package Delivery.BusinessLayer;

import Delivery.DataAccessLayer.*;
import Employee.ServiceLayer.Gateway;
import Suppliers.BusinessLayer.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import Suppliers.BusinessLayer.OrderItem;
import Suppliers.ServiceLayer.DummyObjects.DOrder;
import Utilities.Pair;
import Utilities.Response;

public class DeliveriesController {
    private int nextDeliveryId;
    private DriversController driversController;
    private SitesController sitesController;
    private TrucksController trucksController;

    private UpcomingDeliveryDAO upcomingDeliveryDAO;
    private DeliveryArchiveDAO deliveryArchiveDAO;
    private DeliveryDestinationItemsDAO deliveryDestinationItemsDAO;
    private DeliveryDestinationsDAO deliveryDestinationsDAO;


    public DeliveriesController(DriversController driversController, SitesController sitesController, TrucksController trucksController) {
        this.driversController = driversController;
        this.sitesController = sitesController;
        this.trucksController = trucksController;
        this.upcomingDeliveryDAO=new UpcomingDeliveryDAO();
        this.deliveryArchiveDAO=new DeliveryArchiveDAO();
        this.deliveryDestinationItemsDAO=new DeliveryDestinationItemsDAO();
        this.deliveryDestinationsDAO= new DeliveryDestinationsDAO();
        nextDeliveryId=getNextDeliveryId();
     //   try{
      //      load();
       // }catch (Exception e){System.out.println(e.getMessage());}

    }

    public void load() throws Exception {
        nextDeliveryId = 1;
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
       addDelivery(LocalDateTime.parse("13-10-2023 13:10",dateTimeFormatter),
               LocalDateTime.parse("13-10-2023 15:10",dateTimeFormatter),
               1000001,
               200000001,
               1,0);
        addItemToDestination(1,0,"Cottage","Tnuva",10, 10);
        addItemToDestination(1,0,"Banana","Perot",10, 10);
        addItemToDestination(1,0,"Chips","Osem",10, 10);
        setWeight(1, 7000);
    }



    private void checkAvailability(LocalDateTime startTime,LocalDateTime endTime, int truckId, int driverId,int deliveryid) throws Exception {
        //todo: maybe use select query for truckId instead of using getUpcomingDeliveries
        for(Delivery delivery:upcomingDeliveryDAO.getUpcomingDeliveries()){
            if((deliveryid!=delivery.getId() && delivery.getTruckId()==truckId &&
                    isOverlappedIntervals(startTime,endTime,delivery.getStartTime(),delivery.getEndTime()))){
                throw new Exception(String.format("Tuck %d is not available for delivery starting at %s ending at: %s, overlapping delivery: %d  start time: %s  end time: %s",truckId,startTime,endTime,delivery.getId(),delivery.getStartTime(),delivery.getEndTime()));
            }
            if(deliveryid!=delivery.getId() && delivery.getDriverId()==driverId &&
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
    private int getNextDeliveryId(){
        return Integer.max(deliveryArchiveDAO.getMaxId(),upcomingDeliveryDAO.getMaxId())+1;
    }

    //manual
    public void addDelivery(LocalDateTime startTime,LocalDateTime endTime,int truckId,int driverId,int originId,int destinationId) throws Exception{
        validateDeliveryTime(startTime,endTime);
        Driver driver=driversController.getDriver(driverId);
        if(!trucksController.isAbleToDrive(driver.getLicenseType(),truckId)){
            throw new Exception(String.format("Driver whose id number is %d is not permitted to drive the truck %d",driverId,truckId));
        }

        checkAvailability(startTime,endTime,truckId,driverId,-1);
        trucksController.getTruck(truckId);
        sitesController.getSite(originId);
        Site destination=sitesController.getSite(destinationId);
        if(!destination.canBeADestination()){
            throw new Exception(String.format("Site %d is not a destination...",destinationId));
        }
        upcomingDeliveryDAO.Create(new Delivery(nextDeliveryId,startTime,endTime,driverId,truckId,originId,0, 111111111, 1));
        addDestination(nextDeliveryId,destinationId);
        nextDeliveryId++;
    }

    //auto
    public void addDelivery(DOrder order, Collection<LocalDate> days, Gateway employeeMod, String address) throws Exception {
        Collection<Truck> trucks = trucksController.getTrucks();
        Collection<Driver> drivers = driversController.getAllDrivers();
        Collection<Pair<Truck, Driver>> pairs = findMatchingTrucksDrivers(trucks, drivers);
        if(pairs.isEmpty()){throw new Exception("Non of SuperLee's drivers have a suitable license to any of SuperLee's trucks ");}
        boolean foundDriverWithShift=false;
        LocalDateTime deliveryStartTime = null;
        LocalDateTime deliveryEndTime=null;
        boolean foundAvailableDriverTruck=false;
        //days.add(LocalDate.now().atStartOfDay().plusDays(1).toLocalDate());
        //System.out.println(LocalDate.now().atStartOfDay().plusDays(1).toLocalDate().toString());
        for (Pair<Truck, Driver> p: pairs) {
            for(LocalDate day : days) {
                Response<Boolean> driverAvailable = employeeMod.driverAvailableOnShift(day.atStartOfDay(), p.getValue().getId());
                boolean driverAvailableToday=driverAvailable.isSuccess()&&driverAvailable.getData();
                if(driverAvailableToday) {
                    foundDriverWithShift = true;
                    LocalDateTime startTimeA = day.atStartOfDay(); //up to change
                    LocalDateTime endTimeA = day.atStartOfDay().plusHours(11).plusMinutes(59); //up to change
                    LocalDateTime startTimeB = day.atStartOfDay().plusHours(12); //up to change
                    LocalDateTime endTimeB = day.atStartOfDay().plusDays(1).minusMinutes(1);
                    try {
                        checkAvailability(startTimeA,endTimeA,p.getKey().getPlateNum(),p.getValue().getId(), -1);
                        deliveryStartTime=startTimeA;
                        deliveryEndTime=endTimeA;
                        foundAvailableDriverTruck=true;
                    } catch (Exception e) {}
                    if (!foundAvailableDriverTruck){
                        try {
                            checkAvailability(startTimeB,endTimeB,p.getKey().getPlateNum(),p.getValue().getId(), -1);
                            deliveryStartTime=startTimeB;
                            deliveryEndTime=endTimeB;
                            foundAvailableDriverTruck=true;
                        } catch (Exception e) {}
                    }

                    if(foundAvailableDriverTruck){
                        upcomingDeliveryDAO.Create(new Delivery(nextDeliveryId,deliveryStartTime,deliveryEndTime,p.getValue().getId(),p.getKey().getPlateNum(),sitesController.getSite(address).getId(),0, order.getSupplier_BN(), order.getOrder_Id()));
                        addDestination(nextDeliveryId,0);
                        for (misc.Pair<String, String> item : order.getItem_Num_To_OrderItem().keySet())
                            addItemToDestination(nextDeliveryId,
                                    0,
                                    item.getFirst(),
                                    item.getSecond(),
                                    order.getItem_Num_To_OrderItem().get(item).getItem_Price(),
                                    order.getItem_Num_To_OrderItem().get(item).getItem_Amount());

                        nextDeliveryId++;
                        return;
                    }
                }
            }
        }

        if(!foundDriverWithShift){throw new Exception("Could not find a driver with shift..");}
        throw new Exception("Could not find unoccupied pair of a driver and a truck");
    }

    private Collection<Pair<Truck, Driver>> findMatchingTrucksDrivers(Collection<Truck> trucks, Collection<Driver> drivers) {
        Collection<Pair<Truck, Driver>> pairs = new ArrayList<>();
        for (Truck t: trucks)
            for (Driver d: drivers)
                if (trucksController.isAbleToDrive(d.getLicenseType(),t.getPlateNum()))
                    pairs.add(new Pair<>(t, d));
        return pairs;
    }

    private void validateDeliveryTime(LocalDateTime startTime,LocalDateTime endTime) throws Exception {
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        if(startTime.isBefore(LocalDateTime.now())){
            throw new Exception(String.format("%s has passed",startTime.format(dateTimeFormatter)));
        }
        if(endTime.isBefore(LocalDateTime.now())){
            throw new Exception(String.format("%s has passed",endTime.format(dateTimeFormatter)));
        }
        if(endTime.isBefore(startTime)){
            throw new Exception("end time cant be earlier than the start time...");
        }
        LocalDateTime midDay=startTime.withMinute(0).withHour(12);
        LocalDateTime midNight=startTime.withMinute(59).withHour(23);
        if(startTime.isBefore(midDay) && !endTime.isBefore(midDay)){
            throw new Exception(String.format("Delivery must be between %s and %s..",startTime.format(dateTimeFormatter),midDay.format(dateTimeFormatter)));
        }else if(startTime.isAfter(midDay) && endTime.isAfter(midNight)){
            throw new Exception(String.format("Delivery must be between %s and %s..",startTime.format(dateTimeFormatter),midNight.format(dateTimeFormatter)));
        }
    }
    public String getCompletedDelivery(int deliveryId) throws Exception {
        String deliveryRecord=deliveryArchiveDAO.Read(deliveryId);
        if(deliveryRecord==null){
            throw new Exception(String.format("Delivery %d was not found",deliveryId));
        }
        return deliveryRecord;
    }
    public Delivery getUpcomingDelivery(int deliveryId) throws Exception {
        Delivery delivery=upcomingDeliveryDAO.Read(deliveryId);
        if(delivery==null){
            throw new Exception(String.format("Delivery %d was not found",deliveryId));
        }
        if(delivery.getDestinationItems()==null)
            loadDeliveryDestinations(deliveryId);
        return delivery;
    }
    public ArrayList<Delivery> getUpcomingDeliveries(){
        ArrayList<Delivery> deliveries=upcomingDeliveryDAO.getUpcomingDeliveries();
        for(Delivery delivery:deliveries) {
            if (delivery.getDestinationItems() == null)
                loadDeliveryDestinations(delivery.getId());
        }
        return deliveries;
    }
    public ArrayList<String> getDeliveryArchive(){
        return deliveryArchiveDAO.getDeliveryArchive();
    }
    public void addDestination(int deliveryId,int siteId) throws Exception {
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site destination=sitesController.getSite(siteId);
        if(!destination.canBeADestination()){
            throw new Exception(String.format("Site id %d is not a destination...",siteId));
        }
        delivery.addDestination((Branch) destination);
        deliveryDestinationsDAO.Create(deliveryId,siteId);
    }

    public void removeDestination(int deliveryId,int siteId) throws Exception {
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site destination=sitesController.getSite(siteId);
        if(!(destination instanceof Branch)) throw new Exception(String.format("Site id %d is not a destination...",siteId));
        delivery.removeDestination((Branch) destination);
        deliveryDestinationItemsDAO.removeItemsOfDestination(deliveryId,siteId);
        deliveryDestinationsDAO.Delete(deliveryId,siteId);
    }

    public void addItemToDestination(int deliveryId,int siteId, String item, String producer, double price ,int quantity) throws Exception {
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site destination=sitesController.getSite(siteId);
        if(!destination.canBeADestination()){
            throw new Exception(String.format("Site id %d is not a destination...",siteId));
        }
        delivery.addItemToDestination((Branch)destination,item,producer,price,quantity);
        deliveryDestinationItemsDAO.Create(deliveryId,siteId,item,producer,price,quantity);
    }
    public void removeItemFromDestination(int deliveryId,int siteId, String item, String producer) throws Exception {
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site destination=sitesController.getSite(siteId);
        if(!destination.canBeADestination()){
            throw new Exception(String.format("Site id %d is not a destination...",siteId));
        }
        delivery.removeItemFromDestination((Branch)destination,item, producer);
        deliveryDestinationItemsDAO.removeItemFromDestination(deliveryId,siteId,item, producer);
    }

    public void editItemQuantity(int deliveryId,int siteId, String item, String producer, int quantity) throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Site destination=sitesController.getSite(siteId);
        if(!destination.canBeADestination()){
            throw new Exception(String.format("Site id %d is not a destination...",siteId));
        }
        delivery.editItemQuantity((Branch)destination,item, producer, quantity);
        deliveryDestinationItemsDAO.editItemQuantity(deliveryId,siteId,item, producer, quantity);
    }

    public void loadDeliveryDestinations(int deliveryId){
        Delivery delivery=upcomingDeliveryDAO.Read(deliveryId);
        LinkedList<Integer> destinations=deliveryDestinationsDAO.Read(deliveryId);
        LinkedHashMap<Integer, HashMap<Pair<String, String>,Pair<Double,Integer>>> deliveryDestinations=new LinkedHashMap<>();
        for(Integer destination: destinations){
            HashMap<Pair<String, String>,Pair<Double,Integer>> items=getItemsOfDest(deliveryId,destination);
            deliveryDestinations.put(destination,items);
        }
        delivery.setDestinationItems(deliveryDestinations);
    }

    public HashMap<Pair<String, String>,Pair<Double,Integer>> getItemsOfDest(int deliveryId,int siteId) {
        HashMap<Pair<String, String>,Pair<Double,Integer>> items=deliveryDestinationItemsDAO.getItemsOfDest(deliveryId,siteId);
        return items;
    }

    public void editStartTime(int deliveryId,LocalDateTime newStartTime)throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        validateDeliveryTime(newStartTime,delivery.getEndTime());
        checkAvailability(newStartTime,delivery.getEndTime(),delivery.getTruckId(),delivery.getDriverId(),deliveryId);
        delivery.setStartTime(newStartTime);
        upcomingDeliveryDAO.Update(delivery);
    }
    public void editEndTime(int deliveryId,LocalDateTime newEndTime)throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        validateDeliveryTime(delivery.getStartTime(),newEndTime);
        checkAvailability(delivery.getStartTime(),newEndTime,delivery.getTruckId(),delivery.getDriverId(),deliveryId);
        delivery.setEndTime(newEndTime);
        upcomingDeliveryDAO.Update(delivery);
    }

    public void editDriver(int deliveryId,int newDriverId) throws Exception{
        Driver driver = driversController.getDriver(newDriverId);
        Delivery delivery=getUpcomingDelivery(deliveryId);
        checkAvailability(delivery.getStartTime(),delivery.getEndTime(),-1,newDriverId,deliveryId);
        if (!trucksController.isAbleToDrive(driver.getLicenseType(),delivery.getTruckId()))
            throw new Exception("The driver cannot drive the truck...");
        delivery.setDriverId(newDriverId);
        upcomingDeliveryDAO.Update(delivery);
        //upcomingDeliveryDAO.setDriverId(deliveryId,newDriverId);
    }

    public void editTruck(int deliveryId,int newTruckId) throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        trucksController.getTruck(newTruckId);
        Driver driver=driversController.getDriver(delivery.getDriverId());
        checkAvailability(delivery.getStartTime(),delivery.getEndTime(),newTruckId,-1,deliveryId);
        if (!trucksController.isAbleToDrive(driver.getLicenseType(),newTruckId))
            throw new Exception("The driver cannot drive the truck...");
        delivery.setTruckId(newTruckId);
        upcomingDeliveryDAO.Update(delivery);
       // upcomingDeliveryDAO.setTruck(deliveryId,newTruckId);
    }

    public void setOrigin(int deliveryId,int newOriginId) throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        sitesController.getSite(newOriginId);
        delivery.setOriginSiteId(newOriginId);
        upcomingDeliveryDAO.Update(delivery);
    }

    public void setWeight(int deliveryId,int weight) throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        Truck truck=trucksController.getTruck(delivery.getTruckId());
        if(weight<0)
            throw new Exception("invalid weight...");
        if(weight > truck.getMaxWeight())
            throw new Exception(
                    String.format("Actual weight exceeds the max weight of truck %d..",
                            truck.getMaxWeight()));
        delivery.setWeight(weight);
        upcomingDeliveryDAO.Update(delivery);
        //upcomingDeliveryDAO.setWeight(deliveryId, weight);
    }

    public HashMap<misc.Pair<String, String>,misc.Pair<Double, Integer>> completeDelivery(int deliveryId) throws Exception{
        Delivery delivery=getUpcomingDelivery(deliveryId);
        if(delivery.getWeight()<=0){
            throw new Exception(String.format("Before completing delivery num. %d, truck's weight must be updated...",deliveryId));
        }
        upcomingDeliveryDAO.Delete(deliveryId);
        deliveryArchiveDAO.Create(deliveryId,delivery.toString());
        if (delivery.getDestinationItems().containsKey(0))
            return convertPairFormat(delivery.getDestinationItems().get(0));
        return null;
    }

    public int getBn(int id) throws Exception {
        return getUpcomingDelivery(id).getBn();
    }

    public int getOrderId(int id) throws Exception {
        return getUpcomingDelivery(id).getOrderId();
    }

    private HashMap<misc.Pair<String, String>,misc.Pair<Double, Integer>> convertPairFormat(HashMap<Pair<String, String>, Pair<Double, Integer>> map) {
        HashMap<misc.Pair<String, String>,misc.Pair<Double, Integer>> new_map = new HashMap<>();
        for (Pair<String,String> p: map.keySet())
            new_map.put(new misc.Pair<>(p.getKey(),p.getValue()), new misc.Pair<>(map.get(p).getKey(), map.get(p).getValue()));
        return new_map;
    }

    public void checkTruckHasUpcomingDelivery(int truckId) throws Exception {
        for (Delivery delivery: upcomingDeliveryDAO.getUpcomingDeliveries()){
            if(delivery.getTruckId()==truckId){
                throw new Exception(String.format("Truck id %d has upcoming deliveries...",truckId));
            }
        }
    }

    public void checkSiteHasUpcomingDelivery(int siteId) throws Exception {
        String site=sitesController.getSite(siteId).getAddress();
        for (Delivery delivery: upcomingDeliveryDAO.getUpcomingDeliveries()){
            if(delivery.getOriginSiteId()==siteId){
                throw new Exception(String.format("Site %s has upcoming deliveries...",site));
            }
            for (Integer branch: deliveryDestinationsDAO.Read(delivery.getId()))
                if(branch==siteId)
                    throw new Exception(String.format("Site %s has upcoming deliveries...",site));
        }
    }

    public void checkDriverHasUpcomingDelivery(int driverId) throws Exception {
        for (Delivery delivery: upcomingDeliveryDAO.getUpcomingDeliveries()){
            if(delivery.getDriverId()==driverId){
                throw new Exception(String.format("Driver id %d has upcoming deliveries...",driverId));
            }
        }
    }

    public void deleteDelivery(int deliveryId) throws Exception{
        if(upcomingDeliveryDAO.Read(deliveryId)==null){
            throw new Exception(String.format("Delivery %d was not found...",deliveryId));
        }
        upcomingDeliveryDAO.Delete(deliveryId);
    }

    public void validateLicenseTypeChange(int id, String licenseType) throws Exception {
        for(Delivery delivery:upcomingDeliveryDAO.getUpcomingDeliveries()){
            if(delivery.getDriverId()==id && !trucksController.isAbleToDrive(LicenseType.valueOf(licenseType),delivery.getTruckId())){
                throw new Exception(String.format("New license type does not permit driving the truck assigned to delivery id %s..",delivery.getId()));
            }
        }
    }

    public void validateMaxWeightChange(int truckId , int maxWeight) throws Exception {
        for(Delivery delivery:getUpcomingDeliveries()){
            if(delivery.getTruckId()==truckId){
                Driver driver=driversController.getDriver(delivery.getDriverId());
                if(!trucksController.isAbleToDriveWeight(driver.getLicenseType(),maxWeight))
                    throw new Exception(String.format("New max weight is higher than the license type of the driver of delivery id %s permits..",delivery.getId()));
            }
        }
    }

    public void resetNextId() {
        nextDeliveryId = 1;
    }

    public void checkSiteHasUpcomingDelivery(String address) throws Exception {
        checkSiteHasUpcomingDelivery(sitesController.getSite(address).getId());
    }
}
