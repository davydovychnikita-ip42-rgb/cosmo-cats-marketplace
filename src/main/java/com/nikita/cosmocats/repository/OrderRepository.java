package com.nikita.cosmocats.repository;

import com.nikita.cosmocats.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    @Query("""
            SELECT p.name AS productName, SUM(oi.quantity) AS totalQuantitySold
            FROM OrderItem oi
            JOIN oi.product p
            WHERE oi.quantity > 0
            GROUP BY p.id, p.name
            ORDER BY SUM(oi.quantity) DESC
            """)
    List<ProductSalesProjection> findMostFrequentlyBoughtProducts();
}
