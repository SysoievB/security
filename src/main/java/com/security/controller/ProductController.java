package com.security.controller;

import com.security.entity.Product;
import com.security.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        var allProducts = service.getAllProducts();

        if (allProducts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(allProducts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        var searchedProduct = service
                .getProductById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (searchedProduct == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(searchedProduct);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> createProduct(@RequestParam Product product) {
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }

        service.saveProduct(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateCustomer(@RequestBody Product product, @PathVariable Long id) {
        if (id == null || product == null) {
            return ResponseEntity.badRequest().build();
        }

        service.updateProduct(id, product);

        return ResponseEntity.accepted().body(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        return service.deleteById(id)
                ? ResponseEntity.accepted().build()
                : ResponseEntity.notFound().build();
    }
}
