package xyz.therahul.creatorstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.therahul.creatorstore.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
