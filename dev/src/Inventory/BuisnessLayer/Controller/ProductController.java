package Inventory.BuisnessLayer.Controller;

import Inventory.BuisnessLayer.Objects.*;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.spi.CalendarDataProvider;

public class ProductController {

    private DataController data;
    private int productId; //TODO: add to diagram
    private int storeId; //TODO: add to diagram

    public ProductController(DataController data) {
        this.productId = 0;
        this.data = data;
        addProducts();

    }

    private void addProducts() {
        addProduct("Shampoo","Kef",10.20,12.50, "Wash,Shampoo,Size");
        addProduct("Chips","Osem",7.20,10.50,"Snacks,Salty,Weight");
        addProduct("Cini Minis","Telma",25,32,"Cereal,Sweets,Weight");
        addProduct("Milk","Tnuva",7.50,10,"Diary,Milk,Size");
        addProduct("Cottage","Tnuva",4.50,7.90,"Diary,Delicacy,ML");
        addProduct("Coffee","Turkey",6.50,11,"Hot Drink,Coffee,Weight");
        addProduct("Banana","Perot",5,6,"Fruits,Sweets,Weight");
        addProduct("Apple","Perot",4,5,"Fruits,Sweets,Weight");
    }

    /**
     *
     * @return
     */

    public Map<Product,StoreProduct> getAllProducts() {
        Map<Product,StoreProduct> products = new HashMap<>();
        for(Map.Entry<Product,List<StoreProduct>> entry : data.getProductListMap().entrySet()) {
            StoreProduct sp = getStoreProductByStoreId(entry.getValue());
            if(sp!=null)
                products.put(entry.getKey(),sp);
            else
                products.put(entry.getKey(),new StoreProduct(storeId,0,0,null,null));
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
    public Product addProduct(String name, String producer, double buyingPrice,double sellingPrice, String categories) {
        //check for base conditions that satisfy the requirements - else throw exception
        List<String> categoriesList = stringToCategoryList(categories);
        categoriesExistance(categoriesList);
        //TODO: capitalize all categories names
        List<Category> productCategories = getCategoriesByName(categoriesList);
        Product product = new Product(productId++,name,producer,buyingPrice,sellingPrice,productCategories);
        data.getProductListMap().put(product,new ArrayList<>());
        return product;

    }

    private void categoriesExistance(List<String> categories) {
        for(String item : categories) {
            Category curCategory = data.getCategories().stream().filter(category-> item.equals(category.getCategoryName()))
                    .findFirst().orElse(null);
            if(curCategory == null)
                throw new IllegalArgumentException(item + " doesn't exists.");
        }
    }

    private List<String> stringToCategoryList(String categoryString) {
        List<String> categories = new ArrayList<>();
        String[] categoryArray = categoryString.split(",");
        for(int i=0;i<categoryArray.length;i++)
            categoryArray[i] = categoryArray[i].trim();
        Collections.addAll(categories,categoryArray);
        return categories;
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
     * @param id
     * @param quantityInStore
     * @param quantityInWarehouse
     * @param expDate
     * @param locations
     */
    public StoreProduct addStoreProduct(int id, int quantityInStore, int quantityInWarehouse, String expDate, String locations) {
        Product currentProduct = data.findProductById(id);
        Date curExpDate = getDateByString(expDate);
        List<Location> curLocations = getLocationListByString(locations);
        StoreProduct sp = new StoreProduct(storeId,quantityInStore,quantityInWarehouse,curExpDate,curLocations);
        addStoreProductToMap(currentProduct,sp);
        return sp;
    }

    private void addStoreProductToMap(Product p,StoreProduct sp) {
        List<StoreProduct> spList = data.getProductListMap().get(p);
        spList.removeIf(item -> item.getStoreId() == storeId);
        data.getProductListMap().get(p).add(sp);
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
            return new Date(Integer.parseInt(res[2])-1900,Integer.parseInt(res[1])-1,Integer.parseInt(res[0]));
        throw new IllegalArgumentException("Date format isn't valid.");
    }


    public Product addDefectiveProduct(int productId) {
        Product product = data.findProductById(productId);
        if(data.getDefectiveProducts().contains(product))
            throw new IllegalArgumentException("Product already reported as defective.");
        data.getDefectiveProducts().add(product);
        return product;
    }
    public String addDiscountByName(String name,String producer, int discount, String date)
    {
        Date discountExpDate = getDateByString(date);
        for(Map.Entry<Product,List<StoreProduct>> entry : data.getProductListMap().entrySet()){
            if((entry.getKey().getName().equals(name))&&(entry.getKey().getProducer().equals(producer))){
                entry.getKey().setDiscount(discount,discountExpDate);
            }
        }
        return "discount was set successfully ";
    }
    public String addDiscountByCategory(String categoryName, int discount, String date){
        Date discountExpDate = getDateByString(date);
        ArrayList<String> temp = new ArrayList<String>();
        temp.add(categoryName);
        List<Category> category = data.getCategoriesByName(temp);
        for(Map.Entry<Product,List<StoreProduct>> entry : data.getProductListMap().entrySet()){
            if(entry.getKey().getCategories().contains(category.get(0)))
                entry.getKey().setDiscount(discount,discountExpDate);
        }
        return "discount was set successfully ";
    }

    public String deleteProduct(int productId) {
        Product product = data.findProductById(productId);
        data.getProductListMap().remove(product);
        data.getDefectiveProducts().remove(product);
        data.getExpiredProducts().remove(product);
        return String.format("Product with ID:%d deleted successfully.",productId);
    }


    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        if(storeId != 1)
            throw new IllegalArgumentException("Store doesn't exists.");
        this.storeId = storeId;
    }
    public String turnOffTimer(){return data.turnOffTimer();}
}
