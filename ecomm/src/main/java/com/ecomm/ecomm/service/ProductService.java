package com.ecomm.ecomm.service;

import com.ecomm.ecomm.payload.ProductDTO;
import com.ecomm.ecomm.payload.ProductResponse;

public interface ProductService {
    void createProduct(ProductDTO productDTO, Long categoryId);
    void updateProduct(ProductDTO productDTO, Long id);
    void deleteProduct(Long id);
    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
