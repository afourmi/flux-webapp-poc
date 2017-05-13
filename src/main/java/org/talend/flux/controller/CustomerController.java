package org.talend.flux.controller;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.talend.flux.repo.Customer;
import org.talend.flux.repo.MongoCustomerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CustomerController {

    @Autowired
    private MongoCustomerRepository repository;

    /**
     * See
     * https://spring.io/blog/2017/02/23/spring-framework-5-0-m5-update
     * 
     */
    @GetMapping(path = "/customer")
    public Flux<Customer> list() {
        System.out.println("get customers");
        return repository.findAll();
    }

    @PostMapping("/customer")
    public Mono<Void> create(@RequestBody Publisher<Customer> customerStream) {
        return this.repository.saveAll(customerStream).then();
    }

}