package com.ecomm.ecomm.dto.response;

import java.util.List;

public record CartItemResponseDTO (
        Long cartItemId,
        List<CartResponseDTO> carts,
        List<ProductCartResponseDTO> products,
        Integer quantity,
        Double discount,
        Double productPrice
){}
