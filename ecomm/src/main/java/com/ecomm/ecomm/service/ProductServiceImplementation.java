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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public void updateProduct(ProductDTO productDTO, Long id) {
        if(productRepository.existsById(id)){
            Product product = modelMapper.map(productDTO, Product.class);
            double specialPrice = productDTO.getPrice()*
                    ((100 - productDTO.getDiscount())/100);
            product.setSpecialPrice(specialPrice);
            product.setId(id);
            productRepository.save(product);
        }
        else{
            throw new ResourceNotFoundException("Product", "id", id);
        }
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

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%', pageDetails);
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
           String path ="src/main/resources/static/images";
           String fileName = uploadImage(path, image);
           // Updating the file name to the product
           productFromDb.setImage(fileName);
           productRepository.save(productFromDb);
       }else{
           throw new ResourceNotFoundException("Product", "id", id);
       }
    }
    private String uploadImage(String path, MultipartFile image) throws IOException {
        // File names of current/original file
        String originalFileName = image.getOriginalFilename();

        // Generate a unique file name (UUID)
        // image.jpg --> 123 --> 123.jpg
        String randomId = UUID.randomUUID().toString();
        String fileName = null;
        if (originalFileName != null) {
            fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        }
        String filePath = path + File.pathSeparator + fileName;
        //Check if path exists and create
        File folder = new File(path);
        if(!(folder.exists())){
            folder.mkdir();
        }
        if (!folder.canWrite()) {
            throw new IOException("No write permissions for: " + path);
        }
        // Upload to server
        Files.copy(image.getInputStream(), Paths.get(filePath));
        return fileName;
    }
}
