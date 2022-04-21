package ServiceLayer;


import Inventory.BuisnessLayer.Controller.DataController;
import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.ServiceLayer.ProductService;
import Inventory.ServiceLayer.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ServiceTest {

    private static ProductService productService;

    @BeforeAll
    static void beforeAll() {
        productService = new ProductService(new DataController());
        productService.SelectStore(1);
        List<String> categoryList = Arrays.asList("Wash","Shampoo","Size");
        productService.AddProduct("Shampoo","Kef",10.99,15.99,"Wash,Shampoo,Size");
    }

    @Test
    void selectStore() {
        Response<Integer> res = productService.SelectStore(1);

        Assertions.assertEquals(1,res.getData());
    }

    @Test
    void addProduct() {
        List<String> categoryList = Arrays.asList("Diary","Milk","Size");
        Response<String> product = productService.AddProduct("Milk 3%","Tnuva",10.99,15.99,"Diary,Milk,Size");


        Assertions.assertEquals(" Milk 3% : Tnuva : 15.99 : 10.99 : Diary,Milk,Size",product.getData());
    }

    @Test
    void addStoreProduct() {
        Response<String> res = productService.AddStoreProduct(0,10,12,"10/10/2020","warehouse-1-2&store-1-2");

        String expected = "10 : 12 : 3/10/2020 : WAREHOUSE-1-2&STORE-1-2";

        Assertions.assertEquals(expected,res.getData());
    }

    //TODO: changing store product test

    @Test
    void addCategory() {
        Response<Category> res = productService.AddCategory("Bread Stuff");

        String expected = "Bread Stuff";

        Assertions.assertEquals(expected,res.getData().getCategoryName());
    }

    @Test
    void addDefectiveProduct() {
    }

    @Test
    void deleteProduct() {
    }

    @Test
    void addDiscountByName() {
    }

    @Test
    void addDiscountByCategory() {
    }

    @AfterAll
    static void tearDown() {

    }


}