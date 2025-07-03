package com.ecomm.ecomm.service;

import com.ecomm.ecomm.model.CategoryModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImplementation implements CategoryService {

    private List<CategoryModel> categories = new ArrayList<>();
    int intid;
    long id;

    @Override
    public List<CategoryModel> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(CategoryModel category) {
        //Implementing id auto generation
        if (categories.isEmpty()) {
            intid = 1;
        }else {
            intid++;
        }
        id = intid;
        category.setId(id);
        categories.add(category);

    }

    @Override
    public String deleteCategory(Long id) {
        CategoryModel category = categories.stream()
                .filter(c->c.getId().equals(id))
                .findFirst()
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));

        categories.remove(category);
        return " Category with id: "+id+" has been deleted successfully!";
        }

    }


    
