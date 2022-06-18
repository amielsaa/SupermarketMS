package Integration;

import Employee.ServiceLayer.Gateway;
import SupplierInventory.SIService;
import Suppliers.ServiceLayer.DummyObjects.DOrder;
import Suppliers.ServiceLayer.DummyObjects.DRoutineOrder;
import Suppliers.ServiceLayer.DummyObjects.DSupplier;
import Utilities.Response;
import misc.Days;
import misc.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class SupplierInventoryIntegrationTests {
    Gateway gateway;
    SIService siService;

    Map<Pair<String, String>, Integer> demandedSupplies = new HashMap<Pair<String, String>, Integer>();
    Set<Integer> days=new HashSet<>();

    @Before
    public void setUp() {
        this.gateway = new Gateway();
        this.siService = gateway.getSIService().getData();

        siService.DeleteAll();
        siService.deleteAllData();
        //siService.InsertData();

        demandedSupplies.put(new Pair<>("Milk", "Tnuva"), 100);
        Pair milkTnuva=new Pair("Milk","Tnuva");
        Pair applePerot=new Pair("Apple","Perot");
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
        siService.addSupplier("ari",123456789,1,"check",days,"ari","0508639353",item_To_Price,item_Num_To_Quantity_To_Discount,true,"", "");

    }

    @After
    public void tearDown() {
        Map<Pair<String, String>, Integer> demandedSupplies = new HashMap<Pair<String, String>, Integer>();
    }


    @Test
        //1
    public  void makeOrderToSuppliers() {
        //demandedSupplies.put(new Pair<>("milk", "Tnuva"), 100);

        Response<List<DOrder>> res  = siService.MakeOrderToSuppliers(demandedSupplies);
        if(!res.getData().isEmpty()) {
            DOrder dOrder = res.getData().get(0);
            assertTrue(dOrder.getSupplier_BN() == 123456789 && dOrder.getOrder_Id() == 0);
        }
        else
            assertTrue(false);
    }
    @Test //2
    public void makeOrderToSuppliersFail(){
        demandedSupplies.clear();
        demandedSupplies.put(new Pair<>("gun","Glock"), 5);
        Response<List<DOrder>> res  = siService.MakeOrderToSuppliers(demandedSupplies);
        assertTrue(res.getData().isEmpty());

    }
    @Test //3
    public void makeRoutineOrder(){
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        Pair milk=new Pair("Milk","Tnuva");
        order.put(milk,100);
        Set<Integer> days=new HashSet<>();
        days.add(1);
        Response<DRoutineOrder> i= siService.makeRoutineOrder(123456789,order,days);
        assertTrue(i.getData().getSupplier_BN()==123456789&&i.getData().getDays().contains(Days.sunday));
    }
    @Test //4
    public void makeRoutineOrderFail(){
        //cannot give a day that the supplier doesnt supply
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        Pair milk=new Pair("Milk","Tnuva");
        order.put(milk,100);
        Set<Integer> days=new HashSet<>();
        days.add(3);
        Response<DRoutineOrder> i=siService.makeRoutineOrder(123456789,order,days);
        assertFalse(i.isSuccess());
    }

    @Test //5
    public void makeRoutineOrderUpdate(){
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
    public void makeRoutineOrderAddFail(){
        //cannot order items which is not in the QA
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        Pair milk=new Pair("Milk","Shtraus");
        order.put(milk,100);
        Set<Integer> days=new HashSet<>();
        days.add(1);
        Response<DRoutineOrder> i=siService.makeRoutineOrder(123456789,order,days);
        assertFalse(i.isSuccess());
    }

    @Test //7
    public void makeRoutineOrderAdd(){
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        Pair milk=new Pair("Milk","Tnuva");
        order.put(milk,100);
        Set<Integer> days=new HashSet<>();
        days.add(1);
        Response<DRoutineOrder>  i=siService.makeRoutineOrder(123456789,order,days);
        Response<DRoutineOrder>  j=siService.addOrUpdateRoutineOrder(123456789,0,"Apple","Perot",1000);
        assertTrue(j.getData().getPriceBeforeDiscount()==1100&&j.getData().getFinal_Price()==890);

    }

    @Test //8
    public void getAllSuppliers(){
        Response<List<DSupplier>> list=siService.getAllSuppliers();
        assertTrue(list.getData().size()==1);

    }
    @Test //9
    public void getAllRoutineOrders(){
        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        Pair milk=new Pair("Milk","Tnuva");
        order.put(milk,100);
        Set<Integer> days=new HashSet<>();
        days.add(1);
        Response<DRoutineOrder> i=siService.makeRoutineOrder(123456789,order,days);
        HashMap<Pair<String,String>,Integer> order2=new HashMap<>();
        Pair apple=new Pair("Apple","Perot");
        order.put(apple,100);
        Set<Integer> days2=new HashSet<>();
        days2.add(2);
        Response<DRoutineOrder> j=siService.makeRoutineOrder(123456789,order,days2);
        Response<List<DRoutineOrder>> routineOrders=siService.getAllRoutineOrders();
        assertTrue(routineOrders.getData().size()==2);
    }
    @Test //10
    public void MakeOrderMinQuantity(){
        Response<Pair<String,List<DOrder>>> dOrderResponse = siService.MakeOrderMinQuantity();
        assertTrue(dOrderResponse.getData().getFirst().isEmpty());
    }

}