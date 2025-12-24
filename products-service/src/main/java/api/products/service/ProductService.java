package api.products.service;

import api.products.dto.ProductDto;
import api.products.dto.ProductRequest;
import api.products.dto.ProductResponse;
import api.products.entity.Product;
import api.products.exception.CategoryNotFoundException;
import api.products.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
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

    @CachePut(value = PRODUCT_CACHE, key = "#result.category() + ':' + #result.name()")
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        String normalizedName = normalizeForComparison(productRequest.getName());

        return productRepository.findByNameForUpdate(normalizedName)
                .map(existing -> updateExistingProduct(existing, productRequest))
                .orElseGet(() -> createNewProduct(productRequest));
    }

    public ProductResponse getProductByName(String name) {
        Boolean exists = productRepository.existsByName(name);

        if(!exists) {
            throw new RuntimeException("Cannot return item that doesnt exist!");
        }

        Product productByName = productRepository.getProductByName(name);

        return mapToResponse(productByName);
    }


    @Cacheable(value = PRODUCT_CACHE, key = "#category")
    public List<ProductResponse> getProducts(String category) {
        if (!productRepository.existsByCategory(category)) {
            throw new CategoryNotFoundException("Category must exist to retrieve data!");
        }

        return productRepository.getProductsByCategory(category).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        Product productById = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        return mapToResponse(productById);
    }

    private ProductResponse updateExistingProduct(Product existing, ProductRequest request) {
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
                .replaceAll("\\s+", "")
                .replaceAll("[^a-z0-9]", "");
    }

    private ProductDto mapToDto(Product product) {
        return new ProductDto(product.getId(), product.getCategory(), product.getName(), product.getPrice(), product.getQuantity());
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
