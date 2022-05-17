package Inventory.DataAccessLayer;

import Inventory.BuisnessLayer.Objects.*;
import Inventory.DataAccessLayer.DAO.CategoryDAO;
import Inventory.DataAccessLayer.DAO.ProductDAO;
import Inventory.DataAccessLayer.DAO.StoreProductDAO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestingDAL {


    public static void main(String[] args) {
        //prodprint();
        ProductDAO pdao = new ProductDAO("Products");
        pdao.Update("id",1,"categories","SomeShit");
    }

    public static void prodselectid(){
        ProductDAO pdao = new ProductDAO("Products");
        System.out.println(pdao.SelectProductById(1).toString());
    }

    public static void prodprint() {
        ProductDAO pdao = new ProductDAO("Products");
        List<Product> products = pdao.SelectAll();
        for(Product p : products)
            System.out.println(p.toString());
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
