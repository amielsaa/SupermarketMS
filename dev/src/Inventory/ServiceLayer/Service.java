package Inventory.ServiceLayer;

import Inventory.BuisnessLayer.Controller.DataController;
import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.BuisnessLayer.Objects.StoreProduct;
import Inventory.ServiceLayer.Objects.Pair;
import Inventory.ServiceLayer.Objects.ProductSL;
import Inventory.ServiceLayer.Objects.Report;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Service {

    private ProductService productService;
    private ReportService reportService;
    private DataController data;

    public Service() {
        this.data = new DataController();
        this.productService = new ProductService(data);
        this.reportService = new ReportService(data);
//        addProducts();
    }

    // product service

    public Response<Integer> SelectStore(int storeId) {
        reportService.setStoreId(storeId);
        return productService.SelectStore(storeId);
    }


    public Response<List<ProductSL>> GetAllProducts() {
        return productService.GetAllProducts();
    }

    public Response<Map<Product,List<StoreProduct>>> GetAllProductsMap() {
        return productService.GetAllProductsMap();
    }

    public Response<List<Category>> GetCategories(String categories) {
        return productService.GetCategories(categories);
    }


        public Response<String> AddProduct(String name, String producer, double buyingPrice,double sellingPrice,int minquantity, String categories) {
        return productService.AddProduct(name,producer,buyingPrice,sellingPrice,minquantity,categories);
    }

    public Response<String> AddStoreProduct(int id, int quantityInStore, int quantityInWarehouse, String expDate, String locations) {
        return productService.AddStoreProduct(id,quantityInStore,quantityInWarehouse,expDate,locations);
    }

    public Response<Category> AddCategory(String category) {
        return productService.AddCategory(category);
    }

    public Response<String> ChangeCategory(int productId, int categoryIndex, String newCategory) {
        return productService.ChangeCategory(productId,categoryIndex,newCategory);
    }


    public Response<String> AddDefectiveProduct(int productId) {
        return reportService.AddDefectiveProduct(productId,GetStoreId().getData());
    }

    public Response<String> DeleteProduct(int productId) {
        return productService.DeleteProduct(productId);
    }

    public Response<String> AddDiscountByName(int productId, int discount,String date) {
        return productService.AddDiscountByName(productId,discount,date);
    }

    public Response<String> AddDiscountByCategory(String categoryName, int discount, String date) {
        return productService.AddDiscountByCategory(categoryName,discount,date);
    }

    public Response<Integer> GetStoreId() {
        return productService.GetStoreId();
    }


    //report service

    public Response<Report> ReportByExpired() {
        return reportService.ReportByExpired(GetAllProductsMap().getData());
    }

    public Response<Report> ReportByDefective() {
        return reportService.ReportByDefective(GetAllProductsMap().getData());
    }

    public Response<Report> ReportStockByCategory(List<String> categories) {
        return reportService.ReportStockByCategories(GetAllProductsMap().getData(),GetCategories(String.join(",",categories)).getData());
    }

    public Response<Report> ReportMinQuantity() {
        return reportService.ReportMinQuantity(GetAllProductsMap().getData());
    }

        public Response<String> stopTimer() {return productService.StopTimer();}

    //integration between suppliers

    public Response<String> MakeOrderMinQuantity() {
        return MakeOrderToSuppliers(reportService.MakeOrderMinQuantity(GetAllProductsMap().getData()).getData());
    }

    private Response<String> MakeOrderToSuppliers(Map<Pair<String,String>,Integer> orders) {
        throw new NotImplementedException();
    }


    private void addProducts() {
        AddProduct("Shampoo","Kef",10.20,12.50,10 ,"Wash,Shampoo,Size");
        AddProduct("Chips","Osem",7.20,10.50,10,"Snacks,Salty,Weight");
        AddProduct("Cini Minis","Telma",25,32,10,"Cereal,Sweets,Weight");
        AddProduct("Milk","Tnuva",7.50,10,10,"Diary,Milk,Size");
        AddProduct("Cottage","Tnuva",4.50,7.90,10,"Diary,Delicacy,ML");
        AddProduct("Coffee","Turkey",6.50,11,10,"Hot Drink,Coffee,Weight");
        AddProduct("Banana","Perot",5,6,10,"Fruits,Sweets,Weight");
        AddProduct("Apple","Perot",4,5,10,"Fruits,Sweets,Weight");
    }





}
