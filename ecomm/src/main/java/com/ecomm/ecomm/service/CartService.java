package com.ecomm.ecomm.service;

import com.ecomm.ecomm.dto.response.CartResponseDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {
    CartResponseDTO addProductToCart(Long productId, Integer quantity);
    List<CartResponseDTO> getAllCarts();
    CartResponseDTO getCart(String email, Long cartId);
    @Transactional
    CartResponseDTO updateProductQuantityInCart(Long productId, Integer quantity);
    @Transactional
    void deleteProductFromCart(Long cartId, Long productId);
    void updateProductInCart(Long cartId, Long productId);
}
