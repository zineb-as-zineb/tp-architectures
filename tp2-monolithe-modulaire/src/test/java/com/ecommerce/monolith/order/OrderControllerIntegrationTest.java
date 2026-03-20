package com.ecommerce.monolith.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.ecommerce.monolith.category.model.Category;
import com.ecommerce.monolith.category.repository.CategoryRepository;
import com.ecommerce.monolith.customer.model.Customer;
import com.ecommerce.monolith.customer.repository.CustomerRepository;
import com.ecommerce.monolith.order.repository.OrderRepository;
import com.ecommerce.monolith.product.model.Product;
import com.ecommerce.monolith.product.repository.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
        categoryRepository.deleteAll();

        Category category = categoryRepository.save(Category.builder()
                .name("Electronics")
                .build());

        customer = customerRepository.save(Customer.builder()
                .firstName("Zineb")
                .lastName("Elb")
                .email("zineb@example.com")
                .build());

        product = productRepository.save(Product.builder()
                .name("Laptop")
                .description("Portable computer")
                .price(BigDecimal.valueOf(999.99))
                .stock(10)
                .category(category)
                .build());
    }

    @Test
    void shouldCreateOrderAndExposeCustomerHistory() throws Exception {
        String payload = """
                {
                  "customerId": %d,
                  "productId": %d,
                  "quantity": 2
                }
                """.formatted(customer.getId(), product.getId());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(customer.getId()))
                .andExpect(jsonPath("$.productId").value(product.getId()))
                .andExpect(jsonPath("$.productName").value("Laptop"))
                .andExpect(jsonPath("$.customerName").value("Zineb Elb"))
                .andExpect(jsonPath("$.totalPrice").value(1999.98));

        mockMvc.perform(get("/api/orders/customer/{customerId}", customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(customer.getId()))
                .andExpect(jsonPath("$[0].productId").value(product.getId()))
                .andExpect(jsonPath("$[0].quantity").value(2));
    }
}
