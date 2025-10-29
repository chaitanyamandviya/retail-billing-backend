package com.vandana.service;

import com.vandana.entity.Product;
import com.vandana.exception.BadRequestException;
import com.vandana.exception.NotFoundException;
import com.vandana.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllActiveProducts() {
        return productRepository.findByStatus(Product.ProductStatus.ACTIVE);
    }

    public Product addProduct(String productName, String description) {
        if (productRepository.findByProductName(productName).isPresent()) {
            throw new BadRequestException("Product already exists!");
        }
        Product product = Product.builder()
                .productName(productName)
                .description(description)
                .status(Product.ProductStatus.ACTIVE)
                .build();
        return productRepository.save(product);
    }

    public Product updateProduct(Integer productId, String name, String desc, String status) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found!"));
        product.setProductName(name);
        product.setDescription(desc);
        product.setStatus(Product.ProductStatus.valueOf(status));
        return productRepository.save(product);
    }

    public void deleteProduct(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Product not found!");
        }
        productRepository.deleteById(productId);
    }
}