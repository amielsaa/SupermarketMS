package Inventory.BuisnessLayer.Objects;

import java.util.Objects;

public class Category {

    private String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return getCategoryName().equals(category.getCategoryName());
    }




}
