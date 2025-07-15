package com.ecomm.ecomm.service;

import com.ecomm.ecomm.payload.CategoryDTO;
import com.ecomm.ecomm.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories();
    void createCategory(CategoryDTO categoryDTO);
    String deleteCategory(Long id);
    void updateCategory(CategoryDTO categoryDTO, Long id);
}
