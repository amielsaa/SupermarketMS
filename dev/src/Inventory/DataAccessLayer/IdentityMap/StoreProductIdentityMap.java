package Inventory.DataAccessLayer.IdentityMap;

import Inventory.BuisnessLayer.Objects.StoreProduct;

import java.util.*;

public class StoreProductIdentityMap {

    private Map<Integer, List<StoreProduct>> storeProductsMap;
    private boolean pulled_all_data = false;

    public StoreProductIdentityMap() {
        this.storeProductsMap = new HashMap<>();
    }

    public void deleteAll() {
        storeProductsMap = new HashMap<>();
        pulled_all_data = false;
    }

    //TODO: override to update
    public List<StoreProduct> addStoreProduct(int productid, StoreProduct sp) {
        if(storeProductsMap.containsKey(productid)) {
            for(int i=0;i<storeProductsMap.get(productid).size();i++) {
                StoreProduct cur = storeProductsMap.get(productid).get(i);
                if(!cur.isNull() && cur.getExpDate().equals(sp.getExpDate()) && cur.getStoreId() == sp.getStoreId())
                    storeProductsMap.get(productid).remove(cur);
            }
            storeProductsMap.get(productid).add(sp);
        }
        else{
            storeProductsMap.put(productid, new ArrayList<>());
            storeProductsMap.get(productid).add(sp);
        }

        return storeProductsMap.get(productid);
    }

    public void deleteSP(int id) {
        if(storeProductsMap.containsKey(id))
            storeProductsMap.remove(id);
    }

    public boolean storeProductsExists(int productid, int storeid, Date expdate) {
        return storeProductsMap.containsKey(productid) && !storeProductsMap.get(productid).isEmpty() && storeProductsMap.get(productid).stream().anyMatch((s)->!s.isNull() && s.getExpDate().equals(expdate) &&  s.getStoreId()==storeid);
    }

    public boolean isPulled_all_data() {
        return pulled_all_data;
    }

    public void setPulled_all_data(boolean pulled_all_data) {
        this.pulled_all_data = pulled_all_data;
    }

    public Map<Integer, List<StoreProduct>> getStoreProductsMap() {
        return storeProductsMap;
    }
}
