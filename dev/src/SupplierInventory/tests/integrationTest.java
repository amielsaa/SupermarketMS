package SupplierInventory.tests;

import SupplierInventory.SIService;
import Suppliers.ServiceLayer.DummyObjects.DOrder;
import Suppliers.ServiceLayer.DummyObjects.DRoutineOrder;
import Suppliers.ServiceLayer.DummyObjects.DSupplier;
import Suppliers.ServiceLayer.Response;
import misc.Days;
import misc.Pair;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class integrationTest {
    static SIService siService = new SIService();
    static Map<Pair<String, String>, Integer> demandedSupplies = new HashMap<Pair<String, String>, Integer>();
    static Set<Integer> days=new HashSet<>();

    @BeforeAll
    static void setUp() {
        siService.DeleteAll();
//        siService.SetStartingValues();
        siService.deleteAllData();
        demandedSupplies.put(new Pair<>("Milk", "Tnuva"), 100);
        Pair milkTnuva=new Pair("Milk","Tnuva");
        Pair applePerot=new Pair("apple","Perot");
        HashMap<Pair<String,String>,Double> item_To_Price=new HashMap<>();
        item_To_Price.put(milkTnuva,(double)1);
        item_To_Price.putIfAbsent(applePerot,(double)1);
        HashMap<Pair<String,String>,HashMap<Integer,Integer>> item_Num_To_Quantity_To_Discount=new HashMap<>();
        item_Num_To_Quantity_To_Discount.put(milkTnuva,new HashMap<Integer,Integer>());
        item_Num_To_Quantity_To_Discount.get(milkTnuva).put(10,5);
        item_Num_To_Quantity_To_Discount.get(milkTnuva).put(100,10);
        item_Num_To_Quantity_To_Discount.get(milkTnuva).put(500,20);
        item_Num_To_Quantity_To_Discount.put(applePerot,new HashMap<Integer,Integer>());
        item_Num_To_Quantity_To_Discount.get(applePerot).put(50,5);
        item_Num_To_Quantity_To_Discount.get(applePerot).put(100,10);
        item_Num_To_Quantity_To_Discount.get(applePerot).put(1000,20);
        days.add(1);
        days.add(2);
        siService.addSupplier("ari",123456789,1,"check",days,"ari","0508639353",item_To_Price,item_Num_To_Quantity_To_Discount,true);

    }

    @AfterAll
    static void tearDown() {
        Map<Pair<String, String>, Integer> demandedSupplies = new HashMap<Pair<String, String>, Integer>();
    }


    @Test //1
    void makeOrderToSuppliers() {
        //demandedSupplies.put(new Pair<>("milk", "Tnuva"), 100);

        Response<List<DOrder>> res  = siService.MakeOrderToSuppliers(demandedSupplies);
        if(!res.getData().isEmpty()) {
            DOrder dOrder = res.getData().get(0);
            Assertions.assertTrue(dOrder.getSupplier_BN() == 123456789 && dOrder.getOrder_Id() == 0);
        }
        else
            Assertions.assertTrue(false);
    }
    @Test //2
    void makeOrderToSuppliersFail(){


    }
    @Test //3
    void makeRoutineOrder(){
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        Pair milk=new Pair("Milk","Tnuva");
        order.put(milk,100);
        Set<Integer> days=new HashSet<>();
        days.add(1);
        Response<DRoutineOrder> i= siService.makeRoutineOrder(123456789,order,days);
        assertTrue(i.getData().getSupplier_BN()==123456789&&i.getData().getDays().contains(Days.sunday));
    }
    @Test //4
    void makeRoutineOrderFail(){
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        Pair milk=new Pair("Milk","Tnuva");
        order.put(milk,100);
        Set<Integer> days=new HashSet<>();
        days.add(3);
        assertThrows(IllegalArgumentException.class,()->siService.makeRoutineOrder(123456789,order,days));
    }

    @Test //5
    void makeRoutineOrderAdd(){
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        Pair milk=new Pair("Milk","Tnuva");
        order.put(milk,100);
        Set<Integer> days=new HashSet<>();
        days.add(1);
        siService.makeRoutineOrder(123456789,order,days);
        Response<DRoutineOrder>  i=siService.addOrUpdateRoutineOrder(123456789,0,"Milk","Tnuva",500);
        assertTrue(i.getData().getPriceBeforeDiscount()==500&&i.getData().getFinal_Price()==400);
    }
    @Test //6
    void makeRoutineOrderAddFail(){
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        Pair milk=new Pair("Milk","Shtraus");
        order.put(milk,100);
        Set<Integer> days=new HashSet<>();
        days.add(1);
        siService.makeRoutineOrder(123456789,order,days);
        assertThrows(IllegalArgumentException.class,()->siService.makeRoutineOrder(123456789,order,days));
    }

    @Test //7
    void makeRoutineOrderUpdate(){
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        Pair milk=new Pair("Milk","Shtraus");
        order.put(milk,100);
        Set<Integer> days=new HashSet<>();
        days.add(1);
        siService.makeRoutineOrder(123456789,order,days);
        Response<DRoutineOrder>  i=siService.addOrUpdateRoutineOrder(123456789,0,"Apple","Perot",1000);
        assertTrue(i.getData().getPriceBeforeDiscount()==1100&&i.getData().getFinal_Price()==890);

    }

    @Test //8
    void getAllSuppliers(){
        Response<List<DSupplier>> list=siService.getAllSuppliers();
        assertTrue(list.getData().size()==1);

    }
    @Test //9
    void getAllRoutineOrders(){
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        Pair milk=new Pair("Milk","Shtraus");
        order.put(milk,100);
        Set<Integer> days=new HashSet<>();
        days.add(1);
        siService.makeRoutineOrder(123456789,order,days);
        HashMap<Pair<String,String>,Integer> order2=new HashMap<>();
        Pair apple=new Pair("Apple","Perot");
        order.put(apple,100);
        Set<Integer> days2=new HashSet<>();
        days.add(2);
        siService.makeRoutineOrder(123456789,order,days2);
        Response<List<DRoutineOrder>> routineOrders=siService.getAllRoutineOrders();
        assertTrue(routineOrders.getData().size()==2);
    }
    @Test //10
    void MakeOrderMinQuantity(){
        Response<List<DOrder>> dOrderResponse = siService.MakeOrderMinQuantity();
        Assertions.assertTrue(dOrderResponse.getData().isEmpty());
    }

}