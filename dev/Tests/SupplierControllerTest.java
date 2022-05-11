
import BusinessLayer.Contact;
import BusinessLayer.SupplierController;
import misc.Days;
import misc.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SupplierControllerTest {
//    SupplierController sController=new SupplierController();
//
//    @BeforeEach
//    void setUp() {
//        HashMap<Integer,Double> item_To_Price=new HashMap<>();
//        item_To_Price.put(1,(double)1);
//        HashMap<Integer,String> item_To_Name=new HashMap<>();
//        item_To_Name.put(1,"Milk");
//        HashMap<Integer,HashMap<Integer,Integer>> item_Num_To_Quantity_To_Discount=new HashMap<>();
//        item_Num_To_Quantity_To_Discount.put(1,new HashMap<Integer,Integer>());
//        item_Num_To_Quantity_To_Discount.get(1).put(10,5);
//        item_Num_To_Quantity_To_Discount.get(1).put(100,10);
//        item_Num_To_Quantity_To_Discount.get(1).put(500,20);
//
//
//
//        sController.addSupplier("ari",123456789,1,"check","ari","0508639353",item_To_Price,item_Num_To_Quantity_To_Discount,item_To_Name,false,false,new LinkedHashSet<Integer>());
//
//    }
//    @Test
//    void addSupplierToSuccess() {
//        assertTrue(sController.HasSupplier(123456789));
//
//    }
//    @Test
//    void addSupplierThatExists() {
//        HashMap<Integer,Double> forTest1=new HashMap<>();
//        HashMap<Integer,String> forTest2=new HashMap<>();
//        HashMap<Integer,HashMap<Integer,Integer>> forTest3=new HashMap<>();
//        assertThrows(IllegalArgumentException.class,()->sController.addSupplier("ari",123456789,1,"check","ari","0508639353",forTest1,forTest2,forTest3,false,false,new LinkedHashSet<Integer>()));
//
//    }
//
//
//    @Test
//    void removeSupplierToSuccess() {
//        sController.removeSupplier(123456789);
//       assertFalse(sController.HasSupplier(123456789));
//
//    }
//    @Test
//    void removeSupplierThatDoesntExists(){
//        assertThrows(IllegalArgumentException.class,()->sController.removeSupplier(123456788));
//    }
//
//    @Test
//    void updateSupplierDeliveryDaysToSuccess() {
//        sController.addSupplierDeliveryDay(123456789,1);
//        Set<Days> test=sController.getSupplier(123456789).getDays_To_Deliver();
//        assertTrue(test.contains(Days.sunday));
//
//    }
//    @Test
//    void addSupplierContact() {
//        sController.addSupplierContact(123456789,"felix","123");
//        List<Contact> test=sController.getSupplier(123456789).getContacts();
//        assertEquals("123",test.get(1).getPhone_Num());
//    }
//
//    @Test
//    void removeSupplierContact() {
//        sController.removeSupplierContact(123456789,"0508639353");
//        List<Contact> test=sController.getSupplier(123456789).getContacts();
//        assertTrue(test.isEmpty());
//    }
//    @Test
//    void removeSupplierContactThatDoesntExists() {
//        assertThrows(IllegalArgumentException.class,()->sController.removeSupplierContact(123456789,"050863935"));
//
//    }
//
//    @Test
//    void makeOrderMaxDiscount() {
//        HashMap<Integer,Integer> order=new HashMap<>();
//        order.put(1,1000);
//        HashMap<Integer,Pair<String,Double>> fixedOrder= sController.makeOrder(123456789,order);
//        assertEquals(800,fixedOrder.get(1).getSecond());
//    }
//    @Test
//    void makeOrderMiddleDiscount() {
//        HashMap<Integer,Integer> order=new HashMap<>();
//        order.put(1,100);
//        HashMap<Integer,Pair<String,Double>> fixedOrder= sController.makeOrder(123456789,order);
//        assertEquals(90,fixedOrder.get(1).getSecond());
//    }
//    @Test
//    void makeOrderLowestDiscount() {
//        HashMap<Integer,Integer> order=new HashMap<>();
//        order.put(1,20);
//        HashMap<Integer,Pair<String,Double>> fixedOrder= sController.makeOrder(123456789,order);
//        assertEquals(19,fixedOrder.get(1).getSecond());
//    }
//    @Test
//    void makeOrderThatFails(){
//        HashMap<Integer,Integer> order=new HashMap<>();
//        order.put(2,1000);
//        assertThrows(IllegalArgumentException.class,()-> sController.makeOrder(123456789,order));
//
//    }
//    @Test
//    void makeOrderThatFailsIlleagalNumbers(){
//        HashMap<Integer,Integer> order=new HashMap<>();
//        order.put(1,-1);
//        assertThrows(IllegalArgumentException.class,()-> sController.makeOrder(123456789,order));
//
//    }
//
//
//
//
//    @Test
//    void addSupplierDeliveryDay() {
//        sController.addSupplierDeliveryDay(123456789,1);
//        Set<Days> days= sController.getSupplier(123456789).getDays_To_Deliver();
//        assertTrue(days.contains(Days.sunday));
//        }
//
//
//
//    @Test
//    void removeSupplierDeliveryDay() {
//        sController.addSupplierDeliveryDay(123456789,1);
//        sController.removeSupplierDeliveryDay(123456789,1);
//        Set<Days> days= sController.getSupplier(123456789).getDays_To_Deliver();
//        assertFalse(days.contains(Days.sunday));
//
//
//
//
//    }


}