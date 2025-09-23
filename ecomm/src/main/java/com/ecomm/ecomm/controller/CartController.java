package com.ecomm.ecomm.controller;

import com.ecomm.ecomm.dto.response.CartResponseDTO;
import com.ecomm.ecomm.model.Cart;
import com.ecomm.ecomm.repository.CartRepository;
import com.ecomm.ecomm.service.CartService;
import com.ecomm.ecomm.util.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    private AuthUtil authUtil;
    private CartService cartService;
    private CartRepository cartRepository;
    public CartController(CartService cartService, AuthUtil authUtil, CartRepository cartRepository){
        this.cartService=cartService;
        this.authUtil=authUtil;
        this.cartRepository=cartRepository;
    }

    @PostMapping("/public/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartResponseDTO> addProductToCart(@PathVariable Long productId,
                                                            @PathVariable Integer quantity){
        CartResponseDTO cartResponseDTO = cartService.addProductToCart(productId, quantity);
        //Map<String, String> map = new HashMap<>();
        //map.put("message", "Product added to the cart successfully!!!");
        //map.put("status", "true");
        return new ResponseEntity<>(cartResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/admin/carts")
    public ResponseEntity<List<CartResponseDTO>> getAllCarts(){
        List<CartResponseDTO> cartResponseDTO = cartService.getAllCarts();
        return new ResponseEntity<>(cartResponseDTO,HttpStatus.FOUND);
    }

    @GetMapping("/public/carts/users/cart")
    public ResponseEntity<CartResponseDTO> getCartById(){
        String email = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);
        Long cartId = cart.getId();

        CartResponseDTO cartResponseDTO = cartService.getCart(email, cartId);
        return new ResponseEntity<>(cartResponseDTO, HttpStatus.OK);
    }

}
