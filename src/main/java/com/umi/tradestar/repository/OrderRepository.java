package com.umi.tradestar.repository;

import com.umi.tradestar.model.Order;
import com.umi.tradestar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Order entity.
 * Provides methods for CRUD operations and custom queries for orders.
 *
 * @author VrushankPatel
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Find all orders for a specific trader.
     *
     * @param trader the user who placed the orders
     * @return list of orders for the trader
     */
    List<Order> findByTrader(User trader);

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
}