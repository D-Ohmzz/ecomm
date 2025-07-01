package com.ecomm.ecomm.controller;

import com.ecomm.ecomm.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;
import com.ecomm.ecomm.model.CategoryModel;

@RestController
public class CategoryController {
    private CategoryService categoryservice;
    public CategoryController(CategoryService categoryservice){
        this.categoryservice=categoryservice;
    }



    @GetMapping("/api/public/categories")
    public List<CategoryModel> getAllCategories(){
        return categoryservice.getAllCategories();
    }

    @PostMapping("/api/public/category")
    public String createCategory(@RequestBody CategoryModel category){
        categoryservice.createCategory(category);
        return "Category added successfully!";
    }
    
    @DeleteMapping("/api/admin/categories/{id}")
    public String deleteCategory(@PathVariable Long id){
       String status =  categoryservice.deleteCategory(id);
        return status;
    }



}
