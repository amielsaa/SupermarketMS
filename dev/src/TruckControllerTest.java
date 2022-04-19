
import Business.LicenseType;
import Business.TruckController;

import static org.junit.jupiter.api.Assertions.*;
class TruckControllerTest {

    @org.junit.jupiter.api.Test
    void addTruck() {
        TruckController tc = new TruckController();
        try{
            tc.addTruck(1, "a", 10000);
            assertTrue(true);
        }
        catch (Exception e) {
            assertTrue(false);
        }
        try{
            tc.addTruck(2, "b", 20000);
            assertTrue(true);
        }
        catch (Exception e) {
            assertTrue(false);
        }
        try{
            tc.addTruck(1, "c", 30000);
            assertTrue(false);
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
            assertTrue(true);
        }
        catch (Exception e){
            assertTrue(false);
        }
        try{
            tc.editPlateNum(2, 3);
            assertTrue(false);
        }
        catch (Exception e){
            assertTrue(true);
        }
        try{
            tc.editPlateNum(4, 5);
            assertTrue(false);
        }
        catch (Exception e){
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
            assertTrue(false);
        }
        try{
            tc.editModel(3, "d");
            assertTrue(false);
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
            assertTrue(true);
        }
        catch (Exception e){
            assertTrue(false);
        }
        try{
            tc.editMaxWeight(3, 15000);
            assertTrue(false);
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
            tc.deleteTruck(1);
            tc.addTruck(1, "a", 10000);
            assertTrue(true);
        }
        catch (Exception e){
            assertTrue(false);
        }
        try{
            tc.deleteTruck(2);
            assertTrue(false);
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
            tc.addTruck(1, "a", 10000);
            tc.addTruck(2, "b", 20000);
            assertEquals(2, tc.getTrucks().size());
        }
        catch (Exception e){

        }
    }
}