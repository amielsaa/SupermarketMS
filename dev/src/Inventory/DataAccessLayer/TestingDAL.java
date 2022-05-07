package Inventory.DataAccessLayer;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Location;
import Inventory.BuisnessLayer.Objects.Locations;
import Inventory.BuisnessLayer.Objects.StoreProduct;
import Inventory.DataAccessLayer.DAO.CategoryDAO;
import Inventory.DataAccessLayer.DAO.ProductDAO;
import Inventory.DataAccessLayer.DAO.StoreProductDAO;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestingDAL {


    public static void main(String[] args) {
        storeprod();
    }

    public static void storeprod() {
        StoreProductDAO spdao = new StoreProductDAO("StoreProducts");
        //spdao.InsertStoreProduct(1,1,1,1, Date.valueOf("2022-01-01"),Arrays.asList(new Location(Locations.STORE,1,1)));

        Map<Integer,List<StoreProduct>> mapsp = spdao.SelectAll();
        for(Map.Entry<Integer,List<StoreProduct>> entry : mapsp.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue().get(0).toString());
        }
    }

    public static void catandprod() {
        ProductDAO pdao = new ProductDAO("Products");
        CategoryDAO cdao = new CategoryDAO("Category");
        List<Category> categoryList = Arrays.asList(new Category("Milk"),new Category("Good"),new Category("Stuff"));
        pdao.InsertProduct(1,"milk","tnuva",10.50,12.50,0,"", categoryList,0);
        cdao.InsertCategory("somestuff");
        //System.out.println(cdao.SelectCategory("somestuff").getCategoryName());
    }

}
