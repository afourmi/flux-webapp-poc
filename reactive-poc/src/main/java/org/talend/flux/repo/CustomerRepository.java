package org.talend.flux.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CustomerRepository {

    @Autowired
    private MongoCustomerRepository mongoCustomerRepository;

    @Autowired
    private ReactiveMongoTemplate template;

    public void deleteAll() {
        mongoCustomerRepository.deleteAll();
    }

    public Flux<Customer> insertAll(List<Customer> customers) {
        return template.insertAll(customers);
    }

    public Flux<Customer> findAll() {
        return mongoCustomerRepository.findAll();
    }

    public Mono<Customer> findByFirstName(String firstName) {
        return mongoCustomerRepository.findByFirstName(firstName);
    }

    public Flux<Customer> findByLastName(String lastName) {
        return mongoCustomerRepository.findByLastName(lastName);
    }

    public Mono<Customer> insert(Customer customer) {
        return mongoCustomerRepository.insert(customer);
    }
}
