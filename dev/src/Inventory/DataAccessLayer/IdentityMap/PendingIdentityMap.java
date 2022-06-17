package Inventory.DataAccessLayer.IdentityMap;

//import Inventory.ServiceLayer.Objects.Pair;
import misc.Pair;
import java.util.HashMap;
import java.util.Map;

public class PendingIdentityMap {
    private Map<Pair<String,String>,Pair<Integer,Integer>> pendingMap;
    private boolean pulled_all_data;

    public PendingIdentityMap() {
        this.pendingMap = new HashMap<>();
        pulled_all_data = false;
    }


    public void addPending(Pair<String,String> productProducer, Pair<Integer,Integer> priceQuantity) {
        pendingMap.put(productProducer,priceQuantity);
    }

    public boolean pendingExists(String name, String producer) {
        return pendingMap.keySet().stream().anyMatch((pair) -> pair.getFirst().equals(name) && pair.getSecond().equals(producer));
    }

    public boolean isPulled_all_data() {
        return pulled_all_data;
    }

    public void setPulled_all_data(boolean pulled) {
        this.pulled_all_data = pulled;
    }

    public Map<Pair<String, String>, Pair<Integer, Integer>> getPendings() {
        return pendingMap;
    }

    public Pair<Integer,Integer> getPendingValue(String name, String producer) {
        for(Map.Entry<Pair<String, String>, Pair<Integer, Integer>> entry : pendingMap.entrySet()) {
            if(entry.getKey().getFirst().equals(name) && entry.getKey().getSecond().equals(producer))
                return entry.getValue();
        }
        return null;
    }
}
