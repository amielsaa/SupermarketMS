package DeliveryModule.DataAccessLayer;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class DeliveryDestinationsDAO {
    LinkedHashMap<Integer, LinkedList<Integer>> deliveryDestinationCache;

    public DeliveryDestinationsDAO() {
        this.deliveryDestinationCache = new LinkedHashMap<>();
    }

    public void addDeliveryDestination(int deliveryId,int siteId) {
    }

    public void removeDeliveryDestination(int deliveryId,int siteId) {
    }

    public LinkedList<Integer> getDeliveryDestinations(int deliveryId){
        return null;
    }
}
