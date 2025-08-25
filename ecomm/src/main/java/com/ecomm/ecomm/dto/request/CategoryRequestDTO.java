package com.ecomm.ecomm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {
    @NotBlank(message="Category name cannot be blank!!!")
    @Size(max = 255, message = "Category name cannot exceed 255 characters!!!")
    private String categoryName;
}
