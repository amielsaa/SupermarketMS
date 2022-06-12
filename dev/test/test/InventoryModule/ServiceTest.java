package InventoryModule;


import Inventory.BuisnessLayer.Objects.Category;
import Inventory.DataAccessLayer.DAO.CategoryDAO;
import Inventory.DataAccessLayer.DAO.ReportDAO;
import Inventory.ServiceLayer.ProductService;
import Inventory.ServiceLayer.ReportService;
import Inventory.ServiceLayer.Response;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

class ServiceTest {

    private static ProductService productService;
    private static ReportService reportService;

    @Before
    void beforeEach() {
        productService = new ProductService();
        productService.SelectStore(1);
        reportService = new ReportService();
    }

    @After
    void afterEach() {

    }

    @Test
    void selectStore() {
        Response<Integer> res = productService.SelectStore(1);

        assertEquals(1, res.getData().intValue());
    }

    @Test
    void addProduct() {

        Response<String> product = productService.AddProduct("Milk 3%","Tnuva",10.99,15.99,10,"Diary,Milk,Size");


        assertEquals("Milk 3% : Tnuva : 15.99 : 10.99 : Diary,Milk,Size",product.getData());
    }

    @Test
    void addStoreProduct() {
        Response<String> res = productService.AddStoreProduct(5,10,12,"10/10/2020","warehouse-1-2&store-1-2");

        String expected = "10 : 12 : 10/10/2020 : WAREHOUSE-1-2&STORE-1-2";

        assertEquals(expected,res.getData());
    }

    @Test
    void overrideStoreProduct() {
        Response<String> res1 = productService.AddStoreProduct(6,20,20,"10/10/2020","WAREHOUSE-1-2");
        Response<String> res2 = productService.AddStoreProduct(6,30,50,"10/10/2020","WAREHOUSE-1-2");

        String expected = "30 : 50 : 10/10/2020 : WAREHOUSE-1-2";

        assertEquals(expected,res2.getData());

    }

    @Test
    void addCategory() {
        Response<Category> res = productService.AddCategory("Bread Stuff");

        String expected = "Bread Stuff";

        assertEquals(expected,res.getData().getCategoryName());
    }

    @Test
    void changeCategory() {
        Response<String> res = productService.ChangeCategory(2,1,"Salty");

        String expected = "Salty";

        assertEquals(expected,res.getData());
    }

    @Test
    void addDefectiveProduct() {
        Response<String> res = reportService.AddDefectiveProduct(1,1);

        String expected = "Product with ID:1 added successfully";

        assertEquals(expected,res.getData());
    }

    @Test
    void deleteProduct() {
        Response<String> res = productService.DeleteProduct(1);

        String expected = "Product with ID:1 deleted successfully.";

        assertEquals(expected,res.getData());


    }

    @Test
    void addDiscountByName() {
        Response<String> res = productService.AddDiscountByName(2,3,"10/6/2022");
        String expected ="discount was set successfully ";
        assertEquals(expected,res.getData());
    }

    @Test
    void addDiscountByCategory() {
        Response<String> res = productService.AddDiscountByCategory("Diary", 3, "10/6/2022");
        String expected ="discount was set successfully ";
        assertEquals(expected,res.getData());
    }

    @AfterClass
    static void tearDown() {
        CategoryDAO categoryDAO = new CategoryDAO("Category");
        categoryDAO.Delete("name","Bread Stuff");
        ReportDAO reportDAO = new ReportDAO("Reports");
        reportDAO.Delete("productid",1);
    }


}