package Inventory.DataAccessLayer.Mappers;

import Inventory.BuisnessLayer.Objects.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    private List<Category> categoryCache;

    public CategoryMapper() {
        categoryCache = new ArrayList<>();
    }

    public void deleteAll() {
        categoryCache = new ArrayList<>();
    }

    public Category addCategory(Category c) {
        categoryCache.add(c);
        return c;
    }

    public List<Category> getCategoriesByName(List<String> cat) {
        List<Category> categoryList = new ArrayList<>();
        for (String it: cat) {
            categoryList.add(categoryCache.stream().filter(category-> it.equals(category.getCategoryName()))
                    .findFirst().orElse(null));
        }
        return categoryList;
    }

    public Category getCategoryByName(String categoryName) {
        return categoryCache.stream().filter(category -> category.getCategoryName().equals(categoryName)).findFirst().orElse(null);
    }

    public List<Category> getCategoryCache() {
        return categoryCache;
    }
}
