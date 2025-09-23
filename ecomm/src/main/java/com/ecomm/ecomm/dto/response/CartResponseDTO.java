package com.ecomm.ecomm.dto.response;

import java.util.List;

public record CartResponseDTO (
    Long cartId,
    Double totalPrice,
    List<ProductCartResponseDTO> products
){}
