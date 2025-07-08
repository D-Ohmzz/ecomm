package com.ecomm.ecomm.service;

import com.ecomm.ecomm.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    void createCategory(Category category);
    String deleteCategory(Long id);
    void updateCategory(Category category, Long id);
}
