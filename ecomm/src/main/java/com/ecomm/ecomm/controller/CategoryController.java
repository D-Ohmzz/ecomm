package com.ecomm.ecomm.controller;

import com.ecomm.ecomm.config.AppConstants;
import com.ecomm.ecomm.dto.request.CategoryRequestDTO;
import com.ecomm.ecomm.dto.response.CategoryResponseDTO;
import com.ecomm.ecomm.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private CategoryService categoryservice;

    public CategoryController(CategoryService categoryservice) {
        this.categoryservice = categoryservice;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponseDTO> getAllCategories(
            @RequestParam(name ="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false)Integer pageNumber,
            @RequestParam(name ="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false)Integer pageSize,
            @RequestParam(name ="sortBy", defaultValue = AppConstants.CATEGORIES_SORT_BY, required = false)String sortBy,
            @RequestParam(name ="sortOrder", defaultValue = AppConstants.CATEGORIES_SORT_ORDER, required = false)String sortOrder
    ) {
        CategoryResponseDTO categoryResponseDTO =  categoryservice.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/admin/category")
    public ResponseEntity<Object> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        categoryservice.createCategory(categoryRequestDTO);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Category added successfully!!!");
        map.put("status", "true");
        return new ResponseEntity<>( map, HttpStatus.CREATED);
    }

    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<Object>updateCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO, @PathVariable Long id){
        categoryservice.updateCategory(categoryRequestDTO, id);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Category updated successfully!!!");
        map.put("status", "true");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Category deleted successfully!!!");
        map.put("status", "true");
        categoryservice.deleteCategory(id);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
