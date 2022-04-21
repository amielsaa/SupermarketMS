package Inventory.BuisnessLayer.Controller;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.BuisnessLayer.Objects.StoreProduct;

import java.util.*;

public class DataController {

    private List<Product> defectiveProducts;
    private List<Product> expiredProducts;
    private Map<Product,List<StoreProduct>> productListMap;
    private List<Category> categories;

    public DataController() {
        this.defectiveProducts = new ArrayList<>();
        this.expiredProducts = new ArrayList<>();
        this.productListMap = new HashMap<>();
        this.categories = new ArrayList<>();
        /**
         * timer set
         */
        setTimer();
    }



    public List<Product> getDefectiveProducts() {
        return defectiveProducts;
    }

    public List<Product> getExpiredProducts() {
        return expiredProducts;
    }

    public Map<Product, List<StoreProduct>> getProductListMap() {
        return productListMap;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Product findProductById(int id) {
        for(Product item : getProductListMap().keySet()) {
            if(item.getId() == id)
                return item;
        }
        throw new IllegalArgumentException("No such product exists.");
    }

    public List<Category> getCategoriesByName(List<String> cat) {
        List<Category> categoryList = new ArrayList<>();
        for (String it: cat) {
            categoryList.add(getCategories().stream().filter(category-> it.equals(category.getCategoryName()))
                    .findFirst().orElse(null));
        }
        return categoryList;
    }
    public void checkExpired()
    {
        Date now = new Date();
        for(Map.Entry<Product,List<StoreProduct>> mapSet:productListMap.entrySet())
        {
            for(StoreProduct sProduct:mapSet.getValue())
            {
                if(!(sProduct.getExpDate()).after(now)) {
                    if(!expiredProducts.contains(mapSet.getKey()))
                        expiredProducts.add(mapSet.getKey());
                    break;
                }
            }
        }
    }


    private void setTimer(){
        TimerTask expCheck = new TimerTask() {
            public void run() {
                checkExpired();
            }
        };
        Timer _timer = new Timer();
        long delay = 1000L;
        long period = 1000L * 60L * 60L * 24L;
        _timer.scheduleAtFixedRate(expCheck,delay,period);
    }
}
