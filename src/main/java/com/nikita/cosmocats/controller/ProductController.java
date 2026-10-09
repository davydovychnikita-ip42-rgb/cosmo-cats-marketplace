package com.nikita.cosmocats.controller;

import com.nikita.cosmocats.domain.Product;
import com.nikita.cosmocats.dto.PagedResponse;
import com.nikita.cosmocats.dto.ProductCreateRequest;
import com.nikita.cosmocats.dto.ProductResponse;
import com.nikita.cosmocats.mapper.ProductMapper;
import com.nikita.cosmocats.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        Product domain = productMapper.toDomain(request);
        Product saved = productService.createProduct(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toDto(saved));
    }

    @GetMapping
    public PagedResponse<ProductResponse> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        List<ProductResponse> content = productService.getAllProducts(page, size).stream()
                .map(productMapper::toDto)
                .toList();
                
        return new PagedResponse<>(content, page, size, productService.count());
    }
}