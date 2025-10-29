package com.vandana.controller;

import com.vandana.dto.request.ProductRequest;
import com.vandana.dto.response.ProductResponse;
import com.vandana.entity.Product;
import com.vandana.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> responses = productService.getAllActiveProducts()
                .stream()
                .map(product -> ProductResponse.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .description(product.getDescription())
                        .status(product.getStatus().toString())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/add")
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest request) {
        Product product = productService.addProduct(request.getProductName(), request.getDescription());
        ProductResponse response = ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .status(product.getStatus().toString())
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Integer productId, @RequestBody ProductRequest request) {
        Product updated = productService.updateProduct(productId,
                request.getProductName(), request.getDescription(), "ACTIVE");
        ProductResponse response = ProductResponse.builder()
                .productId(updated.getProductId())
                .productName(updated.getProductName())
                .description(updated.getDescription())
                .status(updated.getStatus().toString())
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }
}