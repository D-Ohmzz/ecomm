package com.ecomm.ecomm.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ecomm.ecomm.payload.CategoryDTO;
import com.ecomm.ecomm.payload.CategoryResponse;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomm.ecomm.exceptions.APIException;
import com.ecomm.ecomm.exceptions.ResourceNotFoundException;
import com.ecomm.ecomm.model.Category;
import com.ecomm.ecomm.repository.CategoryRepository;

@Service
public class CategoryServiceImplementation implements CategoryService {

    //private List<Category> categories = new ArrayList<>();
    //int intid;
    //long id;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories() {
        List <Category> categories = categoryRepository.findAll();
        if(!(categories.isEmpty())){
            List<CategoryDTO> categoryDTOS = categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class))
                    .toList();
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setContent(categoryDTOS);
            return categoryResponse;
        }
        else{
            throw new APIException("No Categories have been created !!!");
        }
    }

    @Override
    public void createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryFromDb == null) {
            categoryRepository.save(category);
        }
        else{
            throw new APIException("Category with the name {"+category.getCategoryName()+"} already exists!!!");
        }
    }

    @Override
    public String deleteCategory(Long id) {
        /*List<Category> categories = categoryRepository.findAll();

        Category category = categories.stream()
        .filter(c->c.getId().equals(id))
        .findFirst()
        orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));*/

        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            categoryRepository.deleteById(id);
            return " Category with id: " + id + " has been deleted successfully!";
        } else {
            throw new ResourceNotFoundException("Category","categoryId",id);
        }
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO, Long id) {
        Optional<Category>optionalCategory= categoryRepository.findById(id);
        if(optionalCategory.isPresent()){
            Category category = modelMapper.map(categoryDTO, Category.class);
            category.setId(id);
            categoryRepository.save(category);
        }
        else{
            throw new ResourceNotFoundException("Category","categoryId",id);
        }

        /*List<Category> categories = categoryRepository.findAll();
          Category categoryFromDb = categories.stream()
                .filter(c->c.getId().equals(id))
                .findFirst()
               .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));

               categoryRepository.save(category);*/
    }

}


    
