package xyz.therahul.creatorstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.therahul.creatorstore.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
