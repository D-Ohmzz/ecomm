package com.ecomm.ecomm.service.implementation;

import com.ecomm.ecomm.dto.mapper.ProductMapper;
import com.ecomm.ecomm.dto.request.ProductUpdateRequestDTO;
import com.ecomm.ecomm.exceptions.APIException;
import com.ecomm.ecomm.exceptions.ResourceNotFoundException;
import com.ecomm.ecomm.model.Category;
import com.ecomm.ecomm.model.Product;
import com.ecomm.ecomm.dto.request.ProductRequestDTO;
import com.ecomm.ecomm.dto.response.ProductResponseDTO;
import com.ecomm.ecomm.repository.CategoryRepository;
import com.ecomm.ecomm.repository.ProductRepository;
import com.ecomm.ecomm.service.FileService;
import com.ecomm.ecomm.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImplementation implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final FileService fileService;
    @Value("${project.image}")
    private String path;
    public ProductServiceImplementation(CategoryRepository categoryRepository, ProductRepository productRepository, ProductMapper productMapper, FileService fileService){
        this.categoryRepository=categoryRepository;
        this.productRepository=productRepository;
        this.productMapper=productMapper;
        this.fileService=fileService;
    }
    @Override
    public void createProduct(ProductRequestDTO productRequestDTO, Long categoryId) {
        if(categoryRepository.existsById(categoryId)){
            Category category = categoryRepository.getReferenceById(categoryId);
            Product product = productMapper.convertToProductEntity(productRequestDTO);
            double specialPrice = productRequestDTO.getPrice()*
                    ((100 - productRequestDTO.getDiscount())/100);
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
    public void updateProduct(ProductUpdateRequestDTO productUpdateRequestDTO, Long id) {
        Long categoryId = productUpdateRequestDTO.getCategoryId();
        if(!productRepository.existsById(id)){
            throw new ResourceNotFoundException("Product", "id", id);
        }
        if(!categoryRepository.existsById(categoryId)){
            throw new ResourceNotFoundException("Category", "id", productUpdateRequestDTO.getCategoryId());
        }
        Category category = categoryRepository.getReferenceById(categoryId);
        Product product = productMapper.convertToProductEntity(productUpdateRequestDTO);
        double specialPrice = productUpdateRequestDTO.getPrice()*
                ((100 - productUpdateRequestDTO.getDiscount())/100);
        product.setCategory(category);
        product.setSpecialPrice(specialPrice);
        product.setId(id);
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if(productRepository.existsById(id)){
            productRepository.deleteById(id);
        }
        else{
            throw new ResourceNotFoundException("Product", "id", id);
        }
    }

    @Override
    public ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);
        List<Product> products = productPage.getContent();

        if(!(products.isEmpty())){
            List<Product> productEntities = products.stream()
                    .toList();
            return productMapper.convertToProductResponseDTO(productEntities, productPage);
        }
        else{
            throw new APIException("No products have been created !!!");
        }
    }

    @Override
    public ProductResponseDTO getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
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
            List<Product> productEntities = products.stream()
                    .toList();
            return productMapper.convertToProductResponseDTO(productEntities, productPage);
        }
        else{
            throw new APIException("No products have been created for{"+category.getCategoryName()+"} category !!!");
        }
    }

    @Override
    public ProductResponseDTO searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%', pageDetails);
        List<Product> products = productPage.getContent();
        if(!(products.isEmpty())){
            List<Product> productEntities = products.stream()
                    .toList();
            return productMapper.convertToProductResponseDTO(productEntities, productPage);
        }
        else{
            throw new ResourceNotFoundException("Product", "keyword", keyword);
        }
    }

    @Override
    public void updateProductImage(Long id, MultipartFile image) throws IOException {
        // Get the product from the DB
       boolean productExists = productRepository.existsById(id);
       if(productExists){
           Product productFromDb = productRepository.getReferenceById(id);
           // Upload image to server-/images directory
           // Get the file name of the uploaded image
           String fileName = fileService.uploadImage(path, image);
           // Updating the file name to the product
           productFromDb.setImage(fileName);
           productRepository.save(productFromDb);
       }else{
           throw new ResourceNotFoundException("Product", "id", id);
       }
    }
}
