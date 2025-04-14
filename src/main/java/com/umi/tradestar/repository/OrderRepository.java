package com.umi.tradestar.repository;

import com.umi.tradestar.model.Order;
import com.umi.tradestar.model.User;
import com.umi.tradestar.model.enums.OrderSide;
import com.umi.tradestar.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Order entity.
 * Provides methods for CRUD operations and custom queries for orders.
 *
 * @author VrushankPatel
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    
    /**
     * Find all orders for a specific trader.
     *
     * @param trader the user who placed the orders
     * @return list of orders for the trader
     */
    List<Order> findByUser(User user);

    /**
     * Find order by its unique client order ID.
     *
     * @param clientOrderId the client order ID
     * @return the order if found
     */
    Optional<Order> findByClientOrderId(String clientOrderId);

    /**
     * Find order by its unique client order ID.
     *
     * @param orderId the client order ID
     * @return the order if found
     */
    Order findByOrderId(String orderId);

    /**
     * Find all orders for a specific symbol.
     *
     * @param symbol the trading symbol
     * @return list of orders for the symbol
     */
    List<Order> findBySymbol(String symbol);

    /**
     * Find all orders with a specific status.
     *
     * @param status the order status
     * @return list of orders with the specified status
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Find all orders with a specific side.
     *
     * @param side the order side
     * @return list of orders with the specified side
     */
    List<Order> findBySide(OrderSide side);
}