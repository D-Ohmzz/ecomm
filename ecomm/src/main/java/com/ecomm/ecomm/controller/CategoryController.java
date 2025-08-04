package com.ecomm.ecomm.controller;

import com.ecomm.ecomm.config.AppConstants;
import com.ecomm.ecomm.dto.request.CategoryRequestDTO;
import com.ecomm.ecomm.dto.response.CategoryResponseDTO;
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
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        categoryservice.createCategory(categoryRequestDTO);
        return new ResponseEntity<>( "Category added successfully!", HttpStatus.CREATED);
    }

    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<Void>updateCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO, @PathVariable Long id){
        categoryservice.updateCategory(categoryRequestDTO, id);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryservice.deleteCategory(id);
        return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
    }
}
