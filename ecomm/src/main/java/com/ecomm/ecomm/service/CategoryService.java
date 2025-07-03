package com.ecomm.ecomm.service;

import com.ecomm.ecomm.model.CategoryModel;
import org.springframework.context.annotation.Bean;

import java.util.List;

public interface CategoryService {
    List<CategoryModel> getAllCategories();
    void createCategory(CategoryModel category);
    String deleteCategory(Long id);
    void updateCategory(CategoryModel category, Long id);
}
