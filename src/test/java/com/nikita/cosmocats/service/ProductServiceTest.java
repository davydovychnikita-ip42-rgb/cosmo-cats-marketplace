package com.nikita.cosmocats.service;

import com.nikita.cosmocats.domain.Product;
import com.nikita.cosmocats.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProductShouldSaveAndReturnProduct() {
        Product product = new Product("Star Catnip", BigDecimal.valueOf(19.99), null);
        when(productRepository.save(product)).thenReturn(product);

        Product saved = productService.createProduct(product);

        assertThat(saved.getName()).isEqualTo("Star Catnip");
        verify(productRepository).save(product);
    }

    @Test
    void getAllProductsShouldReturnPagedResults() {
        Product product = new Product("Galaxy Toy", BigDecimal.valueOf(9.99), null);
        when(productRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(product)));

        List<Product> result = productService.getAllProducts(0, 10);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Galaxy Toy");
    }

    @Test
    void countShouldReturnRepositoryCount() {
        when(productRepository.count()).thenReturn(5L);

        assertThat(productService.count()).isEqualTo(5L);
    }

    @Test
    void findByIdShouldReturnProduct() {
        Product product = new Product("Comet Laser", BigDecimal.valueOf(15.00), null);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThat(productService.findById(1L)).isPresent();
    }

    @Test
    void deleteByIdShouldCallRepository() {
        productService.deleteById(1L);

        verify(productRepository).deleteById(1L);
    }
}
