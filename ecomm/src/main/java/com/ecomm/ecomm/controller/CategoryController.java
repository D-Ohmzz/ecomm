package com.ecomm.ecomm.controller;

import com.ecomm.ecomm.config.AppConstants;
import com.ecomm.ecomm.payload.CategoryDTO;
import com.ecomm.ecomm.payload.CategoryResponse;
import com.ecomm.ecomm.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private CategoryService categoryservice;

    public CategoryController(CategoryService categoryservice) {
        this.categoryservice = categoryservice;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name ="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false)Integer pageNumber,
            @RequestParam(name ="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false)Integer pageSize,
            @RequestParam(name ="sortBy", defaultValue = AppConstants.CATEGORIES_SORT_BY, required = false)String sortBy,
            @RequestParam(name ="sortOrder", defaultValue = AppConstants.CATEGORIES_SORT_ORDER, required = false)String sortOrder
    ) {
        CategoryResponse categoryResponse =  categoryservice.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("/admin/category")
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        categoryservice.createCategory(categoryDTO);
        return new ResponseEntity<>( "Category added successfully!", HttpStatus.CREATED);
    }

    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<Void>updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long id){
        categoryservice.updateCategory(categoryDTO, id);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryservice.deleteCategory(id);
        return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
    }
}
