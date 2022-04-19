package Inventory.BuisnessLayer.Controller;

import Inventory.BuisnessLayer.Objects.Category;

public class CategoryController {
    private DataController data;

    public CategoryController(DataController data) {
        this.data = data;
    }

    /**
     *
     * @param categoryName
     * @return
     */

    public Category addCategory(String categoryName) {
        checkForCategoryExistance(categoryName);
        Category curCategory = new Category(categoryName);
        data.getCategories().add(curCategory);
        return curCategory;
    }

    private void checkForCategoryExistance(String categoryName) {
        if(data.getCategories().stream().filter(category -> category.getCategoryName().equals(categoryName)).findFirst().orElse(null) != null)
            throw new IllegalArgumentException("Category already exists.");
    }


}
