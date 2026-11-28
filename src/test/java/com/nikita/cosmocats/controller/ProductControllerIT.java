package com.nikita.cosmocats.controller;

import com.nikita.cosmocats.TestcontainersConfiguration;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikita.cosmocats.domain.Product;
import com.nikita.cosmocats.dto.ProductCreateRequest;
import com.nikita.cosmocats.exception.GlobalExceptionHandler;
import com.nikita.cosmocats.mapper.ProductMapper;
import com.nikita.cosmocats.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import({ProductMapper.class, GlobalExceptionHandler.class})
class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void createProductShouldReturnCreatedWhenRequestIsValid() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest("Star Catnip", new BigDecimal("25.50"));
        Product saved = new Product();
        saved.setId(1L);
        saved.setName("STAR CATNIP");
        saved.setPrice(new BigDecimal("25.50"));

        when(productService.createProduct(any(Product.class))).thenReturn(saved);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("STAR CATNIP"))
                .andExpect(jsonPath("$.price").value(25.50));
    }

    @Test
    void getProductsShouldReturnPagedResponse() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("COMET BED");
        product.setPrice(new BigDecimal("120.00"));

        when(productService.getAllProducts(0, 10)).thenReturn(List.of(product));
        when(productService.count()).thenReturn(1L);

        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("1"))
                .andExpect(jsonPath("$.content[0].name").value("COMET BED"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void createProductShouldReturnBadRequestWhenCosmicWordIsMissing() throws Exception {
        ProductCreateRequest invalidRequest = new ProductCreateRequest("Regular Milk", new BigDecimal("10.00"));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.instance").value("/api/products"))
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    void createProductShouldReturnBadRequestWhenNameIsTooShort() throws Exception {
        ProductCreateRequest invalidRequest = new ProductCreateRequest("St", new BigDecimal("10.00"));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createProductShouldReturnBadRequestWhenPriceIsNegative() throws Exception {
        ProductCreateRequest invalidRequest = new ProductCreateRequest("Galaxy Whiskers", new BigDecimal("-5.00"));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createProductShouldReturnBadRequestWhenFieldsAreNull() throws Exception {
        ProductCreateRequest invalidRequest = new ProductCreateRequest(null, null);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}