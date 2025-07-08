package com.ecomm.ecomm.service;

import com.ecomm.ecomm.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImplementation implements CategoryService {

    private List<Category> categories = new ArrayList<>();
    int intid;
    long id;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
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
        Category category = categories.stream()
                .filter(c->c.getId().equals(id))
                .findFirst()
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));

        categories.remove(category);
        return " Category with id: "+id+" has been deleted successfully!";
        }

    @Override
    public void updateCategory(Category category, Long id) {
        Category categoryFromArrayList = categories.stream()
                .filter(c->c.getId().equals(id))
                .findFirst()
               .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));

        int index = categories.indexOf(categoryFromArrayList);
        category.setId(id);
        categories.set(index, category);
    }

}


    
