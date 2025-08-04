package com.ecomm.ecomm.service.implementation;

import java.util.List;

import com.ecomm.ecomm.dto.mapper.CategoryMapper;
import com.ecomm.ecomm.dto.request.CategoryRequestDTO;
import com.ecomm.ecomm.dto.response.CategoryResponseDTO;
import com.ecomm.ecomm.service.CategoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecomm.ecomm.exceptions.APIException;
import com.ecomm.ecomm.exceptions.ResourceNotFoundException;
import com.ecomm.ecomm.model.Category;
import com.ecomm.ecomm.repository.CategoryRepository;

@Service
public class CategoryServiceImplementation implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    public CategoryServiceImplementation(CategoryRepository categoryRepository, CategoryMapper categoryMapper){
        this.categoryRepository=categoryRepository;
        this.categoryMapper=categoryMapper;
    }
    @Override
    public CategoryResponseDTO getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List <Category> categories = categoryPage.getContent();
        if(!(categories.isEmpty())){
            List<Category> categoryEntities = categories.stream()
                    .toList();
            return categoryMapper.convertToCategoryResponseDTO(categoryEntities, categoryPage);
        }
        else{
            throw new APIException("No Categories have been created !!!");
        }
    }

    @Override
    public void createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryMapper.convertToCategoryEntity(categoryRequestDTO);
        if(!(categoryRepository.existsByCategoryName(category.getCategoryName()))) {
            categoryRepository.save(category);
        }
        else{
            throw new APIException("Category with the name {"+category.getCategoryName()+"} already exists!!!");
        }
    }

    @Override
    public void deleteCategory(Long id) {
        if(categoryRepository.existsById(id)){
            categoryRepository.deleteById(id);
        }
        else{
            throw new ResourceNotFoundException("Category","id",id);
        }
    }

    @Override
    public void updateCategory(CategoryRequestDTO categoryRequestDTO, Long id) {
        if (!(categoryRepository.existsById(id)))
        {
            throw new ResourceNotFoundException("Category","id",id);
        }
        if(categoryRepository.existsByCategoryName(categoryRequestDTO.getCategoryName())){
            throw new APIException("Category with the name {"+ categoryRequestDTO.getCategoryName()+"} already exists!!!");
        }
        Category category = categoryMapper.convertToCategoryEntity(categoryRequestDTO);
        category.setId(id);
        categoryRepository.save(category);
    }
}


    
