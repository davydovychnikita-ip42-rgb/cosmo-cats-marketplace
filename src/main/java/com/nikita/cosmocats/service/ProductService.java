package com.nikita.cosmocats.service;

import com.nikita.cosmocats.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final Map<String, Product> productDatabase = new ConcurrentHashMap<>();

    public Product createProduct(Product product) {
        productDatabase.put(product.getId(), product);
        return product;
    }

    public List<Product> getAllProducts(int page, int size) {
        return productDatabase.values().stream()
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }
    
    public long count() { 
        return productDatabase.size(); 
    }
}