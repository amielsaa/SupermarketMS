package DeliveryModule.DataAccessLayer;

import DeliveryModule.BusinessLayer.Delivery;

import java.util.ArrayList;
import java.util.HashMap;

public class UpcomingDeliveryDAO {
    private HashMap<Integer, Delivery> upcomingDeliveryCache;

    //ToDo
    public ArrayList<Delivery> getUpcomingDeliveries() {
        return null;
    }

    //ToDo
    public Delivery getUpcomingDelivery(int deliveryId) {
        return null;
    }

    //ToDo: auto generate ids
    public Integer addUpcomingDelivery(Delivery delivery){return 1;}

    public void setDriverId(int deliveryId, int newDriverId) {
    }

    public void setTruck(int deliveryId, int newTruckId) {
    }

    public void setWeight(int weight) {
    }

    public void deleteUpcomingDelivery(int deliveryId) {
    }
}
