package com.ecomm.ecomm.dto.mapper;

import com.ecomm.ecomm.dto.response.CartItemResponseDTO;
import com.ecomm.ecomm.dto.response.CartResponseDTO;
import com.ecomm.ecomm.dto.response.ProductCartResponseDTO;
import com.ecomm.ecomm.model.CartItem;

import java.util.List;

public class CartItemMapper {
    public CartItemResponseDTO convertToCartResponseDTO (CartItem cartItem, List<CartResponseDTO> cartResponseDTOS, List<ProductCartResponseDTO> productCartResponseDTOs){
        return new CartItemResponseDTO(cartItem.getId(), cartResponseDTOS, productCartResponseDTOs,cartItem.getQuantity(), cartItem.getDiscount(), cartItem.getProductPrice());
    }
}
