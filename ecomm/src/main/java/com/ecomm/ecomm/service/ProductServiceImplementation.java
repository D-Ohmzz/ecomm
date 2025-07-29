package com.ecomm.ecomm.service;

import com.ecomm.ecomm.exceptions.APIException;
import com.ecomm.ecomm.exceptions.ResourceNotFoundException;
import com.ecomm.ecomm.model.Category;
import com.ecomm.ecomm.model.Product;
import com.ecomm.ecomm.payload.CategoryDTO;
import com.ecomm.ecomm.payload.CategoryResponse;
import com.ecomm.ecomm.payload.ProductDTO;
import com.ecomm.ecomm.payload.ProductResponse;
import com.ecomm.ecomm.repository.CategoryRepository;
import com.ecomm.ecomm.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImplementation implements  ProductService{
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    public ProductServiceImplementation(CategoryRepository categoryRepository, ProductRepository productRepository, ModelMapper modelMapper){
        this.categoryRepository=categoryRepository;
        this.productRepository=productRepository;
        this.modelMapper=modelMapper;
    }
    @Override
    public void createProduct(ProductDTO productDTO, Long categoryId) {
        if(categoryRepository.existsById(categoryId)){
            Category category = categoryRepository.getReferenceById(categoryId);
            Product product = modelMapper.map(productDTO, Product.class);
            double specialPrice = productDTO.getPrice()*
                    ((100 - productDTO.getDiscount())/100);
            product.setCategory(category);
            product.setImage("default.png");
            product.setSpecialPrice(specialPrice);
            productRepository.save(product);
        }
        else{
            throw new ResourceNotFoundException("Category", "categoryId", categoryId);
        }

        
    }

    @Override
    public void updateProduct(ProductDTO productDTO, Long productId) {

    }

    @Override
    public String deleteProduct(ProductDTO productDTO, Long productId) {
        return "Product deleted successfully";
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);
        List<Product> products = productPage.getContent();

        if(!(products.isEmpty())){
            List<ProductDTO> productDTOS = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();
            ProductResponse productResponse = new ProductResponse();
            productResponse.setContent(productDTOS);
            productResponse.setPageNumber(productPage.getNumber());
            productResponse.setPageSize(productPage.getSize());
            productResponse.setTotalElements(productPage.getTotalElements());
            productResponse.setTotalPages(productPage.getTotalPages());
            productResponse.setLastPage(productPage.isLast());
            return productResponse;
        }
        else{
            throw new APIException("No products have been created !!!");
        }
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        if (!(categoryRepository.existsById(categoryId))) {
            throw new ResourceNotFoundException("Category", "categoryId", categoryId);
        }
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Category category = categoryRepository.getReferenceById(categoryId);
        Page<Product> productPage = productRepository.findByCategory(category, pageDetails);
        List<Product> products = productPage.getContent();
        if(!(products.isEmpty())){
            List<ProductDTO> productDTOS = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();
            ProductResponse productResponse = new ProductResponse();
            productResponse.setContent(productDTOS);
            productResponse.setPageNumber(productPage.getNumber());
            productResponse.setPageSize(productPage.getSize());
            productResponse.setTotalElements(productPage.getTotalElements());
            productResponse.setTotalPages(productPage.getTotalPages());
            productResponse.setLastPage(productPage.isLast());
            return productResponse;
        }
        else{
            throw new APIException("No products have been created for{"+category.getCategoryName()+"} category !!!");
        }

    }

}
