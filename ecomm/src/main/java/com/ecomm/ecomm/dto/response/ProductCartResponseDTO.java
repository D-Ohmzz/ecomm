package com.ecomm.ecomm.dto.response;

public record ProductCartResponseDTO(
        Long id,
        String productName,
        String image,
        String description,
        Integer quantity,
        Double price,
        Double discount,
        Double specialPrice
){}
