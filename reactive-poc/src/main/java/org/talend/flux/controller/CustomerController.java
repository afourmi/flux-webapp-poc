package org.talend.flux.controller;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.talend.flux.repo.Customer;
import org.talend.flux.repo.MongoCustomerRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import akka.NotUsed;
import akka.japi.function.Function;
import akka.kafka.ProducerMessage;
import akka.kafka.ProducerSettings;
import akka.kafka.javadsl.Producer;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import reactor.core.publisher.Flux;

@RestController
public class CustomerController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private MongoCustomerRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaProducer<String, String> kafkaProducer;

    @Autowired
    private ProducerSettings<String, String> producerSettings;

    @Autowired
    private Materializer materializer;

    /**
     * See
     * https://spring.io/blog/2017/02/23/spring-framework-5-0-m5-update
     *
     */
    @GetMapping(path = "/customer")
    public Flux<Customer> list() {
        LOG.debug("get customers");
        return repository.findAll();
    }

    @PostMapping("/customer")
    public void create(@RequestBody Publisher<Customer> customerStream) {
        Flux<Customer> savecEntities = this.repository.saveAll(customerStream);
//        Source<Customer, NotUsed> source = Source.fromPublisher(savecEntities);
//        source
//                .map(getRecordFromCustomer())
//                .via(Producer.flow(producerSettings, kafkaProducer))
//                .map(debugRecord())
//                .runWith(Sink.ignore(), materializer);
    }

    private Function<Customer, ProducerMessage.Message<String, String, Customer>> getRecordFromCustomer() {
        return customer -> {
            String key = customer.toString();
            String customerAsJson = objectMapper.writeValueAsString(customer);
            return new ProducerMessage.Message<>(new ProducerRecord<>("customers", key, customerAsJson), customer);
        };
    }

    private Function<ProducerMessage.Result<String, String, Customer>, ProducerMessage.Result<String, String, Customer>>
            debugRecord() {
        return result -> {
            ProducerRecord<String, String> record = result.message().record();
            LOG.info("Send record: " + record.key());
            return result;
        };
    }
}