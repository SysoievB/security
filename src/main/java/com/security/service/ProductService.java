package com.security.service;

import com.security.entity.Product;
import com.security.repository.ProductRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo repo;

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public Product saveProduct(Product product) {
        return repo.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product product) {
        var productForUpdate = repo.findById(id).orElseThrow(NoSuchElementException::new);

        productForUpdate.setName(product.getName());
        productForUpdate.setPrice(product.getPrice());

        if (productForUpdate.getCustomer()==null
                || !productForUpdate.getCustomer().equals(product.getCustomer())) {
            productForUpdate.setCustomer(product.getCustomer());
        }

        return productForUpdate;
    }

    @Transactional
    public boolean deleteById(Long id) {
        return repo.deleteProductById(id) == 1;
    }
}
