package com.ecomm.ecomm.dto.response;

import java.util.ArrayList;
import java.util.List;

public class CartResponseDTO {
    private Long id;
    private Double totalPrice = 0.0;
    private List<ProductResponseDTO> products = new ArrayList<>();
}
