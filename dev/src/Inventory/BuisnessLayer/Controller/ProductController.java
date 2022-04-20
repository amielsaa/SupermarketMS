package Inventory.BuisnessLayer.Controller;

import Inventory.BuisnessLayer.Objects.*;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.spi.CalendarDataProvider;

public class ProductController {
//    private List<Product> defectiveProducts;
//    private List<Product> expiredProducts;
//    private Map<Product,List<StoreProduct>> productListMap;
//    private List<Category> categories;

    private DataController data;
    private int productId; //TODO: add to diagram
    private int storeId; //TODO: add to diagram

    public ProductController(DataController data) {
//        this.defectiveProducts = new ArrayList<>();
//        this.expiredProducts = new ArrayList<>();
//        this.productListMap = new HashMap<>();
//        this.categories = new ArrayList<>();
        this.productId = 0;
        this.data = data;
        //adding categories for testing
//        Collections.addAll(this.categories,new Category("Diary"),new Category("Milk"),new Category("Size"));

    }

    /**
     *
     * @return
     */

    public String getAllProducts() {
        String products = "";
        for(Map.Entry<Product,List<StoreProduct>> entry : data.getProductListMap().entrySet()) {
            StoreProduct sp = getStoreProductByStoreId(entry.getValue());
            if(sp!=null)
                products += sp.toString() +"\n";
        }
        return products;
    }

    private StoreProduct getStoreProductByStoreId(List<StoreProduct> list) {
        for(StoreProduct item : list){
            if(item.getStoreId() == storeId)
                return item;
        }
        return null;
    }


    /**
     *
     * @param name
     * @param producer
     * @param buyingPrice
     * @param sellingPrice
     * @param categories
     * @return
     */
    public Product addProduct(String name, String producer, double buyingPrice,double sellingPrice, List<String> categories) {
        //check for base conditions that satisfy the requirements - else throw exception
        boolean isCategoriesExists = categories.containsAll(categories);
        if(!isCategoriesExists)
            throw new IllegalArgumentException("Categories doesn't exists.");
        //TODO: capitalize all categories names
        List<Category> productCategories = getCategoriesByName(categories);
        Product product = new Product(productId++,name,producer,sellingPrice,buyingPrice,productCategories);
        data.getProductListMap().put(product,new ArrayList<>());
        return product;

    }

    private List<Category> getCategoriesByName(List<String> cat) {
        List<Category> categoryList = new ArrayList<>();
        for (String it: cat) {
            categoryList.add(data.getCategories().stream().filter(category-> it.equals(category.getCategoryName()))
                    .findFirst().orElse(null));
        }
        return categoryList;
    }

    /**
     *
     * @param prodName
     * @param prodProducer
     * @param quantityInStore
     * @param quantityInWarehouse
     * @param expDate
     * @param locations
     */
    public StoreProduct addStoreProduct(String prodName, String prodProducer, int quantityInStore, int quantityInWarehouse, String expDate, String locations) {
        Product currentProduct = findProductByProdName(prodName,prodProducer);
        Date curExpDate = getDateByString(expDate);
        List<Location> curLocations = getLocationListByString(locations);
        StoreProduct sp = new StoreProduct(storeId,quantityInStore,quantityInWarehouse,curExpDate,curLocations);
        data.getProductListMap().get(currentProduct).add(sp);
        return sp;
    }

    //locations of items will represenet as follow: warehouse-1-2&store-1-2
    private List<Location> getLocationListByString(String location) {
        List<Location> locationList = new ArrayList<>();
        String[] locations = location.split("&");
        for(String item : locations) {
            String[] itemComponent = item.split("-");
            Locations eLocation = Locations.valueOf(itemComponent[0].toUpperCase());
            locationList.add(new Location(eLocation,Integer.parseInt(itemComponent[1]),Integer.parseInt(itemComponent[2])));
        }
        return locationList;

    }

    private Date getDateByString(String expDate) {
        String[] res = expDate.split("/");
        if(res.length==3 && res[2].length()==4)
            return new Date(Integer.parseInt(res[2]),Integer.parseInt(res[1]),Integer.parseInt(res[0]));
        throw new IllegalArgumentException("Date format isn't valid.");
    }

    private Product findProductByProdName(String name, String producer) {
        for(Map.Entry<Product, List<StoreProduct>> entry : data.getProductListMap().entrySet()) {
            if(entry.getKey().getName().equals(name) && entry.getKey().getProducer().equals(producer))
                return entry.getKey();
        }
        throw new IllegalArgumentException("No such product exists.");
    }




    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
