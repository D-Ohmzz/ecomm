package com.ecomm.ecomm.payload;

import lombok.Data;

import java.util.List;

@Data
public class CategoryResponse{
    private List<CategoryDTO> content;
}
