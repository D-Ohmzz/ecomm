package com.ecomm.ecomm.controller;

import com.ecomm.ecomm.dto.response.CartResponseDTO;
import com.ecomm.ecomm.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CartController {
    private CartService cartService;
    public CartController(CartService cartService){
        this.cartService=cartService;
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

}
