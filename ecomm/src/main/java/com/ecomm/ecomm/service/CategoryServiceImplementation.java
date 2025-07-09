package com.ecomm.ecomm.service;

import com.ecomm.ecomm.model.Category;
import com.ecomm.ecomm.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImplementation implements CategoryService {

    //private List<Category> categories = new ArrayList<>();
    //int intid;
    //long id;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long id) {
        /*List<Category> categories = categoryRepository.findAll();

        Category category = categories.stream()
        .filter(c->c.getId().equals(id))
        .findFirst()
        orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"))*/
        ;
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            categoryRepository.deleteById(id);
            return " Category with id: " + id + " has been deleted successfully!";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found");
        }
    }

    @Override
    public void updateCategory(Category category, Long id) {
        Optional<Category>optionalCategory= categoryRepository.findById(id);
        if(optionalCategory.isPresent()){
            category.setId(id);
            categoryRepository.save(category);
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found");
        }

        /*List<Category> categories = categoryRepository.findAll();
          Category categoryFromDb = categories.stream()
                .filter(c->c.getId().equals(id))
                .findFirst()
               .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));

               categoryRepository.save(category);*/
    }

}


    
