package com.ecomm.ecomm.repository;

import com.ecomm.ecomm.model.Category;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByCategoryName(@NotBlank String categoryName);
}
