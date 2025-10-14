package api.products.repository;

import api.products.dto.ProductDto;

import api.products.entity.Product;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;


    @Test
    public void ProductRepository_SaveAll_ReturnSavedProduct() {

        Product product = Product.builder().
                category("Clothes")
                .name("Nike Hoodie")
                .price(new BigDecimal(60))
                .quantity(10).
                build();

        Product savedProduct = productRepository.save(product);

        Assertions.assertNotNull(savedProduct);
        Assertions.assertNotNull(savedProduct.getId());
        Assertions.assertEquals("Clothes" , savedProduct.getCategory());
        Assertions.assertEquals("Nike Hoodie" , savedProduct.getName());
        Assertions.assertEquals(new BigDecimal("60") , savedProduct.getPrice());
        Assertions.assertEquals(10 , savedProduct.getQuantity());

        Optional<Product> fetched = productRepository.findById(savedProduct.getId());
        Assertions.assertTrue(fetched.isPresent());
        Assertions.assertEquals(savedProduct.getName() , fetched.get().getName());

    }

    @Test
    public void ProductRepository_GetAll_ReturnMoreThanOneTask() {

        Product product1 = Product.builder().category("Electronics").name("Nintendo Switch").price(new BigDecimal(400)).quantity(3).build();
        Product product2 = Product.builder().category("Electronics").name("Playstation 5").price(new BigDecimal(450)).quantity(2).build();

        productRepository.save(product1);
        productRepository.save(product2);


        List<Product> productList = productRepository.findAll();

        Assertions.assertNotNull(productList);
        Assertions.assertEquals(2 , productList.size());

    }

    @Test
    public void ProductRepository_GetByCategory_ReturnAllFromCategory() {

        Product product1 = Product.builder().category("Electronics").name("Nintendo Switch").price(new BigDecimal(400)).quantity(3).build();
        Product product2 = Product.builder().category("Clothes").name("Nike Hoodie").price(new BigDecimal(60)).quantity(10).build();

        productRepository.save(product1);
        productRepository.save(product2);


        List<Product> productList =  productRepository.getProductsByCategory("Clothes");


        Assertions.assertNotNull(productList);
        Assertions.assertEquals(1 , productList.size());
    }

    @Test
    public void ProductRepository_GetByName_ReturnByName() {
        Product product1 = Product.builder().category("Electronics").name("Nintendo Switch").price(new BigDecimal(400)).quantity(3).build();
        Product product2 = Product.builder().category("Clothes").name("Nike Hoodie").price(new BigDecimal(60)).quantity(10).build();

        productRepository.save(product1);
        productRepository.save(product2);

        Product productByName = productRepository.getProductByName("Nike Hoodie");

        Assertions.assertNotNull(productByName);
    }

    @Test
    public void ProductRepository_ExistsByName_ReturnTrue() {
        Product product1 = Product.builder().category("Electronics").name("Nintendo Switch").price(new BigDecimal(400)).quantity(3).build();

        productRepository.save(product1);

        Boolean exists = productRepository.existsByName("Nintendo Switch");

        Assertions.assertTrue(exists);
    }

}
