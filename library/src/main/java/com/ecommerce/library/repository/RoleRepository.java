package com.ecommerce.library.repository;

import com.ecommerce.library.model.role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<role, Long> {
    role findByName(String name);
}
