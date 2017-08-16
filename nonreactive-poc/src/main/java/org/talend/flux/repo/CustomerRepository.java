package org.talend.flux.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

    @Autowired
    private MongoCustomerRepository mongoCustomerRepository;

    @Autowired
    private MongoTemplate template;

    public void deleteAll() {
        mongoCustomerRepository.deleteAll();
    }

    public void insertAll(List<Customer> customers) {
        template.insertAll(customers);
    }

    public List<Customer> findAll() {
        return mongoCustomerRepository.findAll();
    }

    public Optional<Customer> findByFirstName(String firstName) {
        return mongoCustomerRepository.findByFirstName(firstName);
    }

    public List<Customer> findByLastName(String lastName) {
        return mongoCustomerRepository.findByLastName(lastName);
    }

    public void insert(Customer customer) {
        mongoCustomerRepository.insert(customer);
    }
}
