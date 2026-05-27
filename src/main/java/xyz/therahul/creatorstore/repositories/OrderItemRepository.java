package xyz.therahul.creatorstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.therahul.creatorstore.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
