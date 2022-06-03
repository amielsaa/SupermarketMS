//package DeliveryModule.BusinessLayer;
//
//import DeliveryModule.DataAccessLayer.*;
//import org.junit.Test;
//import org.junit.Before;
//
//import java.time.LocalDateTime;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.fail;
//
//public class DeliveriesControllerTest {
//    private SitesController sitesController;
//    private TrucksController trucksController;
//    private DriversController driversController;
//    private DeliveriesController deliveriesController;
//    private LocalDateTime time1;
//    private LocalDateTime time2;
//    private LocalDateTime time3;
//    private LocalDateTime time4;
//    private LocalDateTime time5;
//    private LocalDateTime time6;
//
//    @Before
//    public void setUp() {
//        try {
//            CreateClearTables.clearTables();
//            sitesController = new SitesController();
//            trucksController = new TrucksController();
//            driversController = new DriversController();
//            deliveriesController = new DeliveriesController(driversController,sitesController, trucksController);
//
//            driversController.addDriver(1, "c driver", "C");
//            driversController.addDriver(2, "c1 driver", "C1");
//            trucksController.addTruck(1111111, "everyone", 10000);
//            trucksController.addTruck(2222222, "only c", 20000);
//            sitesController.addSupplierWarehouse("a", 0, "11111111", "name");
//            sitesController.addBranch("b", 1, "11111112", "name2");
//            sitesController.addBranch("c", 1, "11111113", "name3");
//            time1 = LocalDateTime.of(2023,11,3,13,12);
//            time2 = time1.plusHours(1);
//            time3 = time1.plusHours(3);
//            time4 = time1.plusHours(4);
//            time5 = time1.plusHours(5);
//            time6 = time1.plusHours(6);
//        }
//        catch (Exception e)
//        {
//            System.out.println(e.getMessage());
//            fail();
//        }
//    }
//
//    @Test
//    public void addDelivery() {
//        try{
//            deliveriesController.addDelivery(time2, time1, 1111111,1,1,2);
//            fail();
//        }
//        catch (Exception e){}
//
//        try{
//            deliveriesController.addDelivery(time1, time2, 3333333,1,1,2);
//            fail();
//        }
//        catch (Exception e){}
//
//        try{
//            deliveriesController.addDelivery(time1, time2, 1111111,3,1,2);
//            fail();
//        }
//        catch (Exception e){}
//
//        try{
//            deliveriesController.addDelivery(time1, time2, 11111111,1,4,2);
//            fail();
//        }
//        catch (Exception e){}
//
//        try{
//            deliveriesController.addDelivery(time1, time2, 2222222,2,1,2);
//            fail();
//        }
//        catch (Exception e){}
//
//        try{
//            deliveriesController.addDelivery(time1, time4, 1111111,2,1,2);
//            deliveriesController.addDelivery(time1, time2, 2222222,1,1,2);
//            deliveriesController.addDelivery(time3, time4, 2222222,1,1,2);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//        try{
//            deliveriesController.addDelivery(time2, time3, 1111111,2,1,2);
//            fail();
//        }
//        catch (Exception e){
//        }
//
//    }
//
//    @Test
//    public void getCompletedDelivery() {
//        try{
//            deliveriesController.addDelivery(time1, time2, 1111111,1,1,2);
//            deliveriesController.addDelivery(time3, time4, 1111111,1,1,2);
//            deliveriesController.setWeight(1,5000);
//            deliveriesController.completeDelivery(1);
//            deliveriesController.getCompletedDelivery(1);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//        try{
//            deliveriesController.getCompletedDelivery(2);
//            fail();
//        }
//        catch (Exception e){
//        }
//    }
//
//    @Test
//    public void getUpcomingDelivery() {
//        try{
//            deliveriesController.addDelivery(time1, time2, 1111111,1,1,2);
//            deliveriesController.addDelivery(time3, time4, 2222222,1,1,2);
//            deliveriesController.setWeight(1,5000);
//            deliveriesController.completeDelivery(1);
//            assertEquals(2222222, deliveriesController.getUpcomingDelivery(2).getTruckId());
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//        try{
//            deliveriesController.completeDelivery(1);
//            fail();
//        }
//        catch (Exception e){
//        }
//    }
//
//    @Test
//    public void getUpcomingDeliveries() {
//        try{
//            deliveriesController.addDelivery(time1, time2, 2222222,1,1,2);
//            deliveriesController.addDelivery(time1, time2, 1111111,2,1,2);
//            deliveriesController.addDelivery(time3, time4, 1111111,2,1,2);
//            deliveriesController.setWeight(1,5000);
//            deliveriesController.completeDelivery(1);
//            assertEquals(2, deliveriesController.getUpcomingDeliveries().size());
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//    }
//
//    @Test
//    public void getDeliveryArchive() {
//        try{
//            deliveriesController.addDelivery(time1, time2, 2222222,1,1,2);
//            deliveriesController.addDelivery(time1, time2, 1111111,2,1,2);
//            deliveriesController.addDelivery(time3, time4, 1111111,2,1,2);
//            deliveriesController.setWeight(1,5000);
//            deliveriesController.completeDelivery(1);
//            deliveriesController.setWeight(2,5000);
//            deliveriesController.completeDelivery(2);
//            assertEquals(2, deliveriesController.getDeliveryArchive().size());
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//    }
//
//    @Test
//    public void addDestination() {
//        try{
//            deliveriesController.addDelivery(time1, time2, 2222222,1,1,2);
//            deliveriesController.addDestination(1,3);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//        try{
//            deliveriesController.addDestination(1,2);
//            fail();
//        }
//        catch (Exception e){
//        }
//        try{
//            deliveriesController.addDestination(1,1);//might be legal need to discuss it
//            fail();
//        }
//        catch (Exception e){
//        }
//        try{
//            deliveriesController.addDestination(1,4);
//            fail();
//        }
//        catch (Exception e){
//        }
//        try{
//            deliveriesController.addDestination(2,3);
//            fail();
//        }
//        catch (Exception e){
//        }
//    }
//
//    @Test
//    public void removeDestination() {
//        try{
//        deliveriesController.addDelivery(time1, time2, 2222222,1,1,2);
//        deliveriesController.addDestination(1,3);
//        deliveriesController.removeDestination(1,2);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//        try{
//            deliveriesController.removeDestination(1,2);
//            fail();
//        }
//        catch (Exception e){
//        }
//    }
//
//    @Test
//    public void addItemToDestination() {
//        try{
//            deliveriesController.addDelivery(time1, time2, 2222222,1,1,2);
//            deliveriesController.addItemToDestination(1,2,"name",1);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//        try{
//            deliveriesController.addItemToDestination(1,1,"name",1);
//            fail();
//        }
//        catch (Exception e){
//        }
//    }
//
//    @Test
//    public void removeItemFromDestination() {
//        try{
//            deliveriesController.addDelivery(time1, time2, 2222222,1,1,2);
//            deliveriesController.addItemToDestination(1,2,"name",1);
//            deliveriesController.removeItemFromDestination(1, 2, "name");
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//        try{
//            deliveriesController.removeItemFromDestination(1, 2, "name");
//            fail();
//        }
//        catch (Exception e){
//        }
//        try{
//            deliveriesController.removeItemFromDestination(1, 2, "namee");
//            fail();
//        }
//        catch (Exception e){
//        }
//    }
//
//    @Test
//    public void editItemQuantity() {
//        try{
//            deliveriesController.addDelivery(time1, time2, 2222222,1,1,2);
//            deliveriesController.addItemToDestination(1,2,"name",1);
//            deliveriesController.editItemQuantity(1,2,"name",3);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//        try{
//            deliveriesController.editItemQuantity(1,2,"namee",3);
//            fail();
//        }
//        catch (Exception e){
//        }
//    }
//
//    @Test
//    public void editStartTime() {
//        try{
//            deliveriesController.addDelivery(time1, time3, 2222222,1,1,2);
//            deliveriesController.editStartTime(1,time2);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//        try{
//            deliveriesController.editStartTime(1,time4);
//            fail();
//        }
//        catch (Exception e){
//        }
//    }
//
//    @Test
//    public void editEndTime() {
//        try{
//            deliveriesController.addDelivery(time2, time4, 2222222,1,1,2);
//            deliveriesController.editEndTime(1,time5);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//        try{
//            deliveriesController.editEndTime(1,time1);
//            fail();
//        }
//        catch (Exception e){
//        }
//    }
//
//    @Test
//    public void editDriver() {
//        try{
//            deliveriesController.addDelivery(time1, time2, 1111111,2,1,2);
//            deliveriesController.addDelivery(time3, time4, 2222222,1,1,2);
//            deliveriesController.addDelivery(time5, time6, 1111111,2,1,2);
//            deliveriesController.addDelivery(time5, time6, 2222222,1,1,2);
//            deliveriesController.editDriver(1,1);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//        try{
//            deliveriesController.editDriver(2,2);
//            fail();
//        }
//        catch (Exception e){
//        }
//
//        try{
//            deliveriesController.editDriver(2,3);
//            fail();
//        }
//        catch (Exception e){
//        }
//        try{
//            deliveriesController.editDriver(3,1);
//            fail();
//        }
//        catch (Exception e){
//        }
//    }
//
//    @Test
//    public void editTruck() {
//        try{
//            deliveriesController.addDelivery(time1, time2, 1111111,2,1,2);
//            deliveriesController.addDelivery(time3, time4, 2222222,1,1,2);
//            deliveriesController.addDelivery(time5, time6, 1111111,2,1,2);
//            deliveriesController.addDelivery(time5, time6, 2222222,1,1,2);
//            deliveriesController.editTruck(2,1111111);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//        try{
//            deliveriesController.editTruck(1,2222222);
//            fail();
//        }
//        catch (Exception e){
//        }
//
//        try{
//            deliveriesController.editTruck(2,3333333);
//            fail();
//        }
//        catch (Exception e){
//        }
//        try{
//            deliveriesController.editTruck(3,2222222);
//            fail();
//        }
//        catch (Exception e){
//        }
//    }
//
//    @Test
//    public void setOrigin() {
//        try{
//            deliveriesController.addDelivery(time1, time2, 1111111,2,1,2);
//            deliveriesController.setOrigin(1,2);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//        try{
//            deliveriesController.setOrigin(1,4);
//            fail();
//        }
//        catch (Exception e){
//        }
//    }
//
//    @Test
//    public void setWeight() {
//        try{
//            deliveriesController.addDelivery(time1, time2, 1111111,2,1,2);
//            deliveriesController.setWeight(1,9000);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//        try{
//            deliveriesController.setWeight(1,14000);
//            fail();
//        }
//        catch (Exception e){
//        }
//    }
//
//    @Test
//    public void completeDelivery() {
//        try{
//            deliveriesController.addDelivery(time1, time2, 1111111,2,1,2);
//            deliveriesController.setWeight(1,5000);
//            deliveriesController.completeDelivery(1);
//
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//    }
//
//    @Test
//    public void checkTruckHasUpcomingDelivery() {
//        try{
//            deliveriesController.addDelivery(time1, time2, 1111111,2,1,2);
//            deliveriesController.addDelivery(time1, time2, 2222222,1,2,2);
//            deliveriesController.setWeight(2,5000);
//            deliveriesController.completeDelivery(2);
//            deliveriesController.checkTruckHasUpcomingDelivery(2222222);
//
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//        try{
//            deliveriesController.checkTruckHasUpcomingDelivery(1111111);
//            fail();
//        }
//        catch (Exception e){
//        }
//    }
//
//    @Test
//    public void checkSiteHasUpcomingDelivery() {
//        try{
//            deliveriesController.addDelivery(time1, time2, 1111111,2,1,2);
//            deliveriesController.addDelivery(time1, time2, 2222222,1,2,2);
//            deliveriesController.setWeight(2,5000);
//            deliveriesController.completeDelivery(2);
//            deliveriesController.checkSiteHasUpcomingDelivery(2);
//            fail();
//        }
//        catch (Exception e){
//
//        }
//        try{
//            deliveriesController.checkSiteHasUpcomingDelivery(1);
//            fail();
//        }
//        catch (Exception e){
//        }
//    }
//
//    @Test
//    public void checkDriverHasUpcomingDelivery() {
//        try{
//            deliveriesController.addDelivery(time1, time2, 1111111,2,1,2);
//            deliveriesController.addDelivery(time1, time2, 2222222,1,2,2);
//            deliveriesController.setWeight(2,5000);
//            deliveriesController.completeDelivery(2);
//            deliveriesController.checkDriverHasUpcomingDelivery(1);
//
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            fail();
//        }
//        try{
//            deliveriesController.checkDriverHasUpcomingDelivery(2);
//            fail();
//        }
//        catch (Exception e){
//        }
//    }
//}