package api.products.service;

import api.products.dto.ProductDto;
import api.products.dto.ProductRequest;
import api.products.dto.ProductResponse;
import api.products.entity.Product;
import api.products.exception.CategoryNotFoundException;
import api.products.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {


    @Mock
    private  ProductRepository productRepository;

    @InjectMocks
    private  ProductService productService;


    @Test
    public void ProductService_CreateProduct_ShouldReturnProductDto() {
        Product product1 = Product.builder().category("Electronics").name("Nintendo Switch").price(new BigDecimal(400)).quantity(3).build();

        ProductRequest productRequest = ProductRequest.builder().category("Electronics").name("Nintendo Switch").price(new BigDecimal(400)).quantity(3).build();

        when(productRepository.findByNameForUpdate("nintendoswitch"))
                .thenReturn(Optional.empty());
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product1);

        ProductResponse savedProduct = productService.createProduct(productRequest);

        Assertions.assertNotNull(savedProduct);
        Assertions.assertEquals("electronics" , savedProduct.category());
        Assertions.assertEquals("nintendo switch" , savedProduct.name());
        Assertions.assertEquals(new BigDecimal(400) , savedProduct.price());
        Assertions.assertEquals(3 , savedProduct.quantity());
    }

    @Test
    public void ProductService_GetProducts_ShouldReturnProductsDto() {

        String category = "Electronics";
        Product product1 = Product.builder().category("Electronics").name("Nintendo Switch").price(new BigDecimal(400)).quantity(3).build();
        Product product2 = Product.builder().category("Electronics").name("Playstation 5").price(new BigDecimal(450)).quantity(2).build();


        List<Product> products = Arrays.asList(product1 , product2);

        when(productRepository.existsByCategory(category)).thenReturn(true);
        when(productRepository.getProductsByCategory(category)).thenReturn(products);

        List<ProductDto> result = productService.getProducts(category);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2 , result.size());


    }

    @Test
    public void ProductService_GetProducts_ShouldReturnInvalidCategory() {
        String category = "Clothes";

        when(productRepository.existsByCategory(category)).thenReturn(false);


        Assertions.assertThrows(CategoryNotFoundException.class , () -> {
            productService.getProducts(category);
        });
    }

}
