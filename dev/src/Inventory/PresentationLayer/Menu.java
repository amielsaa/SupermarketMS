package Inventory.PresentationLayer;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.ServiceLayer.Objects.ProductSL;
import Inventory.ServiceLayer.Objects.Report;
import Inventory.ServiceLayer.Response;
import Inventory.ServiceLayer.Service;

import java.util.*;

public class Menu {

    private CommandLineTable clt;
    private Service service;
    private Scanner sc;
    private boolean menu_on;

    public Menu(Service service) {
        this.service = service;
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
                    getAllStoreProductsAction();
                    break;
                case 5:
                    reportByExpiredAction();
                    break;
                case 6:
                    changeCategoryAction();
                    break;
                case 7:
                    addDefectiveProductAction();
                    break;
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
        
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
        String[] inputArray = trimProductArray(input,3);
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

    private void reportByExpiredAction() {
        Response<Report> res = service.ReportByExpired();
        if(res.isSuccess())
            res.getData().getTable().print();
        else
            System.out.println(res.getMessage());
    }

    private void addStoreProductAction() {
        getAllStoreProductsAction();
        System.out.println("Select a product to add to your local store by the following scheme: \n" +
                "id # quantity-in-store # quantity-in-warehouse # exp-date # locations:[place-aisle-shelf number]\n" +
                "Example: 0 # 20 # 30 # 01/02/2022 # WAREHOUSE-1-2&STORE-1-2");
        String input = enterStringInput();
        String[] inputArray = trimProductArray(input,5);
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
                "product-name # producer # buying-price # selling-price # categories-[...] \n" +
                "Example: Cottage 5% # Tnuva # 10.90 # 15.90 # Diary,Milk,Size\n");
        printDivider();
        String input = enterStringInput();
        String[] inputArray = trimProductArray(input,5);
        Response<String> res = service.AddProduct(inputArray[0],inputArray[1],Double.parseDouble(inputArray[2]),Double.parseDouble(inputArray[3]),inputArray[4]);
        if(res.isSuccess())
            System.out.println(res.getData());
        else
            System.out.println(res.getMessage());

    }

    private String[] trimProductArray(String input, int expectedLength) {
        String[] inputArray = input.split("#");
        for(int i=0;i<inputArray.length;i++)
            inputArray[i] = inputArray[i].trim();
        if(inputArray.length!= expectedLength)
            throw new IllegalArgumentException("\nCommand missing arguments, try again.");
        return inputArray;
    }

    private void printDivider(){
        System.out.println("[--------------------------------------------]");
    }

    private void printMenu() {
        System.out.println("1-Add Product\n" +
                "2-Add Store Product\n" +
                "3-Add Category\n" +
                "4-Print All Store Products\n" +
                "5-Report By Expired Products\n" +
                "6-Change Category\n" +
                "7-Add Defective Product");
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
