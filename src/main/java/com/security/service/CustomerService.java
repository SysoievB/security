package com.security.service;

import com.security.entity.Customer;
import com.security.repository.CustomerRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepo repo;

    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }

    public Customer getCustomerById(Long id) {
        return repo.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public Customer saveCustomer(Customer customer) {
        return repo.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long id, Customer customer) {
        var customerForUpdate = repo.findById(id).orElseThrow(NoSuchElementException::new);

        customerForUpdate.setFirstName(customer.getFirstName());
        customerForUpdate.setLastName(customer.getLastName());

        if (customerForUpdate.getProducts().equals(Collections.emptyList())
                || !customerForUpdate.getProducts().equals(customer.getProducts())) {
            customerForUpdate.setProducts(customer.getProducts());
        }

        return customerForUpdate;
    }

    @Transactional
    public boolean deleteById(Long id) {
        return repo.deleteCustomerById(id) == 1;
    }
}
