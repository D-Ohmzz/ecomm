package com.ecomm.ecomm.repository;

import com.ecomm.ecomm.model.AppRole;
import com.ecomm.ecomm.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
