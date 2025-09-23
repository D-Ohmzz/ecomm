package com.ecomm.ecomm.service.implementation;

import com.ecomm.ecomm.dto.mapper.CartMapper;
import com.ecomm.ecomm.dto.mapper.ProductMapper;
import com.ecomm.ecomm.dto.response.CartResponseDTO;
import com.ecomm.ecomm.dto.response.ProductCartResponseDTO;
import com.ecomm.ecomm.exceptions.APIException;
import com.ecomm.ecomm.exceptions.ResourceNotFoundException;
import com.ecomm.ecomm.model.Cart;
import com.ecomm.ecomm.model.CartItem;
import com.ecomm.ecomm.model.Product;
import com.ecomm.ecomm.repository.CartItemRepository;
import com.ecomm.ecomm.repository.CartRepository;
import com.ecomm.ecomm.repository.ProductRepository;
import com.ecomm.ecomm.service.CartService;
import com.ecomm.ecomm.util.AuthUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImplementation implements CartService {
    private CartRepository cartRepository;
    private AuthUtil authUtil;
    private ProductRepository productRepository;
    private CartItemRepository cartItemRepository;
    private ProductMapper productMapper;
    private CartMapper cartMapper;
    public CartServiceImplementation(CartRepository cartRepository, AuthUtil authUtil, ProductRepository productRepository, CartItemRepository cartItemRepository, ProductMapper productMapper, CartMapper cartMapper){
        this.cartRepository=cartRepository;
        this.authUtil=authUtil;
        this.productRepository=productRepository;
        this.cartItemRepository=cartItemRepository;
        this.productMapper=productMapper;
        this.cartMapper=cartMapper;
    }
    @Override
    public CartResponseDTO addProductToCart(Long productId, Integer quantity) {
        // Find existing cart or create a new one
        Cart cart = createCart();

        // Retrieve product details
        if(!productRepository.existsById(productId)){
            throw new ResourceNotFoundException("Product", "id", productId);
        }
        Product product = productRepository.getReferenceById(productId);

        // Perform validations(See if stock exists, check if the product id that was supplied exists in the database)
            // Check if a cart item exists
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getId(),product.getId());
        if (cartItem != null){
            throw new APIException(product.getProductName()+" already exists in the cart!!!");
        }
            // Check if the product is in stock
        if(product.getQuantity() == 0){
            throw new APIException(product.getProductName()+" is out of stock!!!");
        }
            // Check if the quantity requested is greater than the available stock
        if(product.getQuantity() < quantity){
            throw new APIException("Sorry only "+product.getQuantity()+" items are available for purchase!!!");
        }

        // Create a Cart Item
        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        // Save Cart Item
        cartItemRepository.save(newCartItem);

        // N/B:- We will only reduce the quantity of products after an order is placed

        // Updating cart total price
        cart.setTotalPrice(cart.getTotalPrice() + (quantity * product.getSpecialPrice()));

        // Save Cart
        cartRepository.save(cart);

        // Getting a list of cart items from cart
        List<CartItem> cartItems = cart.getCartItems();

        // Creating a list of productcartresponsedtos to attach to the cartresponsedto
        Stream<ProductCartResponseDTO> productCartResponseDTOStream = cartItems.stream().map(item ->{
                    Product updatedProduct = item.getProduct();
                    updatedProduct.setQuantity(item.getQuantity());
                    return productMapper.convertToProductCartResponseDTO(updatedProduct);
                });

        // Return Updated Cart
        return cartMapper.convertToCartResponseDTO(cart, productCartResponseDTOStream.toList());
    }

    @Override
    public List<CartResponseDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(!(carts.isEmpty())){
            return carts.stream()
                    .map(cart -> {
                        List<ProductCartResponseDTO> products = cart.getCartItems().stream()
                                .map(item -> productMapper.convertToProductCartResponseDTO(item.getProduct()))
                                .collect(Collectors.toList());
                         return cartMapper.convertToCartResponseDTO(cart, products);
                    }).collect(Collectors.toList());
        }
        else{
            throw new APIException("No carts have been created !!!");
        }
    }

    @Override
    public CartResponseDTO getCart(String email, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(email, cartId);
        if(cart != null){
            cart.getCartItems().forEach(c ->c.getProduct().setQuantity(c.getQuantity()));
            List<ProductCartResponseDTO> products = cart.getCartItems().stream()
                    .map(item -> productMapper.convertToProductCartResponseDTO(item.getProduct()))
                    .collect(Collectors.toList());
            return cartMapper.convertToCartResponseDTO(cart, products);
        }
        else{
            throw new APIException("No carts found associated with the user !!!");
        }
    }

    //Function that checks if the user has a cart and if not creates one
    public Cart createCart(){
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart != null){
            return userCart;
        }
        Cart cart  =  new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        return cartRepository.save(cart);
    }
}
