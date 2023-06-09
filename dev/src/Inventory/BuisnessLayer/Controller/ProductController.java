package Inventory.BuisnessLayer.Controller;

import Inventory.BuisnessLayer.Objects.*;
import Inventory.DataAccessLayer.DAO.ProductDAO;
import Inventory.DataAccessLayer.DAO.StoreProductDAO;
import misc.Pair;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.spi.CalendarDataProvider;
import java.util.stream.Collectors;

public class ProductController {

    private int productId;
    private int storeId;

    private ProductDAO productDAO;
    private StoreProductDAO storeProductDAO;

    public ProductController() {
        this.productId = 0;
        this.productDAO = new ProductDAO("Products");
        this.storeProductDAO = new StoreProductDAO("StoreProducts");
        //this.loadProductId();
        //addProducts();

    }

    public void loadProducts() {
        productDAO.SelectAll();
        storeProductDAO.SelectAll();
        loadProductId();
    }

    public void loadProductId() {
        this.productId = productDAO.SelectMaxId();
    }


    /**
     *
     * @return
     */
    public Map<Product,List<StoreProduct>> getAllProducts() {
        Map<Product,List<StoreProduct>> products = new HashMap<>();
        Map<Integer, List<StoreProduct>> storeProductMap = storeProductDAO.SelectAll();
        List<Product> productList = productDAO.SelectAll();
        for(Product p : productList) {
            if(storeProductMap.containsKey(p.getId()))
                products.put(p,storeProductMap.get(p.getId()));
            else
                products.put(p,Arrays.asList(new StoreProduct(storeId,0,0,null,null)));
        }
        return products;
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


    public Product addProduct(String name, String producer, double buyingPrice,double sellingPrice,int minquantity, List<Category> categories) {
        //TODO: capitalize all categories names
        return productDAO.InsertProduct(++productId,name,producer,buyingPrice,sellingPrice,0,"Unknown",categories,minquantity);
    }


    /**
     * @param id
     * @param quantityInStore
     * @param quantityInWarehouse
     * @param expDate
     * @param locations
     */
    public StoreProduct addStoreProduct(int id, int quantityInStore, int quantityInWarehouse, String expDate, String locations) {
        //Product currentProduct = data.findProductById(id);
        Date curExpDate = getDateByString(expDate);
        List<Location> curLocations = getLocationListByString(locations);
        return storeProductDAO.InsertStoreProduct(id,storeId,quantityInStore,quantityInWarehouse,curExpDate,curLocations);
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

    public void addPendingProducts(Map<Pair<String,String>,Pair<Double,Integer>> pendingProducts) {
        for(Map.Entry<Pair<String,String>,Pair<Double,Integer>> entry : pendingProducts.entrySet()) {
            String name = entry.getKey().getFirst();
            String producer = entry.getKey().getSecond();
            double price = entry.getValue().getFirst();
            int quantity = entry.getValue().getSecond();
            Product prod = productDAO.SelectProductByName(name,producer);
            if(prod==null) {
                List<Category> categories = new ArrayList<>();
                categories.add(new Category("Unknown"));
                addProduct(name,producer,price,price*1.25,quantity,categories);
                addStoreProduct(productId,0,quantity,expDateGenerator(),"STORE-0-0");
            } else {
                addStoreProduct(prod.getId(),0,quantity,expDateGenerator(),"STORE-0-0");
            }

        }
    }

    private String expDateGenerator() {
        return String.format("%d/%d/%d",createRandomIntBetween(1,28),createRandomIntBetween(Calendar.MONTH+1,11),2022);
    }

    private int createRandomIntBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }


    /**
     *
     * @param productId
     * @param discount
     * @param date
     * @return
     */

    //TODO: concat updates
    public String addDiscountByName(int productId, int discount, String date)
    {
        Product product = productDAO.SelectProductById(productId);
        product.setDiscount(discount,getDateByString(date));
        productDAO.Update("id",productId,"discount",discount);
        productDAO.Update("id",productId,"discountDate",date);
        productDAO.UpdateMapper(product);
        return "discount was set successfully ";
    }

    /**
     *
     * @param categoryName
     * @param discount
     * @param date
     * @return
     */

    public String addDiscountByCategory(String categoryName, int discount, String date){
        List<Product> products = productDAO.SelectAll();
        for(int i=0;i<products.size();i++) {
            Product p = products.get(i);
            Category c = p.getCategories().stream().filter(cat->cat.getCategoryName().equals(categoryName)).findFirst().orElse(null);
            if(c!=null)
                addDiscountByName(p.getId(),discount,date);
        }

        return "discount was set successfully ";
    }

    /**
     *
     * @param productId
     * @return
     */
    public String deleteProduct(int productId) {
        productDAO.DeleteProduct("id",productId);
        storeProductDAO.DeleteSP("productid",productId);
        return String.format("Product with ID:%d deleted successfully.",productId);
    }


    /**
     *
     * @param productId
     * @param categoryIndex
     * @param newCategory
     * @return
     */
    public String changeCategory(int productId, int categoryIndex, Category newCategory) {
        Product product = productDAO.SelectProductById(productId);

        product.removeCategory(categoryIndex);
        product.addCategory(newCategory,categoryIndex);
        productDAO.Update("id",productId,"categories",product.getCategories().stream().map(c->c.getCategoryName()).collect(Collectors.joining(",")));
        productDAO.UpdateMapper(product);
        return newCategory.getCategoryName();
    }

    public void setStoreId(int storeId) {
        if(storeId != 1)
            throw new IllegalArgumentException("Store doesn't exists.");
        this.storeId = storeId;
    }

    public int getStoreId() {
        return storeId;
    }

   // public String turnOffTimer(){return data.turnOffTimer();}

    public void deleteAllData(){
        storeProductDAO.deleteStoredData();
        productDAO.deleteStoredData();
    }
}
