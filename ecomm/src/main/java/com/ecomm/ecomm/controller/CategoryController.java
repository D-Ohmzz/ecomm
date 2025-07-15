package com.ecomm.ecomm.controller;

import com.ecomm.ecomm.payload.CategoryDTO;
import com.ecomm.ecomm.payload.CategoryResponse;
import com.ecomm.ecomm.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.ecomm.ecomm.model.Category;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private CategoryService categoryservice;

    public CategoryController(CategoryService categoryservice) {
        this.categoryservice = categoryservice;
    }


    //@GetMapping("/api/public/categories")
    @RequestMapping(value="/public/categories", method = RequestMethod.GET)
    public ResponseEntity<CategoryResponse> getAllCategories() {
        CategoryResponse categoryResponse =  categoryservice.getAllCategories();
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    //@PostMapping("/api/admin/category")
    @RequestMapping(value="/admin/category", method = RequestMethod.POST)
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        categoryservice.createCategory(categoryDTO);
        return new ResponseEntity<>( "Category added successfully!", HttpStatus.CREATED);
    }

    //@PutMapping("/api/admin/categories/{id}")
    @RequestMapping(value="/admin/categories/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String>updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long id){
        categoryservice.updateCategory(categoryDTO, id);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }

    //@DeleteMapping("/api/admin/categories/{id}")
    @RequestMapping(value="/admin/categories/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        String status = categoryservice.deleteCategory(id);
        //return ResponseEntity.ok(status);
        return new ResponseEntity<>(status, HttpStatus.OK);
        //return ResponseEntity.status(HttpStatus.OK).body(status);
    }
}
