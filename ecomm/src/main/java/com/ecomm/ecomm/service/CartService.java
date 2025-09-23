package com.ecomm.ecomm.service;

import com.ecomm.ecomm.dto.response.CartResponseDTO;

import java.util.List;

public interface CartService {
    CartResponseDTO addProductToCart(Long productId, Integer quantity);
    List<CartResponseDTO> getAllCarts();
}
