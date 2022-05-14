package DeliveryModule.DataAccessLayer;

import Utilities.Pair;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class DeliveryDestinationItemsDAO {
    LinkedHashMap<Pair<Integer,Integer>,Pair<String,Integer>> deliveryDestinationItemsCache;

    public void addItemToDeliveryDestination(int deliveryId, int siteId, String item, int quantity) {
    }

    public void removeItemFromDestination(int siteId, String item) {
    }

    public void removeItemsOfDestination(int deliveryId, int siteId) {
    }

    public void editItemQuantity(int deliveryId, int siteId, String item, int quantity) {
    }

    public HashMap<String, Integer> getItemsOfDest(int deliveryId, int siteId) {
        return null;
    }
}
