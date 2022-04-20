package BusinessLayer;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
class TruckControllerTest {

    @org.junit.jupiter.api.Test
    void addTruck() {
        TruckController tc = new TruckController();
        try{
            tc.addTruck(1, "a", 10000);
            assertEquals(1, tc.getTrucks().size());
        }
        catch (Exception e) {
            fail();
        }
        try{
            tc.addTruck(2, "b", 20000);
            assertEquals(2, tc.getTrucks().size());
        }
        catch (Exception e) {
            fail();
        }
        try{
            tc.addTruck(1, "c", 30000);
            fail();
        }
        catch (Exception e) {
            assertTrue(true);
        }
    }

    @org.junit.jupiter.api.Test
    void editPlateNum() {
        TruckController tc = new TruckController();
        try{
            tc.addTruck(1, "a", 10000);
            tc.addTruck(2, "b", 20000);
            tc.editPlateNum(1, 3);
        }
        catch (Exception e){
            fail();
        }
        try{
            tc.editPlateNum(2, 3);
            fail();
        }
        catch (Exception e){
            assertTrue(true);
        }
        try{
            tc.editPlateNum(4, 5);
            fail();
        }
        catch (Exception e){
            assertTrue(true);
        }
        try {
            tc.getTruck(1);
            fail();
        }catch (Exception e){
            assertTrue(true);
        }
    }

    @org.junit.jupiter.api.Test
    void editModel() {
        TruckController tc = new TruckController();
        try{
            tc.addTruck(1, "a", 10000);
            tc.addTruck(2, "b", 20000);
            tc.editModel(1, "c");
            assertTrue(true);
        }
        catch (Exception e){
            fail();
        }
        try{
            tc.editModel(3, "d");
            fail();
        }
        catch (Exception e){
            assertTrue(true);
        }
    }

    @org.junit.jupiter.api.Test
    void editMaxWeight() {
        TruckController tc = new TruckController();
        try{
            tc.addTruck(1, "a", 10000);
            tc.addTruck(2, "b", 20000);
            tc.editMaxWeight(1, 15000);
            assertEquals(15000,tc.getTruck(1).getMaxWeight());
        }
        catch (Exception e){
            fail();
        }
        try{
            tc.editMaxWeight(3, 15000);
            fail();
        }
        catch (Exception e){
            assertTrue(true);
        }
    }

    @org.junit.jupiter.api.Test
    void deleteTruck() {
        TruckController tc = new TruckController();
        try{
            tc.addTruck(1, "a", 10000);
            assertEquals(1,tc.getTrucks().size());
            tc.deleteTruck(1);
            assertEquals(0,tc.getTrucks().size());
            tc.addTruck(1, "a", 10000);
            assertTrue(true);
        }
        catch (Exception e){
            fail();
        }
        try{
            tc.deleteTruck(2);
            fail();
        }
        catch (Exception e){
            assertTrue(true);
        }
    }

    @org.junit.jupiter.api.Test
    void isAbleToDrive() {
        TruckController tc = new TruckController();
        try{
            tc.addTruck(1, "a", 10000);
            tc.addTruck(2, "b", 20000);
            assertTrue(tc.isAbleToDrive(LicenseType.C1, 1));
            assertTrue(tc.isAbleToDrive(LicenseType.C, 1));
            assertFalse(tc.isAbleToDrive(LicenseType.C1, 2));
            assertTrue(tc.isAbleToDrive(LicenseType.C, 2));
            tc.isAbleToDrive(LicenseType.C1, 3);
        }
        catch (Exception e){
            assertTrue(true);
        }
    }

    @org.junit.jupiter.api.Test
    void getTrucks() {
        TruckController tc = new TruckController();
        try{
            tc.addTruck(1, "a", 60000);
            tc.addTruck(2, "b", 10000);
            tc.addTruck(3, "c", 20000);
            tc.addTruck(4, "d", 5000);
            ArrayList<Truck> list=tc.getTrucks();
            assertEquals(4, list.size());
            assertEquals(5000, list.get(0).getMaxWeight());
            assertEquals(10000, list.get(1).getMaxWeight());
            assertEquals(20000, list.get(2).getMaxWeight());
            assertEquals(60000, list.get(3).getMaxWeight());
        }
        catch (Exception e){
            fail();
        }
    }
}