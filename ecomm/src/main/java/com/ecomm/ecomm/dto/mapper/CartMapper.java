package com.ecomm.ecomm.dto.mapper;

import com.ecomm.ecomm.dto.response.CartResponseDTO;
import com.ecomm.ecomm.dto.response.ProductCartResponseDTO;
import com.ecomm.ecomm.model.Cart;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {
    public CartResponseDTO convertToCartResponseDTO(Cart cart, List<ProductCartResponseDTO> productCartResponseDTOs){
        return new CartResponseDTO(cart.getId(), cart.getTotalPrice(), productCartResponseDTOs);
    }
}
