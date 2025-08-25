package com.ecomm.ecomm.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequestDTO {
    @NotBlank(message="Product name cannot be blank!!!")
    @Size(max = 255, message = "Product name cannot exceed 255 characters!!!")
    private String productName;

    private String image;

    @NotBlank(message="Description cannot be blank!!!")
    @Size(max = 255, message = "Description cannot exceed 255 characters!!!")
    private String description;

    @NotBlank(message = "Amount cannot be null!!!")
    @Positive(message = "Amount must be positive!!!")
    @Digits(integer = 10, fraction = 0, message = "Quantity must be a whole number with upto 10 integers!!")
    private Integer quantity;

    @NotBlank(message = "Price cannot be null!!!")
    @Positive(message = "Price must be positive!!!")
    @Digits(integer = 10, fraction = 2, message = "Price must be a whole number with upto 10 integers and 2 decimal places!!!")
    private double price;

    @NotBlank(message = "Discount cannot be null!!!")
    @Positive(message = "Price must be positive!!!")
    @Digits(integer = 10, fraction = 2, message = "Discount must be a whole number with upto 10 integers and 2 decimal places!!!")
    private double discount;

    @NotBlank(message = "Discount cannot be null!!!")
    @Positive(message = "Price must be positive!!!")
    @Digits(integer = 10, fraction = 2, message = "Special price must be a whole number with upto 10 integers and 2 decimal places!!!")
    private double specialPrice;

    @NotBlank(message = "Category id cannot be null!!!")
    private long categoryId;
}
