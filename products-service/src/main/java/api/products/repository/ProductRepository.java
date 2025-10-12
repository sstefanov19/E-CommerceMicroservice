package api.products.repository;

import api.products.dto.ProductDto;
import api.products.entity.Product;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.name = :name")
    Optional<Product> findByNameForUpdate(@Param("name") String name);

    Boolean existsByName(String name);

    Boolean existsByCategory(String category);

    Product getProductByName(String name);

    List<Product> getProductsByCategory(String category);
}