package com.ecomm.ecomm.dto.mapper;

import com.ecomm.ecomm.dto.request.CategoryRequestDTO;
import com.ecomm.ecomm.dto.response.CategoryResponseDTO;
import com.ecomm.ecomm.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {
    public CategoryResponseDTO convertToCategoryResponseDTO(List<Category> categoryEntities, Page<Category> categoryPage){
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setContent(categoryEntities);
        categoryResponseDTO.setPageNumber(categoryPage.getNumber());
        categoryResponseDTO.setPageSize(categoryPage.getSize());
        categoryResponseDTO.setTotalElements(categoryPage.getTotalElements());
        categoryResponseDTO.setTotalPages(categoryPage.getTotalPages());
        categoryResponseDTO.setLastPage(categoryPage.isLast());
        return categoryResponseDTO;
    }
    public Category convertToCategoryEntity(CategoryRequestDTO categoryRequestDTO){
        Category category = new Category();
        category.setCategoryName(categoryRequestDTO.getCategoryName().trim());
        return category;
    }
}
