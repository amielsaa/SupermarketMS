package ServiceLayer;


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
        productService = new ProductService();
    }

    @Test
    void addProduct() {
        List<String> categoryList = Arrays.asList("Diary","Milk","Size");
        Response<String> product = productService.AddProduct("Milk 3%","Tnuva",10,15,categoryList);



        Assertions.assertEquals(" Milk 3% : Tnuva : 15 : 10 : [Diary,Milk,Size]",product.getData());
    }

    @Test
    void addCategory() {
    }

    @Test
    void deleteCategory() {
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