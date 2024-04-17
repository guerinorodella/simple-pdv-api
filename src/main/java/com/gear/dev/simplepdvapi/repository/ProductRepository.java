package com.gear.dev.simplepdvapi.repository;

import com.gear.dev.simplepdvapi.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
}
