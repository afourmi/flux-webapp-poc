package org.talend.flux.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import org.talend.flux.repo.Customer;
import org.talend.flux.repo.CustomerRepository;
import org.talend.flux.repo.MongoCustomerRepository;

//@Component
public class RefreshListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private CustomerRepository repository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        repository.deleteAll();

        // save a couple of customers
        for(int i = 0;i<10000;i++){
            Customer customer = new Customer("First Name " + i, "Name " + i);
            repository.insert(customer).subscribe();
        }
        List<Customer> customers = Arrays.asList(new Customer("Alice", "Smith"), new Customer("Bob", "Smith"));
        repository.insertAll(customers).subscribe();

        // fetch all customers
//        System.out.println("Customers found with findAll():");
//        for (Customer customer : repository.findAll().toIterable()) {
//            System.out.println(customer);
//        }
        System.out.println();

        // fetch an individual customer
        System.out.println("Customer found with findByFirstName('Alice'):");
        System.out.println(repository.findByFirstName("Alice").block());

        System.out.println("Customers found with findByLastName('Smith'):");
        for (Customer customer : repository.findByLastName("Smith").toIterable()) {
            System.out.println(customer);
        }

    }

}
