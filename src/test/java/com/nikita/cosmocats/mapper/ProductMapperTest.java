package com.nikita.cosmocats.mapper;

import com.nikita.cosmocats.domain.Product;
import com.nikita.cosmocats.dto.ProductCreateRequest;
import com.nikita.cosmocats.dto.ProductResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private final ProductMapper mapper = new ProductMapper();

    @Test
    void toDomainShouldMapRequestAndUppercaseName() {
        ProductCreateRequest request = new ProductCreateRequest("space helmet", new BigDecimal("99.50"));

        Product domain = mapper.toDomain(request);

        assertNotNull(domain.getId());
        assertEquals("SPACE HELMET", domain.getName());
        assertEquals(new BigDecimal("99.50"), domain.getPrice());
    }

    @Test
    void toDtoShouldMapAllFields() {
        Product product = new Product();
        product.setId("test-id-1");
        product.setName("COMET LASER");
        product.setPrice(new BigDecimal("45.00"));

        ProductResponse response = mapper.toDto(product);

        assertEquals("test-id-1", response.id());
        assertEquals("COMET LASER", response.name());
        assertEquals(new BigDecimal("45.00"), response.price());
    }
}