package InventoryModule;


import Inventory.BuisnessLayer.Objects.Category;
import Inventory.DataAccessLayer.DAO.CategoryDAO;
import Inventory.DataAccessLayer.DAO.ProductDAO;
import Inventory.DataAccessLayer.DAO.ReportDAO;
import Inventory.ServiceLayer.ProductService;
import Inventory.ServiceLayer.ReportService;
import Inventory.ServiceLayer.Response;

import Inventory.ServiceLayer.Service;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceTest {
    private static Service service;

    @Before
    public void SetUp() {
        service = new Service();
        service.InsertData();
        service.SelectStore(1);
    }

    @After
    public void afterEach() {

    }

    @Test
    public void selectStore() {
        Response<Integer> res = service.SelectStore(1);

        assertEquals(1, res.getData().intValue());
    }

    @Test
    public void addProduct() {

        Response<String> product = service.AddProduct("Milk 1%","Tnuva",11.99,16.99,10,"Diary,Milk,Size");


        assertEquals("Milk 1% : Tnuva : 16.99 : 11.99 : Diary,Milk,Size",product.getData());
    }

    @Test
    public void addStoreProduct() {
        Response<String> res = service.AddStoreProduct(5,10,12,"10/10/2020","warehouse-1-2&store-1-2");

        String expected = "10 : 12 : 10/10/2020 : WAREHOUSE-1-2&STORE-1-2";

        assertEquals(expected,res.getData());
    }

    @Test
    public void overrideStoreProduct() {

        Response<String> res1 = service.AddStoreProduct(5,20,20,"10/10/2020","warehouse-1-2&store-1-2");
        Response<String> res2 = service.AddStoreProduct(5,30,50,"10/10/2020","warehouse-1-2&store-1-2");

        String expected = "30 : 50 : 10/10/2020 : WAREHOUSE-1-2&STORE-1-2";

        assertEquals(expected,res2.getData());

    }

    @Test
    public void addCategory() {
        Response<Category> res = service.AddCategory("Bread Stuff");

        String expected = "Bread Stuff";

        assertEquals(expected,res.getData().getCategoryName());
    }

    @Test
    public void changeCategory() {
        Response<String> res = service.ChangeCategory(2,1,"Salty");

        String expected = "Salty";

        assertEquals(expected,res.getData());
    }

    @Test
    public void addDefectiveProduct() {
        Response<String> res = service.AddDefectiveProduct(1);

        String expected = "Product with ID:1 added successfully";

        assertEquals(expected,res.getData());
    }

    @Test
    public void deleteProduct() {
        Response<String> res = service.DeleteProduct(1);

        String expected = "Product with ID:1 deleted successfully.";

        assertEquals(expected,res.getData());


    }

    @Test
    public void addDiscountByName() {
        Response<String> res = service.AddDiscountByName(2,3,"10/6/2022");
        String expected ="discount was set successfully ";
        assertEquals(expected,res.getData());
    }

    @Test
    public void addDiscountByCategory() {
        Response<String> res = service.AddDiscountByCategory("Diary", 3, "10/6/2022");
        String expected ="discount was set successfully ";
        assertEquals(expected,res.getData());
    }

    @AfterClass
    public static void tearDown() {
        CategoryDAO categoryDAO = new CategoryDAO("Category");
        categoryDAO.Delete("name","Bread Stuff");
        ReportDAO reportDAO = new ReportDAO("Reports");
        reportDAO.Delete("productid",1);
        ProductDAO productDAO = new ProductDAO("Products");
        productDAO.DeleteProduct("id",9);
    }


}