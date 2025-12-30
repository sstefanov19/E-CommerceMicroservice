package org.example.orderservice.controller;

import org.example.orderservice.dto.ProductResponse;
import org.example.orderservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("products/")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService  = productService;
    }


    @GetMapping("{param}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String param) {
        ProductResponse response;

        if (isNumeric(param)) {
            Long id = Long.valueOf(param);
            response = productService.getProduct(id);
        } else {
            response = productService.getProductByName(param);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
