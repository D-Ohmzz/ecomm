package com.ecomm.ecomm.dto.response;

import java.util.List;

public record CartResponseDTO (
    Long id,
    Double totalPrice,
    List<ProductCartResponseDTO> products
){}
