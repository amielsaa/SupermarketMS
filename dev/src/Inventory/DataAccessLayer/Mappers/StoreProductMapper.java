package Inventory.DataAccessLayer.Mappers;

import Inventory.BuisnessLayer.Objects.StoreProduct;

import java.util.*;

public class StoreProductMapper {

    private Map<Integer, List<StoreProduct>> storeProductsMap;

    public StoreProductMapper() {
        this.storeProductsMap = new HashMap<>();
    }

    //TODO: override to update
    public List<StoreProduct> addStoreProduct(int productid, StoreProduct sp) {
        if(storeProductsMap.containsKey(productid)) {
            for(int i=0;i<storeProductsMap.get(productid).size();i++) {
                StoreProduct cur = storeProductsMap.get(productid).get(i);
                if(!cur.isNull() && cur.getExpDate().equals(sp.getExpDate()) && cur.getStoreId() == sp.getStoreId())
                    storeProductsMap.get(productid).remove(cur);
            }

            //storeProductsMap.get(productid).removeIf((s)->!s.isNull() && s.getExpDate().equals(sp.getExpDate()) && s.getStoreId() == sp.getStoreId());
            storeProductsMap.get(productid).add(sp);
//            StoreProduct spInMap = storeProductsMap.get(productid).stream().filter(s-> sp.getStoreId()==s.getStoreId() && sp.getExpDate().equals(s.getExpDate())).findFirst().orElse(null);
//            if(spInMap==null)
//                storeProductsMap.get(productid).add(sp);
//            else
        }
        else{
            storeProductsMap.put(productid, new ArrayList<>());
            storeProductsMap.get(productid).add(sp);
        }

        return storeProductsMap.get(productid);
    }

    public boolean storeProductsExists(int productid, int storeid, Date expDate) {
        return storeProductsMap.containsKey(productid) && !storeProductsMap.get(productid).isEmpty() && storeProductsMap.get(productid).stream().anyMatch((s)->!s.isNull() &&  s.getStoreId()==storeid && s.getExpDate().equals(expDate));
    }



}
