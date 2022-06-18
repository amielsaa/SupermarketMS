package Inventory.DataAccessLayer.IdentityMap;

//import Inventory.ServiceLayer.Objects.Pair;
import misc.Pair;
import java.util.HashMap;
import java.util.Map;

public class PendingIdentityMap {
    private Map<Pair<String,String>,Pair<Double,Integer>> pendingMap;
    private boolean pulled_all_data;

    public PendingIdentityMap() {
        this.pendingMap = new HashMap<>();
        pulled_all_data = false;
    }


    public void addPending(Pair<String,String> productProducer, Pair<Double,Integer> priceQuantity) {
        pendingMap.put(productProducer,priceQuantity);
    }

    public boolean pendingExists(String name, String producer) {
        return pendingMap.keySet().stream().anyMatch((pair) -> pair.getFirst().equals(name) && pair.getSecond().equals(producer));
    }

    public void updateQuantity(String name, String producer, int quantity) {
        pendingMap.forEach((key,value)-> {if(key.getFirst().equals(name) && key.getSecond().equals(producer)) value.setSecond(quantity); });
    }

    public boolean isPulled_all_data() {
        return pulled_all_data;
    }

    public void setPulled_all_data(boolean pulled) {
        this.pulled_all_data = pulled;
    }

    public Map<Pair<String, String>, Pair<Double, Integer>> getPendings() {
        return pendingMap;
    }

    public Pair<Double,Integer> getPendingValue(String name, String producer) {
        for(Map.Entry<Pair<String, String>, Pair<Double, Integer>> entry : pendingMap.entrySet()) {
            if(entry.getKey().getFirst().equals(name) && entry.getKey().getSecond().equals(producer))
                return entry.getValue();
        }
        return null;
    }
}
