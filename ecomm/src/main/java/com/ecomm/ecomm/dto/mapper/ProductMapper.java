package com.ecomm.ecomm.dto.mapper;

import com.ecomm.ecomm.dto.request.ProductRequestDTO;
import com.ecomm.ecomm.dto.request.ProductUpdateRequestDTO;
import com.ecomm.ecomm.dto.response.ProductResponseDTO;
import com.ecomm.ecomm.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {
    public ProductResponseDTO convertToProductResponseDTO(List<Product> productEntities, Page<Product> productPage){
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productEntities);
        productResponseDTO.setPageNumber(productPage.getNumber());
        productResponseDTO.setPageSize(productPage.getSize());
        productResponseDTO.setTotalElements(productPage.getTotalElements());
        productResponseDTO.setTotalPages(productPage.getTotalPages());
        productResponseDTO.setLastPage(productPage.isLast());
        return productResponseDTO;
    }
    public Product convertToProductEntity(ProductRequestDTO productRequestDTO){
        Product product = new Product();
        product.setProductName(productRequestDTO.getProductName().trim());
        product.setImage(productRequestDTO.getImage());
        product.setDescription(productRequestDTO.getDescription().trim());
        product.setQuantity(productRequestDTO.getQuantity());
        product.setPrice(productRequestDTO.getPrice());
        product.setDiscount(productRequestDTO.getDiscount());
        product.setSpecialPrice(productRequestDTO.getSpecialPrice());
        return product;
    }
    public Product convertToProductEntity(ProductUpdateRequestDTO productUpdateRequestDTO){
        Product product = new Product();
        product.setProductName(productUpdateRequestDTO.getProductName().trim());
        product.setImage(productUpdateRequestDTO.getImage());
        product.setDescription(productUpdateRequestDTO.getDescription().trim());
        product.setQuantity(productUpdateRequestDTO.getQuantity());
        product.setPrice(productUpdateRequestDTO.getPrice());
        product.setDiscount(productUpdateRequestDTO.getDiscount());
        product.setSpecialPrice(productUpdateRequestDTO.getSpecialPrice());
        return product;
    }
}
