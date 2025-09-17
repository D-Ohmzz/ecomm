package com.ecomm.ecomm.repository;

import com.ecomm.ecomm.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
