package Inventory.BuisnessLayer.Controller;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;

import java.util.Collections;
import java.util.List;

public class CategoryController {
    private DataController data;

    public CategoryController(DataController data) {
        this.data = data;
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
        if(getCategoryByName(categoryName) != null)
            throw new IllegalArgumentException("Category already exists.");
        Category curCategory = new Category(categoryName);
        data.getCategories().add(curCategory);
        return curCategory;
    }

    //flag:true - fail if category doesn't exist, false - fail if category exist
    private Category getCategoryByName(String categoryName) {
        return data.getCategories().stream().filter(category -> category.getCategoryName().equals(categoryName)).findFirst().orElse(null);
    }

    /**
     *
     * @return
     */
    public String changeCategory(int productId, int categoryIndex, String newCategory) {
        Category curCategory = getCategoryByName(newCategory);
        if(curCategory == null)
            throw new IllegalArgumentException("Category doesn't exists");
        Product product = data.findProductById(productId);
        product.removeCategory(categoryIndex);
        product.addCategory(curCategory,categoryIndex);
        return newCategory;
    }





}
