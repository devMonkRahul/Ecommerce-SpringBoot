package xyz.therahul.creatorstore.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.therahul.creatorstore.dto.OrderItemRequest;
import xyz.therahul.creatorstore.dto.OrderRequest;
import xyz.therahul.creatorstore.entities.Order;
import xyz.therahul.creatorstore.entities.OrderItem;
import xyz.therahul.creatorstore.entities.Product;
import xyz.therahul.creatorstore.repositories.OrderRepository;
import xyz.therahul.creatorstore.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // Create an order
    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        Order order = new Order();
        order.setCustomerName(orderRequest.getCustomerName());
        order.setCustomerEmail(orderRequest.getCustomerEmail());
        order.setStatus("CONFIRMED");

        for (OrderItemRequest orderItemRequest : orderRequest.getItems()) {
            Product product = productRepository.findById(
                    orderItemRequest.getProductId()
            ).orElseThrow(() -> new RuntimeException("Product not found with id " + orderItemRequest.getProductId()));

            // Check the product stock
            if (product.getStockQuantity() < orderItemRequest.getQuantity()) {
                throw new RuntimeException("Not enough stock for this order");
            }

            // Calculate total price
            BigDecimal priceOfItem = product.getPrice()
                    .multiply(BigDecimal.valueOf(orderItemRequest.getQuantity()));

            totalPrice = totalPrice.add(priceOfItem);

            // Update the product table with latest stock quantity
            product.setStockQuantity(product.getStockQuantity() - orderItemRequest.getQuantity());

            productRepository.save(product);

            // Builder pattern to make obj
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(orderItemRequest.getQuantity())
                    .priceAtPurchase(product.getPrice())
                    .build();

            orderItems.add(orderItem);
        }

        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }

    // Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get order by id
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + orderId));
    }
}
