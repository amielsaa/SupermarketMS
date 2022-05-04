package DAL;

import BusinessLayer.Order;
import BusinessLayer.Supplier;

import java.util.HashMap;

public class OrderMapper {
    private HashMap<Integer, HashMap<Integer, Order>> BN_To_Orders;

    public OrderMapper() {
        BN_To_Orders = new HashMap<Integer,HashMap<Integer,Order>>();
    }

    public void select(){}
    public void insert(){}
    public void delete(){}
    public void update(){}
}
