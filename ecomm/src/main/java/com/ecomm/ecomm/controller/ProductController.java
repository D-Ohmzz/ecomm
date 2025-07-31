package com.ecomm.ecomm.controller;

import com.ecomm.ecomm.config.AppConstants;
import com.ecomm.ecomm.payload.ProductDTO;
import com.ecomm.ecomm.payload.ProductResponse;
import com.ecomm.ecomm.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {
    ProductService productService;
    public ProductController(ProductService productService){
        this.productService = productService;
    }
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(name ="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false)Integer pageNumber,
                                                          @RequestParam(name ="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false)Integer pageSize,
                                                          @RequestParam(name ="sortBy", defaultValue = AppConstants.PRODUCTS_SORT_BY, required = false)String sortBy,
                                                          @RequestParam(name ="sortOrder", defaultValue = AppConstants.PRODUCTS_SORT_ORDER, required = false)String sortOrder){
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                 @RequestParam(name ="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false)Integer pageNumber,
                                                                 @RequestParam(name ="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false)Integer pageSize,
                                                                 @RequestParam(name ="sortBy", defaultValue = AppConstants.PRODUCTS_SORT_BY, required = false)String sortBy,
                                                                 @RequestParam(name ="sortOrder", defaultValue = AppConstants.PRODUCTS_SORT_ORDER, required = false)String sortOrder){
        ProductResponse productResponse = productService.getProductsByCategory(categoryId,pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                 @RequestParam(name ="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false)Integer pageNumber,
                                                                 @RequestParam(name ="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false)Integer pageSize,
                                                                 @RequestParam(name ="sortBy", defaultValue = AppConstants.PRODUCTS_SORT_BY, required = false)String sortBy,
                                                                 @RequestParam(name ="sortOrder", defaultValue = AppConstants.PRODUCTS_SORT_ORDER, required = false)String sortOrder){
        ProductResponse productResponse = productService.searchProductByKeyword(keyword,pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PostMapping("/admin/products/categories/{categoryId}/product")
    public ResponseEntity<String> createProduct(@RequestBody ProductDTO productDTO,
                                                @PathVariable Long categoryId){
        productService.createProduct(productDTO, categoryId);
        return new ResponseEntity<>( "Product added successfully!", HttpStatus.CREATED);
    }

    @PutMapping("/admin/products/{id}")
    public ResponseEntity<Void> updateProduct(@RequestBody ProductDTO productDTO,
                                                @PathVariable Long id){
        productService.updateProduct(productDTO, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return new ResponseEntity<>("Product deleted successfully!", HttpStatus.OK);
    }

    @PutMapping("/admin/products/{id}/images")
    public ResponseEntity<String> updateProductImage(@PathVariable Long id,
                                                   @RequestParam("image")MultipartFile image) throws IOException {
        productService.updateProductImage(id, image);
        return new ResponseEntity<>("Image updated successfully!", HttpStatus.OK);
    }
}
