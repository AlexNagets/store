package com.store.repository;

import com.store.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Modifying
    @Query(
        value = "UPDATE products SET available = available - :quantity WHERE id = :id",
        nativeQuery = true
    )
    int updateProductAvailability(Long id, int quantity);

    Optional<ProductEntity> findProductEntityById(Long id);
}
