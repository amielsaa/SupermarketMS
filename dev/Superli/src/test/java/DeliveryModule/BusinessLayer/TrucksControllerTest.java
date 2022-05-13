package DeliveryModule.BusinessLayer;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TrucksControllerTest {

    @Test
    public void addTruck() {
        TrucksController tc = new TrucksController();
        try{
            tc.addTruck(1111111, "a", 10000);
            assertEquals(1, tc.getTrucks().size());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
        try{
            tc.addTruck(2222222, "b", 20000);
            assertEquals(2, tc.getTrucks().size());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
        try{
            tc.addTruck(1111111, "c", 30000);
            fail();
        }
        catch (Exception e) {
        }
    }

    @Test
    public void editPlateNum() {
        TrucksController tc = new TrucksController();
        try{
            tc.addTruck(11111111, "a", 10000);
            tc.addTruck(2222222, "b", 20000);
            tc.editPlateNum(11111111, 3333333);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }
        try{
            tc.editPlateNum(11111111, 3333333);
            fail();
        }
        catch (Exception e){
        }
        try{
            tc.editPlateNum(4444444, 5555555);
            fail();
        }
        catch (Exception e){
        }
        try {
            tc.getTruck(11111111);
            fail();
        }catch (Exception e){}
    }

    @Test
    public void editModel() {
        TrucksController tc = new TrucksController();
        try{
            tc.addTruck(11111111, "a", 10000);
            tc.addTruck(2222222, "b", 20000);
            tc.editModel(11111111, "c");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }
        try{
            tc.editModel(3333333, "d");
            fail();
        }
        catch (Exception e){
        }
    }

    @Test
    public void editMaxWeight() {
        TrucksController tc = new TrucksController();
        try{
            tc.addTruck(11111111, "a", 10000);
            tc.addTruck(2222222, "b", 20000);
            tc.editMaxWeight(11111111, 15000);
            assertEquals(15000,tc.getTruck(11111111).getMaxWeight());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }
        try{
            tc.editMaxWeight(3333333, 15000);
            fail();
        }
        catch (Exception e){
        }
    }

    @Test
    public void deleteTruck() {
        TrucksController tc = new TrucksController();
        try{
            tc.addTruck(11111111, "a", 10000);
            assertEquals(1,tc.getTrucks().size());
            tc.deleteTruck(11111111);
            assertEquals(0,tc.getTrucks().size());
            tc.addTruck(11111111, "a", 10000);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }
        try{
            tc.deleteTruck(2222222);
            fail();
        }
        catch (Exception e){}
    }

    @Test
    public void isAbleToDrive() {
        TrucksController tc = new TrucksController();
        try{
            tc.addTruck(11111111, "a", 10000);
            tc.addTruck(2222222, "b", 20000);
            assertTrue(tc.isAbleToDrive(LicenseType.C1, 11111111));
            assertTrue(tc.isAbleToDrive(LicenseType.C, 11111111));
            assertFalse(tc.isAbleToDrive(LicenseType.C1, 2222222));
            assertTrue(tc.isAbleToDrive(LicenseType.C, 2222222));
            tc.isAbleToDrive(LicenseType.C1, 3333333);
        }
        catch (Exception e){
        }
    }

    @Test
    public void getTrucks() {
        TrucksController tc = new TrucksController();
        try{
            tc.addTruck(1111111, "a", 60000);
            tc.addTruck(2222222, "b", 10000);
            tc.addTruck(3333333, "c", 20000);
            tc.addTruck(4444444, "d", 5000);
            ArrayList<Truck> list=tc.getTrucks();
            assertEquals(4, list.size());
            assertEquals(5000, list.get(0).getMaxWeight());
            assertEquals(10000, list.get(1).getMaxWeight());
            assertEquals(20000, list.get(2).getMaxWeight());
            assertEquals(60000, list.get(3).getMaxWeight());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }
    }
}