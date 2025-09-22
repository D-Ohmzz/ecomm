package com.ecomm.ecomm.service;

import com.ecomm.ecomm.dto.response.CartResponseDTO;

public interface CartService {
    public CartResponseDTO addProductToCart(Long productId, Integer quantity);
}
