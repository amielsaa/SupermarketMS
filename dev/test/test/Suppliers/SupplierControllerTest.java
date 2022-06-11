package Suppliers;

import Suppliers.BusinessLayer.Contact;
import Suppliers.BusinessLayer.Supplier;
import Suppliers.BusinessLayer.SupplierController;
import misc.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;

class SupplierControllerTest {

    SupplierController sController=new SupplierController();

    @BeforeEach
    void setUp() throws Exception {
        sController.DeleteAll();
        Pair milkTnuva=new Pair("Milk","Tnuva");
        HashMap<Pair<String,String>,Double> item_To_Price=new HashMap<>();
        item_To_Price.put(milkTnuva,(double)1);
        HashMap<Pair<String,String>,HashMap<Integer,Integer>> item_Num_To_Quantity_To_Discount=new HashMap<>();
        item_Num_To_Quantity_To_Discount.put(milkTnuva,new HashMap<Integer,Integer>());
        item_Num_To_Quantity_To_Discount.get(milkTnuva).put(10,5);
        item_Num_To_Quantity_To_Discount.get(milkTnuva).put(100,10);
        item_Num_To_Quantity_To_Discount.get(milkTnuva).put(500,20);
        Set<Integer> days=new HashSet<>();
        days.add(1);
        if(!sController.HasSupplier(123456789)) {
       //     Supplier a=sController.addSupplier("ari",123456789,1,"check",days,"ari","0508639353",item_To_Price,item_Num_To_Quantity_To_Discount,true);
        }

    }
    @Test
    void addSupplierToSuccess() {
        assertTrue(sController.HasSupplier(123456789));

    }
    @Test
    void addSupplierThatExists() {
        HashMap<Pair<String,String>,Double> forTest1=new HashMap<>();

        HashMap<Pair<String,String>,HashMap<Integer,Integer>> forTest3=new HashMap<>();
        Set<Integer> days=new HashSet<>();
        days.add(1);
        assertThrows(IllegalArgumentException.class,()->sController.addSupplier("ari",123456789,1,"check",days,"ari","050",forTest1,forTest3,true));

    }

/*
    @Test
    void removeSupplierToSuccess() throws DataFormatException {
        sController.removeSupplier(123456780);
       assertFalse(sController.HasSupplier(123456780));

    }

 */


    @Test
    void removeSupplierThatDoesntExists(){
        assertThrows(IllegalArgumentException.class,()->sController.removeSupplier(123456788));
    }


    @Test
    void addSupplierContact() throws DataFormatException {
        sController.addSupplierContact(123456789,"felix","1234");
        List<Contact> test=sController.getSupplier(123456789).getContacts();
        assertEquals("1234",test.get(1).getPhone_Num());
    }

    @Test
    void removeOnlySupplierContactFail() throws DataFormatException {
        assertThrows(IllegalArgumentException.class,()->sController.removeSupplierContact(123456789,"0508639353"));
    }
    @Test
    void removeSupplierContactThatDoesntExists() {
        assertThrows(IllegalArgumentException.class,()->sController.removeSupplierContact(123456789,"050863935"));

    }

    @Test
    void makeOrderMaxDiscount() {
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        Pair milk=new Pair("Milk","Tnuva");
        order.put(milk,1000);
        HashMap<Pair<String,String>,Pair<Double,Double>> fixedOrder= sController.makeOrder(123456789,order);
        assertEquals(800,fixedOrder.get(milk).getSecond());
    }
    @Test
    void makeOrderMiddleDiscount() {
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        Pair milk=new Pair("Milk","Tnuva");
        order.put(milk,100);
        HashMap<Pair<String, String>, Pair<Double, Double>> fixedOrder= sController.makeOrder(123456789,order);
        assertEquals(90,fixedOrder.get(milk).getSecond());
    }
    @Test
    void makeOrderLowestDiscount() {
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        Pair milk=new Pair("Milk","Tnuva");
        order.put(milk,20);
        HashMap<Pair<String, String>, Pair<Double, Double>> fixedOrder= sController.makeOrder(123456789,order);
        assertEquals(19,fixedOrder.get(milk).getSecond());
    }
    @Test
    void makeOrderThatFails(){
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();

        assertThrows(IllegalArgumentException.class,()-> sController.makeOrder(123456789,order));

    }
    @Test
    void makeOrderThatFailsIlleagalNumbers(){
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        Pair milk=new Pair("Milk","Shtraus");
        order.put(milk,-1);
        assertThrows(IllegalArgumentException.class,()-> sController.makeOrder(123456789,order));

    }








}