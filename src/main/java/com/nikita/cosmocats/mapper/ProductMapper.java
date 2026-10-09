package com.nikita.cosmocats.mapper;

import com.nikita.cosmocats.domain.Product;
import com.nikita.cosmocats.dto.ProductCreateRequest;
import com.nikita.cosmocats.dto.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductResponse toDto(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public Product toDomain(ProductCreateRequest dto) {
        Product product = new Product();
        product.setName(dto.name().toUpperCase()); 
        product.setPrice(dto.price());
        return product;
    }
}