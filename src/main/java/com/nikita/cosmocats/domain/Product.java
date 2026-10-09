package com.nikita.cosmocats.domain;

import java.util.UUID;
import java.math.BigDecimal;

public class Product {
    private String id = UUID.randomUUID().toString();
    private String name;
    private BigDecimal price; 

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}