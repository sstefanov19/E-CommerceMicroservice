package api.products.service;

import api.products.dto.ProductDto;
import api.products.dto.ProductRequest;
import api.products.dto.ProductResponse;
import api.products.entity.Product;
import api.products.exception.CategoryNotFoundException;
import api.products.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {


        private final ProductRepository productRepository;
        public static final String PRODUCT_CACHE = "products";


        public ProductService(ProductRepository productRepository) {
            this.productRepository = productRepository;
        }

        @CachePut(value = PRODUCT_CACHE, key = "#productRequest.name")
        @Transactional
        public ProductResponse createProduct(ProductRequest productRequest) {
                String normalizedName = normalizeForComparison(productRequest.getName());

                return productRepository.findByNameForUpdate(normalizedName)
                        .map(existing -> updateExistingProduct(existing , productRequest))
                        .orElseGet(() -> createNewProduct(productRequest));
        }


    @Cacheable(value = PRODUCT_CACHE , key = "#category")
    public List<ProductDto> getProducts(String category) {
        if(!productRepository.existsByCategory(category)) {
            throw new CategoryNotFoundException("Category must exist to retrieve data!");
        }

        return productRepository.getProductsByCategory(category);
    }


    private ProductResponse updateExistingProduct(ProductDto existing , ProductRequest request) {
        Integer newQuantity = existing.getQuantity() + request.getQuantity();

        Product updated = Product.builder()
                .id(existing.getId())
                .category(existing.getCategory())
                .name(existing.getName())
                .price(request.getPrice())
                .quantity(newQuantity)
                .build();

        productRepository.save(updated);
        return mapToResponse(updated);
    }

    private ProductResponse createNewProduct(ProductRequest request) {
        Product product = Product.builder()
                .category(request.getCategory())
                .name(request.getName())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();

        productRepository.save(product);
        return mapToResponse(product);
    }

    private String normalizeForComparison(String name) {
            return name.toLowerCase()
                    .replaceAll("\\s+" , "")
                    .replaceAll("[^a-z0-9]", "");
    }


    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getCategory().toLowerCase(),
                product.getName().toLowerCase(),
                product.getPrice(),
                product.getQuantity()
        );
    }
}
