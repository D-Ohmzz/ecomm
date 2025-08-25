package com.ecomm.ecomm.controller;

import com.ecomm.ecomm.config.AppConstants;
import com.ecomm.ecomm.dto.request.ProductRequestDTO;
import com.ecomm.ecomm.dto.request.ProductUpdateRequestDTO;
import com.ecomm.ecomm.dto.response.ProductResponseDTO;
import com.ecomm.ecomm.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductController {
    ProductService productService;
    public ProductController(ProductService productService){
        this.productService = productService;
    }
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponseDTO> getAllProducts(@RequestParam(name ="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false)Integer pageNumber,
                                                             @RequestParam(name ="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false)Integer pageSize,
                                                             @RequestParam(name ="sortBy", defaultValue = AppConstants.PRODUCTS_SORT_BY, required = false)String sortBy,
                                                             @RequestParam(name ="sortOrder", defaultValue = AppConstants.PRODUCTS_SORT_ORDER, required = false)String sortOrder){
        ProductResponseDTO productResponseDTO = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponseDTO> getProductsByCategory(@PathVariable Long categoryId,
                                                                    @RequestParam(name ="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false)Integer pageNumber,
                                                                    @RequestParam(name ="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false)Integer pageSize,
                                                                    @RequestParam(name ="sortBy", defaultValue = AppConstants.PRODUCTS_SORT_BY, required = false)String sortBy,
                                                                    @RequestParam(name ="sortOrder", defaultValue = AppConstants.PRODUCTS_SORT_ORDER, required = false)String sortOrder){
        ProductResponseDTO productResponseDTO = productService.getProductsByCategory(categoryId,pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponseDTO> getProductsByKeyword(@PathVariable String keyword,
                                                                   @RequestParam(name ="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false)Integer pageNumber,
                                                                   @RequestParam(name ="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false)Integer pageSize,
                                                                   @RequestParam(name ="sortBy", defaultValue = AppConstants.PRODUCTS_SORT_BY, required = false)String sortBy,
                                                                   @RequestParam(name ="sortOrder", defaultValue = AppConstants.PRODUCTS_SORT_ORDER, required = false)String sortOrder){
        ProductResponseDTO productResponseDTO = productService.searchProductByKeyword(keyword,pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.FOUND);
    }

    @PostMapping("/admin/products/categories/{categoryId}/product")
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO,
                                                @PathVariable Long categoryId){
        productService.createProduct(productRequestDTO, categoryId);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Product added successfully!!!");
        map.put("status", "true");
        return new ResponseEntity<>( map, HttpStatus.CREATED);
    }

    @PutMapping("/admin/products/{id}")
    public ResponseEntity<Object> updateProduct(@Valid @RequestBody ProductUpdateRequestDTO productUpdateRequestDTO,
                                                @PathVariable Long id){
        productService.updateProduct(productUpdateRequestDTO, id);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Product updated successfully!!!");
        map.put("status", "true");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Product deleted successfully!!!");
        map.put("status", "true");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("/admin/products/{id}/images")
    public ResponseEntity<Object> updateProductImage(@Valid @PathVariable Long id,
                                                   @RequestParam("image")MultipartFile image) throws IOException {
        productService.updateProductImage(id, image);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Image updated successfully!!!");
        map.put("status", "true");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
