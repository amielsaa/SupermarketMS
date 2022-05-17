package Tests;

import BusinessLayer.Order;
import BusinessLayer.OrderController;
import misc.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest {

    OrderController oController=new OrderController();
    HashMap<Pair<String,String>,Integer> order=new HashMap<>();
    HashMap<Pair<String,String>, Pair<Double,Double>> fixedOrder=new HashMap<>();
    Pair<String,String> milk=new Pair("Milk","Tnuva");




    @BeforeEach
    void setUp() {
        oController.addSupplier(123456789);
        order.put(milk,100);
        order.put(milk,200);
        double forp1=70;
        double forp2=200;

        Pair p1=new Pair(milk,forp1);
        Pair p2=new Pair("glass",forp2);
        fixedOrder.put(1,p1);
        fixedOrder.put(2,p2);



    }

    @Test
    void getAllOrdersFromSupplierEmpty() throws DataFormatException {
        assertTrue(oController.getAllOrdersFromSupplier(123456789).isEmpty());
    }
    @Test
    void getAllOrdersFromSupplierNotEmpty() {
        oController.makeOrder(123456789,order,fixedOrder);
        oController.makeOrder(123456789,order,fixedOrder);
        assertTrue(oController.getAllOrdersFromSupplier(123456789).size()==2);

    }


    @Test
    void getOrderSuccess() {
        oController.makeOrder(123456789,order,fixedOrder);
        Order order=oController.getOrder(123456789,0);
        assertTrue(order.getOrder_Id()==0&&order.getItem_Num_To_Quantity().keySet().size()==2);

    }
    @Test
    void getOrderFail() {
        oController.makeOrder(123456789,order,fixedOrder);
        assertThrows(IllegalArgumentException.class,()->oController.getOrder(123456789,1));

    }


    @Test
    void makeOrdersuccess() {
        oController.makeOrder(123456789,order,fixedOrder);
        assertTrue(oController.getOrder(123456789,0).getOrder_Id()==0);
    }
    @Test
    void makeOrdersuccessFail() {
        assertThrows(NullPointerException.class,()->oController.makeOrder(12345678,order,fixedOrder));
    }

}