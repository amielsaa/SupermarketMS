package Inventory.BuisnessLayer.Controller;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.DataAccessLayer.DAO.CategoryDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryController {
    private DataController data;

    private CategoryDAO categoryDAO;

    public CategoryController(DataController data) {
        this.data = data;
        this.categoryDAO = new CategoryDAO("Category");
        addCategories();
    }

    private void addCategories() {
        String[] cat = {"Diary","Wash","Milk","Size","Shampoo","Salty","Gram","Snacks","Cereal","Sweets","Weight",
        "Delicacy","Hot Drink","Coffee","Fruits","ML"};
        for(int i=0;i<cat.length;i++)
            addCategory(cat[i]);
    }


    /**
     *
     * @param categoryName
     * @return
     */

    public Category addCategory(String categoryName) {
        Category c = categoryDAO.InsertCategory(categoryName);
        if(c == null)
            throw new IllegalArgumentException("An error occurred, try again.");
        return c;
    }

    public List<Category> getCategoriesByString(String categories) {
        List<String> cat = splitCategoriesString(categories);
        List<Category> categoryList = new ArrayList<>();
        for(String s : cat)
            categoryList.add(categoryDAO.SelectCategory(s));
        return categoryList;
    }

    private List<String> splitCategoriesString(String categoryString) {
        List<String> categories = new ArrayList<>();
        String[] categoryArray = categoryString.split(",");
        for(int i=0;i<categoryArray.length;i++)
            categoryArray[i] = categoryArray[i].trim();
        Collections.addAll(categories,categoryArray);
        return categories;
    }


    //TODO: implement with dao
    public String changeCategory(int productId, int categoryIndex, String newCategory) {
        //Category curCategory = getCategoryByName(newCategory);
        Category curCategory = null;
        if(curCategory == null)
            throw new IllegalArgumentException("Category doesn't exists");
        Product product = data.findProductById(productId);
        product.removeCategory(categoryIndex);
        product.addCategory(curCategory,categoryIndex);
        return newCategory;
    }





}
