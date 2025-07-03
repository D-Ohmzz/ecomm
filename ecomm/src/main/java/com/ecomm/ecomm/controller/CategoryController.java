package com.ecomm.ecomm.controller;

import com.ecomm.ecomm.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.ecomm.ecomm.model.CategoryModel;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CategoryController {
    private CategoryService categoryservice;

    public CategoryController(CategoryService categoryservice) {
        this.categoryservice = categoryservice;
    }


    @GetMapping("/api/public/categories")
    public ResponseEntity<List<CategoryModel>> getAllCategories() {
        List<CategoryModel> categories =  categoryservice.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.FOUND);
    }

    @PostMapping("/api/public/category")
    public ResponseEntity<String> createCategory(@RequestBody CategoryModel category) {
        categoryservice.createCategory(category);
        return new ResponseEntity<>( "Category added successfully!", HttpStatus.CREATED);
    }

    @DeleteMapping("/api/admin/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        try {
            String status = categoryservice.deleteCategory(id);
            //return ResponseEntity.ok(status);
            return new ResponseEntity<>(status, HttpStatus.OK);
            //return ResponseEntity.status(HttpStatus.OK).body(status);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
