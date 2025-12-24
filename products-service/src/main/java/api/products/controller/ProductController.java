package api.products.controller;


import api.products.dto.ProductRequest;
import api.products.dto.ProductResponse;
import api.products.entity.Product;
import api.products.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse response = productService.createProduct(productRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ProductResponse> getProductById(@Valid @PathVariable Long id) {
//        ProductResponse product = productService.getProductById(id);
//
//        return ResponseEntity.status(HttpStatus.OK).body(product);
//    }

    @GetMapping("/{param}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String param) {
        try {
            Long id = Long.parseLong(param);

            ProductResponse productById = productService.getProductById(id);

            return ResponseEntity.status(HttpStatus.OK).body(productById);
        }catch(NumberFormatException e) {
        ProductResponse productByName = productService.getProductByName(param);

        return ResponseEntity.status(HttpStatus.OK).body(productByName);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(@Valid @RequestParam String category) {
        List<ProductResponse> getProductsFromDb = productService.getProducts(category);

        return ResponseEntity.status(HttpStatus.OK).body(getProductsFromDb);
    }
}
