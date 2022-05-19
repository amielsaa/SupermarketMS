package SupplierInventory.tests;

import SupplierInventory.SIService;
import Suppliers.BusinessLayer.Supplier;
import Suppliers.ServiceLayer.DummyObjects.DOrder;
import Suppliers.ServiceLayer.DummyObjects.DSupplier;
import Suppliers.ServiceLayer.Response;
import misc.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class integrationTest {
    SIService siService = new SIService();
    Map<Pair<String, String>, Integer> demandedSupplies = new HashMap<Pair<String, String>, Integer>();
    Set<Integer> days=new HashSet<>();

    @BeforeEach
    void setUp() {
        demandedSupplies.put(new Pair<>("milk", "Tnuva"), 100);
        Pair milkTnuva=new Pair("milk","Tnuva");
        HashMap<Pair<String,String>,Double> item_To_Price=new HashMap<>();
        item_To_Price.put(milkTnuva,(double)1);
        HashMap<Pair<String,String>,HashMap<Integer,Integer>> item_Num_To_Quantity_To_Discount=new HashMap<>();
        item_Num_To_Quantity_To_Discount.put(milkTnuva,new HashMap<Integer,Integer>());
        item_Num_To_Quantity_To_Discount.get(milkTnuva).put(10,5);
        item_Num_To_Quantity_To_Discount.get(milkTnuva).put(100,10);
        item_Num_To_Quantity_To_Discount.get(milkTnuva).put(500,20);
        days.add(1);
        siService.addSupplier("ari",123456789,1,"check",days,"ari","0508639353",item_To_Price,item_Num_To_Quantity_To_Discount,true);

    }

    @AfterEach
    void tearDown() {
        Map<Pair<String, String>, Integer> demandedSupplies = new HashMap<Pair<String, String>, Integer>();
    }


    @Test //1
    void makeOrderToSuppliers() {
        demandedSupplies.put(new Pair<>("milk", "Tnuva"), 100);

        Response<List<DOrder>> res  = siService.MakeOrderToSuppliers(demandedSupplies);
        DOrder dOrder = res.getData().get(0);
        Assertions.assertTrue(dOrder.getSupplier_BN() ==123456789 && dOrder.getOrder_Id() ==0);
    }
    @Test //2
    void makeOrderToSuppliersFail(){

    }
    @Test //3
    void makeRoutineOrder(){
    }
    @Test //4
    void makeRoutineOrderFail(){}

    @Test //5
    void makeRoutineOrderAdd(){}
    @Test //6
    void makeRoutineOrderAddFail(){}

    @Test //7
    void makeRoutineOrderUpdate(){}
    @Test //8
    void makeRoutineOrderUpdateFail(){}

    @Test //9
    void getAllSuppliers(){}
    @Test //10
    void getAllRoutineOrders(){}

}