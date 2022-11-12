package com.security.controller;

import com.security.entity.Customer;
import com.security.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/customer")
public class CustomerController {
    private final CustomerService service;

    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return service.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getProductById(@PathVariable Long id) {
        return service
                .getCustomerById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer createProduct(@RequestParam Customer customer) {
        return service.saveCustomer(customer);
    }

    @PutMapping("/{id}")
    public Customer updateProduct(@RequestBody Customer customer, @PathVariable Long id) {
        return service.updateCustomer(id, customer);
    }

    @DeleteMapping("/{id}")
    public boolean deleteProduct(@PathVariable Long id) {
        return service.deleteById(id);
    }
}
