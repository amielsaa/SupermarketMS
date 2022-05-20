package Inventory.DataAccessLayer.IdentityMap;

import Inventory.BuisnessLayer.Objects.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryIdentityMap {

    private List<Category> categoryIdentityMap;

    public CategoryIdentityMap() {
        categoryIdentityMap = new ArrayList<>();
    }

    public void deleteAll() {
        categoryIdentityMap = new ArrayList<>();
    }

    public Category addCategory(Category c) {
        categoryIdentityMap.add(c);
        return c;
    }

    public List<Category> getCategoriesByName(List<String> cat) {
        List<Category> categoryList = new ArrayList<>();
        for (String it: cat) {
            categoryList.add(categoryIdentityMap.stream().filter(category-> it.equals(category.getCategoryName()))
                    .findFirst().orElse(null));
        }
        return categoryList;
    }

    public Category getCategoryByName(String categoryName) {
        return categoryIdentityMap.stream().filter(category -> category.getCategoryName().equals(categoryName)).findFirst().orElse(null);
    }

    public List<Category> getCategoryIdentityMap() {
        return categoryIdentityMap;
    }
}
