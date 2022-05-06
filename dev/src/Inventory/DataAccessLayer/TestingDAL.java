package Inventory.DataAccessLayer;

import java.sql.*;
import java.util.Arrays;

public class TestingDAL {


    public static void main(String[] args) {
        ProductDAO pdao = new ProductDAO("Products");
        CategoryDAO cdao = new CategoryDAO("Category");
        pdao.insert(1,"milk","tnuva",10.50,12.50,0,"", Arrays.asList("Milk,Good,Stuff"),0);
        cdao.insert("somestuff");
    }

}
