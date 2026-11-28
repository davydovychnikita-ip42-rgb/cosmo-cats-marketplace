package com.nikita.cosmocats.service;

import com.nikita.cosmocats.domain.Order;
import com.nikita.cosmocats.repository.OrderRepository;
import com.nikita.cosmocats.repository.ProductSalesProjection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    @Transactional(readOnly = true)
    public List<ProductSalesProjection> getMostFrequentlyBoughtProducts() {
        return orderRepository.findMostFrequentlyBoughtProducts();
    }

    @Transactional
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}
