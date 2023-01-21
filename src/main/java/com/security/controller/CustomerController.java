package com.security.controller;

import com.security.entity.Customer;
import com.security.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        var listCustomers = service.getAllCustomers();

        if (listCustomers.isEmpty()) {
            return ResponseEntity.noContent().build();//204
        }

        return ResponseEntity.ok(listCustomers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        var searchedCustomer = service
                .getCustomerById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (searchedCustomer == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(searchedCustomer);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Customer> createCustomer(@RequestParam Customer customer) {
        if (customer == null) {
            return ResponseEntity.badRequest().build();
        }

        service.saveCustomer(customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, @PathVariable Long id) {
        if (id == null || customer == null) {
            return ResponseEntity.badRequest().build();
        }

        service.updateCustomer(id, customer);

        return ResponseEntity.accepted().body(customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        return service.deleteById(id)
                ? ResponseEntity.accepted().build()
                : ResponseEntity.notFound().build();
    }
}
