package com.nikita.cosmocats.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DomainEntitiesTest {

    @Test
    void shouldAccessAllEntityFields() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Cosmic Food");
        assertThat(category.getId()).isEqualTo(1L);
        assertThat(category.getName()).isEqualTo("Cosmic Food");

        Product product = new Product();
        product.setId(2L);
        product.setName("Star Tuna");
        product.setPrice(BigDecimal.TEN);
        product.setCategory(category);
        assertThat(product.getCategory()).isEqualTo(category);

        Order order = new Order();
        LocalDateTime now = LocalDateTime.now();
        order.setId(3L);
        order.setOrderNumber("ORD-100");
        order.setCustomerEmail("test@galaxy.space");
        order.setCreatedAt(now);
        assertThat(order.getId()).isEqualTo(3L);
        assertThat(order.getOrderNumber()).isEqualTo("ORD-100");
        assertThat(order.getCustomerEmail()).isEqualTo("test@galaxy.space");
        assertThat(order.getCreatedAt()).isEqualTo(now);

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(4);
        item.setPriceAtPurchase(BigDecimal.TEN);
        assertThat(item.getId()).isNull();
        assertThat(item.getOrder()).isEqualTo(order);
        assertThat(item.getProduct()).isEqualTo(product);
        assertThat(item.getQuantity()).isEqualTo(4);
        assertThat(item.getPriceAtPurchase()).isEqualTo(BigDecimal.TEN);
    }
}