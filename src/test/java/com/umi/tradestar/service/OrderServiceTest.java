package com.umi.tradestar.service;

import com.umi.tradestar.TradestarApplication;
import com.umi.tradestar.model.Order;
import com.umi.tradestar.model.User;
import com.umi.tradestar.model.enums.Role;
import com.umi.tradestar.model.enums.OrderStatus;
import com.umi.tradestar.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.umi.tradestar.config.TestSecurityConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {TradestarApplication.class})
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class OrderServiceTest {

    @MockBean
    private OrderRepository orderRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Autowired
    private OrderService orderService;

    private User testUser;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup test user
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("Test")
                .lastName("Trader")
                .role(Role.TRADER)
                .build();

        // Setup test order
        testOrder = new Order();
        testOrder.setSymbol("AAPL");
        testOrder.setQuantity(BigDecimal.TEN);
        
        // Setup security context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testUser);
        SecurityContextHolder.setContext(securityContext);
        
        // Setup repository mock
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
    }

    @Test
    void createOrder_ValidOrder_ReturnsCreatedOrder() {
        Order result = orderService.createOrder(testOrder);
        
        assertNotNull(result);
        assertEquals(OrderStatus.NEW, result.getStatus());
        assertEquals(BigDecimal.ZERO, result.getFilledQuantity());
        assertEquals(BigDecimal.ZERO, result.getAveragePrice());
        assertEquals(testUser, result.getTrader());
        assertNotNull(result.getOrderId());
    }

    @Test
    void createOrder_InvalidQuantity_ThrowsException() {
        testOrder.setQuantity(BigDecimal.ZERO);
        
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(testOrder);
        });
    }

    @Test
    void createOrder_EmptySymbol_ThrowsException() {
        testOrder.setSymbol("");
        
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(testOrder);
        });
    }

    @Test
    public void testOrderService() {
        // Your test code here
    }
}