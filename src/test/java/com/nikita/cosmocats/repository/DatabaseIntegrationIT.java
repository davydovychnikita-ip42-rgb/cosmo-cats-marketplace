package com.nikita.cosmocats.repository;

import com.nikita.cosmocats.TestcontainersConfiguration;
import com.nikita.cosmocats.domain.Category;
import com.nikita.cosmocats.domain.Order;
import com.nikita.cosmocats.domain.OrderItem;
import com.nikita.cosmocats.domain.Product;
import com.nikita.cosmocats.service.CategoryService;
import com.nikita.cosmocats.service.OrderService;
import com.nikita.cosmocats.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        TestcontainersConfiguration.class,
        CategoryService.class,
        ProductService.class,
        OrderService.class
})
class DatabaseIntegrationIT {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldPerformCategoryCrud() {
        Category saved = categoryService.createCategory(new Category("Space Treats"));
        assertThat(saved.getId()).isNotNull();
        assertThat(categoryService.findById(saved.getId())).isPresent();
        assertThat(categoryService.getAllCategories()).extracting(Category::getName).contains("Space Treats");
        assertThat(categoryRepository.findByName("Space Treats")).isPresent();

        categoryService.deleteById(saved.getId());
        assertThat(categoryService.findById(saved.getId())).isEmpty();
    }

    @Test
    void shouldEnforceUniqueProductNameWithinCategory() {
        Category category = categoryService.createCategory(new Category("Cosmic Gear"));
        Product p1 = productService.createProduct(new Product("Star Helmet", BigDecimal.valueOf(50.00), category));
        assertThat(productRepository.findByCategoryId(category.getId())).hasSize(1);
        assertThat(p1.getCategory().getName()).isEqualTo("Cosmic Gear");

        Product duplicate = new Product("Star Helmet", BigDecimal.valueOf(60.00), category);
        assertThatThrownBy(() -> {
            productService.createProduct(duplicate);
            productRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldSaveOrderWithNaturalIdAndReturnMostBoughtProductsProjection() {
        Category category = categoryService.createCategory(new Category("Galactic Toys"));
        Product laser = productService.createProduct(new Product("Comet Laser", BigDecimal.valueOf(12.50), category));
        Product catnip = productService.createProduct(new Product("Star Catnip", BigDecimal.valueOf(8.00), category));

        Order order1 = new Order("ORD-COSMO-001", "cat1@galaxy.space");
        order1.addItem(new OrderItem(laser, 2, laser.getPrice()));
        order1.addItem(new OrderItem(catnip, 5, catnip.getPrice()));
        Order savedOrder1 = orderService.createOrder(order1);

        Order order2 = new Order("ORD-COSMO-002", "cat2@galaxy.space");
        order2.addItem(new OrderItem(laser, 1, laser.getPrice()));
        orderService.createOrder(order2);

        // Перевірка Natural ID
        Order foundByNaturalId = orderService.findByOrderNumber("ORD-COSMO-001").orElseThrow();
        assertThat(foundByNaturalId.getId()).isEqualTo(savedOrder1.getId());
        assertThat(foundByNaturalId.getCustomerEmail()).isEqualTo("cat1@galaxy.space");
        assertThat(foundByNaturalId.getCreatedAt()).isNotNull();
        assertThat(foundByNaturalId.getItems()).hasSize(2);
        assertThat(foundByNaturalId.getItems().get(0).getOrder()).isEqualTo(foundByNaturalId);
        assertThat(foundByNaturalId.getItems().get(0).getPriceAtPurchase()).isPositive();

        // Перевірка JPQL Projection (WHERE + ORDER BY DESC)
        List<ProductSalesProjection> report = orderService.getMostFrequentlyBoughtProducts();
        assertThat(report).hasSize(2);
        assertThat(report.get(0).getProductName()).isEqualTo("Star Catnip");
        assertThat(report.get(0).getTotalQuantitySold()).isEqualTo(5L);
        assertThat(report.get(1).getProductName()).isEqualTo("Comet Laser");
        assertThat(report.get(1).getTotalQuantitySold()).isEqualTo(3L);

        orderService.deleteById(savedOrder1.getId());
        assertThat(orderService.findByOrderNumber("ORD-COSMO-001")).isEmpty();
    }
}
