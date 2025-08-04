package com.ecomm.ecomm.service;

import com.ecomm.ecomm.dto.request.ProductRequestDTO;
import com.ecomm.ecomm.dto.request.ProductUpdateRequestDTO;
import com.ecomm.ecomm.dto.response.ProductResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    void createProduct(ProductRequestDTO productRequestDTO, Long categoryId);
    void updateProduct(ProductUpdateRequestDTO productUpdateRequestDTO, Long id);
    void deleteProduct(Long id);
    ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponseDTO getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponseDTO searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    void updateProductImage(Long id, MultipartFile image) throws IOException;
}
