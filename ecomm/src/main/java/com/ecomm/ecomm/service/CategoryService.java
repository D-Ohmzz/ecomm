package com.ecomm.ecomm.service;

import com.ecomm.ecomm.dto.request.CategoryRequestDTO;
import com.ecomm.ecomm.dto.response.CategoryResponseDTO;

public interface CategoryService {
    CategoryResponseDTO getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    void createCategory(CategoryRequestDTO categoryRequestDTO);
    void deleteCategory(Long id);
    void updateCategory(CategoryRequestDTO categoryRequestDTO, Long id);
}
