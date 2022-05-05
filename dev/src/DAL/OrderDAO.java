package DAL;

import BusinessLayer.Order;

import java.util.HashMap;

public class OrderDAO {
    private HashMap<Integer, HashMap<Integer, Order>> BN_To_Orders;


    public OrderDAO(){
        BN_To_Orders = new HashMap<Integer,HashMap<Integer,Order>>();
    }

    public void select(){}
    public void insert(){}
    public void delete(){}
    public void update(){}
}
