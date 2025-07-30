package com.ecomm.ecomm.service;

import java.util.List;
import java.util.Optional;

import com.ecomm.ecomm.payload.CategoryDTO;
import com.ecomm.ecomm.payload.CategoryResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List <Category> categories = categoryPage.getContent();
        if(!(categories.isEmpty())){
            List<CategoryDTO> categoryDTOS = categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class))
                    .toList();
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setContent(categoryDTOS);
            categoryResponse.setPageNumber(categoryPage.getNumber());
            categoryResponse.setPageSize(categoryPage.getSize());
            categoryResponse.setTotalElements(categoryPage.getTotalElements());
            categoryResponse.setTotalPages(categoryPage.getTotalPages());
            categoryResponse.setLastPage(categoryPage.isLast());
            return categoryResponse;
        }
        else{
            throw new APIException("No Categories have been created !!!");
        }
    }

    @Override
    public void createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        if(!(categoryRepository.existsByCategoryName(category.getCategoryName().trim()))) {
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
        throw new ResourceNotFoundException("Category","id",id);
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO, Long id) {
        if (!(categoryRepository.existsById(id)))
        {
            throw new ResourceNotFoundException("Category","id",id);
        }
        if(categoryRepository.existsByCategoryName(categoryDTO.getCategoryName().trim())){
            throw new APIException("Category with the name {"+categoryDTO.getCategoryName()+"} already exists!!!");
        }
        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setId(id);
        categoryRepository.save(category);
    }
}


    
