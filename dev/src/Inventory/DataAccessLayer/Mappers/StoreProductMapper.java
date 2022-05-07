package Inventory.DataAccessLayer.Mappers;

import Inventory.BuisnessLayer.Objects.StoreProduct;

import java.util.*;

public class StoreProductMapper {

    private Map<Integer, List<StoreProduct>> storeProductsMap;

    public StoreProductMapper() {
        this.storeProductsMap = new HashMap<>();
    }

    public List<StoreProduct> addStoreProduct(int productid, StoreProduct sp) {
        if(storeProductsMap.containsKey(productid)) {
            StoreProduct spInMap = storeProductsMap.get(productid).stream().filter(s-> sp.getStoreId()==s.getStoreId() && sp.getExpDate().equals(s.getExpDate())).findFirst().orElse(null);
            if(spInMap==null)
                storeProductsMap.get(productid).add(sp);
        }

        else
            storeProductsMap.put(productid, Arrays.asList(sp));
        return storeProductsMap.get(productid);
    }


}
