package com.ecomm.ecomm.service;

import com.ecomm.ecomm.payload.CategoryDTO;
import com.ecomm.ecomm.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    void createCategory(CategoryDTO categoryDTO);
    void deleteCategory(Long id);
    void updateCategory(CategoryDTO categoryDTO, Long id);
}
