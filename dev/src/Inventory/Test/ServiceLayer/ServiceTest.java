package ServiceLayer;


import Inventory.BuisnessLayer.Controller.DataController;
import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.ServiceLayer.ProductService;
import Inventory.ServiceLayer.Response;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ServiceTest {

    private static ProductService productService;

    @BeforeAll
    static void beforeAll() {

    }

    @BeforeEach
    void beforeEach() {
        productService = new ProductService(new DataController());
        productService.SelectStore(1);
    }

    @AfterEach
    void afterEach() {

    }

    @Test
    void selectStore() {
        Response<Integer> res = productService.SelectStore(1);

        Assertions.assertEquals(1,res.getData());
    }

    @Test
    void addProduct() {

        Response<String> product = productService.AddProduct("Milk 3%","Tnuva",10.99,15.99,"Diary,Milk,Size");


        Assertions.assertEquals("Milk 3% : Tnuva : 15.99 : 10.99 : Diary,Milk,Size",product.getData());
    }

    @Test
    void addStoreProduct() {
        Response<String> res = productService.AddStoreProduct(0,10,12,"10/10/2020","warehouse-1-2&store-1-2");

        String expected = "10 : 12 : 10/10/2020 : WAREHOUSE-1-2&STORE-1-2";

        Assertions.assertEquals(expected,res.getData());
    }

    @Test
    void overrideStoreProduct() {
        Response<String> res1 = productService.AddStoreProduct(1,20,20,"10/10/2020","WAREHOUSE-1-2");
        Response<String> res2 = productService.AddStoreProduct(1,30,50,"10/10/2020","WAREHOUSE-1-2");

        String expected = "30 : 50 : 10/10/2020 : WAREHOUSE-1-2";

        Assertions.assertEquals(expected,res2.getData());

    }

    @Test
    void addCategory() {
        Response<Category> res = productService.AddCategory("Bread Stuff");

        String expected = "Bread Stuff";

        Assertions.assertEquals(expected,res.getData().getCategoryName());
    }

    @Test
    void changeCategory() {
        Response<String> res = productService.ChangeCategory(0,1,"Salty");

        String expected = "Salty";

        Assertions.assertEquals(expected,res.getData());
    }

    @Test
    void addDefectiveProduct() {
        Response<String> res = productService.AddDefectiveProduct(0);

        String expected = "Shampoo : Kef : 12.50 : 10.20 : Wash,Shampoo,Size";

        Assertions.assertEquals(expected,res.getData());
    }

    @Test
    void deleteProduct() {
        Response<String> res = productService.DeleteProduct(0);

        String expected = "Product with ID:0 deleted successfully.";

        Assertions.assertEquals(expected,res.getData());
    }

    @Test
    void addDiscountByName() {
        Response<String> res = productService.AddDiscountByName(0,3,"10/6/2022");
        String expected ="discount was set successfully ";
        Assertions.assertEquals(expected,res.getData());
    }

    @Test
    void addDiscountByCategory() {
        Response<String> res = productService.AddDiscountByCategory("Diary", 3, "10/6/2022");
        String expected ="discount was set successfully ";
        Assertions.assertEquals(expected,res.getData());
    }

    @AfterAll
    static void tearDown() {

    }


}