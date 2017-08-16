package org.talend.flux.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoCustomerRepository extends MongoRepository<Customer, String> {

    Optional<Customer> findByFirstName(String firstName);

    List<Customer> findByLastName(String lastName);

}
