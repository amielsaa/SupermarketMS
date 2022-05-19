package Inventory.PresentationLayer;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.CommandLineTable;
import Inventory.ServiceLayer.Objects.ProductSL;
import Inventory.ServiceLayer.Objects.Report;
import Inventory.ServiceLayer.Response;
import Inventory.ServiceLayer.Service;
import SupplierInventory.SIService;
import Suppliers.ServiceLayer.DummyObjects.DOrder;

import java.util.*;

public class Menu {

    private CommandLineTable clt;
    private SIService service;
    private Scanner sc;
    private boolean menu_on;

    public Menu(SIService sis) {
        this.service = sis;
        this.menu_on = true;
        this.sc = new Scanner(System.in);
        this.clt = new CommandLineTable();
        this.clt.setShowVerticalLines(true);
    }

    public void mainLoop() {
        while(menu_on) {
            printMenu();
            int selection = enterInput();
            action(selection);
        }
    }

    private void action(int selection) {
        try{
            switch (selection) {
                case 0:
                    stopProgram();
                    break;
                case 1:
                    addProductAction();
                    break;
                case 2:
                    addStoreProductAction();
                    break;
                case 3:
                    addCategoryAction();
                    break;
                case 4:
                    addDefectiveProductAction();
                    break;
                case 5:
                    getAllStoreProductsAction();
                    break;
                case 6:
                    reportByCategoriesAction();
                    break;
                case 7:
                    reportByExpiredAction();
                    break;
                case 8:
                    reportByDefectiveAction();
                    break;
                case 9:
                    reportByMinQuantity();
                    break;
                case 10:
                    makeOrderMinQuantity();
                    break;
                case 11:
                    changeCategoryAction();
                    break;
                case 12:
                    addDiscountByCategoryAction();
                    break;
                case 13:
                    addDiscountByNameAction();
                    break;
                case 14:
                    deleteProductAction();
                    break;


            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

    private void stopProgram() {
        this.menu_on = false;
        printDivider();
        System.out.println(service.stopTimer().getData());
        printDivider();
    }



    private void makeOrderMinQuantity() {
        Suppliers.ServiceLayer.Response<List<DOrder>>  res = service.MakeOrderMinQuantity();
        List<DOrder> list = res.getData();
        for(int i=0;i<list.size();i++) {
            System.out.println(list.toString());
        }
    }

    private void deleteProductAction() {
        getAllStoreProductsAction();
        printDivider();
        System.out.println("Select a product (by id) you would like to delete.");
        printDivider();
        String input = enterStringInput();
        Response<String> res = service.DeleteProduct(Integer.parseInt(input.trim()));
        if(res.isSuccess())
            System.out.println(res.getData());
        else
            System.out.println(res.getMessage());
    }


    //REPORTS
    private void reportByCategoriesAction() {
        printDivider();
        System.out.println("Enter category names.\n" +
                "Example: Salty # Shampoo # ...");
        printDivider();
        String input = enterStringInput();
        String[] inputArray = trimProductArray(input,0,false);
        Response<Report> res = service.ReportStockByCategory(Arrays.asList(inputArray));
        if(res.isSuccess()){
            System.out.println(res.getData().getHeadline());
            res.getData().getTable().print();
        }
        else
            System.out.println(res.getMessage());
    }

    private void reportByDefectiveAction() {
        Response<Report> res = service.ReportByDefective();
        if(res.isSuccess()) {
            System.out.println(res.getData().getHeadline());
            res.getData().getTable().print();
        } else
            System.out.println(res.getMessage());
    }

    private void reportByExpiredAction() {
        Response<Report> res = service.ReportByExpired();
        if(res.isSuccess()){
            System.out.println( res.getData().getHeadline());
            res.getData().getTable().print();
        }
        else
            System.out.println(res.getMessage());

    }

    private void reportByMinQuantity() {
        Response<Report> res = service.ReportMinQuantity();
        if(res.isSuccess()) {
            System.out.println(res.getData().getHeadline());
            res.getData().getTable().print();
        } else
            System.out.println(res.getMessage());
    }

    private void addDiscountByNameAction() {
        getAllStoreProductsAction();
        printDivider();
        System.out.println("Select a product to add discount to it by the following scheme:\n" +
                "product-id # discount-in-percentage # discount-exp-date\n" +
                "Example: 0 # 20 # 01/05/2022");
        printDivider();
        String input = enterStringInput();
        String[] inputArray = trimProductArray(input,3,true);
        Response<String> res = service.AddDiscountByName(Integer.parseInt(inputArray[0]),Integer.parseInt(inputArray[1]),inputArray[2]);
        if(res.isSuccess())
            System.out.println(res.getData());
        else
            System.out.println(res.getMessage());
    }

    private void addDiscountByCategoryAction() {
        printDivider();
        System.out.println("Enter a category to add discount to it by the following scheme:\n" +
                "category-name # discount-in-percentage # discount-exp-date\n" +
                "Example: Salty # 20 # 01/05/2022");
        printDivider();
        String input = enterStringInput();
        String[] inputArray = trimProductArray(input,3,true);
        Response<String> res = service.AddDiscountByCategory(inputArray[0],Integer.parseInt(inputArray[1]),inputArray[2]);
        if(res.isSuccess())
            System.out.println(res.getData());
        else
            System.out.println(res.getMessage());

    }

    private void addDefectiveProductAction() {
        getAllStoreProductsAction();
        printDivider();
        System.out.println("Select a product by id that is defective");
        printDivider();
        String input = enterStringInput();
        Response<String> res = service.AddDefectiveProduct(Integer.parseInt(input.trim()));
        if(res.isSuccess())
            System.out.println("Product reported defective successfully.");
        else
            System.out.println(res.getMessage());

    }
    
    private void changeCategoryAction() {
        getAllStoreProductsAction();
        printDivider();
        System.out.println("Select a product to change its category by the following scheme: \n" +
                "id # category-index # existing-category-name\n" +
                "category indexes: 0-Category, 1-Sub-Category, 2-SS-Category \n" +
                "Example: 0 # 1 # Salty");
        printDivider();
        String input = enterStringInput();
        String[] inputArray = trimProductArray(input,3,true);
        Response<String> res = service.ChangeCategory(Integer.parseInt(inputArray[0]),Integer.parseInt(inputArray[1]),inputArray[2]);
        if(res.isSuccess())
            System.out.println("Product category changed successfully.");
        else
            System.out.println(res.getMessage());
    }

    private void addCategoryAction() {
        String input = enterStringInput();
        Response<Category> res = service.AddCategory(input.trim());
        if(res.isSuccess())
            System.out.println(res.getData().getCategoryName() + " category added successfully.");
        else
            System.out.println("Failed to add new category.");
    }




    private void addStoreProductAction() {
        getAllStoreProductsAction();
        System.out.println("Select a product to add to your local store by the following scheme: \n" +
                "id # quantity-in-store # quantity-in-warehouse # exp-date # locations:[place-aisle-shelf number]\n" +
                "Example: 0 # 20 # 30 # 01/02/2022 # WAREHOUSE-1-2&STORE-1-2");
        String input = enterStringInput();
        String[] inputArray = trimProductArray(input,5,true);
        Response<String> res = service.AddStoreProduct(Integer.parseInt(inputArray[0]),Integer.parseInt(inputArray[1]),Integer.parseInt(inputArray[2]),inputArray[3],inputArray[4]);
        if(res.isSuccess())
            System.out.println("Store Product added successfully.");
        else
            System.out.println(res.getMessage());

    }

    private void getAllStoreProductsAction() {
        clt.setHeaders("Id","name","producer","buying-price","selling-price","discount","discount-expiration-date","categories","min-quantity","quantity-in-store","quantity-in-warehouse","exp-date","locations");
        Response<List<ProductSL>> res = service.GetAllProducts();
        for(ProductSL item : res.getData()) {
            String[] row = {item.getId(),item.getName(), item.getProducer(),String.valueOf(item.getBuyingPrice()),
                    String.valueOf(item.getSellingPrice()),String.valueOf(item.getDiscount()),
                    item.getDiscountExpDate(),item.getCategories(),item.getMinQuantity(),
                    item.getQuantityInStore(),item.getQuantityInWarehouse(),item.getExpDate(),item.getLocations()};
            clt.addRow(row);
        }
        clt.print();
        clt = new CommandLineTable();
        clt.setShowVerticalLines(true);
    }





    private void addProductAction() {
        printDivider();
        System.out.println("Adding a product should be as the following scheme: \n" +
                "product-name # producer # buying-price # selling-price # min-quantity #categories-[...] \n" +
                "Example: Cottage 5% # Tnuva # 10.90 # 15.90 # 20 # Diary,Milk,Size\n");
        printDivider();
        String input = enterStringInput();
        String[] inputArray = trimProductArray(input,6,true);
        Response<String> res = service.AddProduct(inputArray[0],inputArray[1],Double.parseDouble(inputArray[2]),Double.parseDouble(inputArray[3]),Integer.parseInt(inputArray[4]),inputArray[5]);
        if(res.isSuccess())
            System.out.println(res.getData());
        else
            System.out.println(res.getMessage());

    }

    private String[] trimProductArray(String input, int expectedLength, boolean flag) {
        String[] inputArray = input.split("#");
        for(int i=0;i<inputArray.length;i++)
            inputArray[i] = inputArray[i].trim();
        if(inputArray.length!= expectedLength && flag)
            throw new IllegalArgumentException("\nCommand missing arguments, try again.");
        return inputArray;
    }

    private void printDivider(){
        System.out.println("[--------------------------------------------]");
    }

    private void printMenu() {
        System.out.println(
                "1-Add Product                         <--->   6-Report By Categories              <--->   11-Change Category\n" +
                "2-Add/Update Store Product            <--->   7-Report By Expired Products        <--->   12-Add Discount By Category\n" +
                "3-Add Category                        <--->   8-Report By Defective Products      <--->   13-Add Discount To Product\n" +
                "4-Add Defective Product               <--->   9-Report By Shortage Products       <--->   14-Delete Product\n" +
                "5-Print All Store Products            <--->   10-Make Order Of Minimum Quantity   <--->   15-Not implemented\n" +
                "0-Exit");
    }

    private String enterStringInput() {
        System.out.print("Enter: ");
        sc.nextLine();
        return sc.nextLine();
    }


    private int enterInput() {
        System.out.print("Enter number: ");
        return sc.nextInt();
    }

}
