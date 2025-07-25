package com.ecomm.ecomm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productName;
    private String description;
    private Integer quantity;
    private Double price;
    private Double specialPrice;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;


}
