package org.talend.flux.controller;

import java.util.List;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.talend.flux.repo.Customer;
import org.talend.flux.repo.MongoCustomerRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class CustomerController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private MongoCustomerRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaProducer<String, String> kafkaProducer;

    @GetMapping(path = "/customer")
    public List<Customer> list() {
        LOG.debug("get customers");
        return repository.findAll();
    }

    @PostMapping("/customer")
    public void create(@RequestBody List<Customer> customerStream) {
        List<Customer> savecEntities = this.repository.save(customerStream);
//        savecEntities.stream().map(this::getRecordFromCustomer).forEach(o -> kafkaProducer.send(o));
    }

    private ProducerRecord<String, String> getRecordFromCustomer(Customer customer) {
        String key = customer.toString();
        String customerAsJson = null;
        try {
            customerAsJson = objectMapper.writeValueAsString(customer);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new ProducerRecord<>("customers", key, customerAsJson);
    }
}