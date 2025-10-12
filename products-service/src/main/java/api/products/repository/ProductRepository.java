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
    @Query("SELECT p FROM Product p WHERE LOWER(REPLACE(p.name, ' ', '')) = LOWER(REPLACE(:name, ' ', ''))")
    Optional<ProductDto> findByNameForUpdate(@Param("name") String name);

    Boolean existsByName(String name);

    Boolean existsByCategory(String category);

    ProductDto getProductByName(String name);

    List<ProductDto> getProductsByCategory(String category);
}