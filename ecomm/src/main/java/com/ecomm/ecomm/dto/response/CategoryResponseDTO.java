package com.ecomm.ecomm.dto.response;

import com.ecomm.ecomm.model.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponseDTO {
    private List<Category> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
