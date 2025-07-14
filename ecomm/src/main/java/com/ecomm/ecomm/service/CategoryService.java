package com.ecomm.ecomm.service;

import com.ecomm.ecomm.model.Category;
import com.ecomm.ecomm.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategories();
    void createCategory(Category category);
    String deleteCategory(Long id);
    void updateCategory(Category category, Long id);
}
