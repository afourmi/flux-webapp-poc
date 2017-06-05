package org.talend.flux.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.talend.flux.repo.Customer;
import org.talend.flux.repo.MongoCustomerRepository;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.kafka.ConsumerSettings;
import akka.kafka.ProducerMessage;
import akka.kafka.ProducerSettings;
import akka.kafka.Subscriptions;
import akka.kafka.javadsl.Consumer;
import akka.kafka.javadsl.Producer;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CustomerController {

    protected final ActorSystem system = ActorSystem.create("kafka-messaging");

    protected final Materializer materializer = ActorMaterializer.create(system);

    private ProducerSettings<String, String> producerSettings = ProducerSettings
            .create(system, new StringSerializer(), new StringSerializer())
            .withBootstrapServers("localhost:9092");

    private KafkaProducer<String, String> kafkaProducer = producerSettings.createKafkaProducer();

    private ConsumerSettings<String, String> consumerSettings = ConsumerSettings
            .create(system, new StringDeserializer(), new StringDeserializer())
            .withBootstrapServers("localhost:9092")
            .withGroupId("group1")
            .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    CompletionStage<Done> customers = Consumer
            .plainSource(consumerSettings, Subscriptions.assignment(new TopicPartition("customers", 0)))
            .mapAsync(1, record -> {
                System.out.println("consuming customer " + record.value());
                return CompletableFuture.completedFuture(Done.getInstance());
            })
            .runWith(Sink.ignore(), materializer);
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

        Source<Customer, NotUsed> source = Source.fromPublisher(customerStream);

        CompletionStage<Done> done = source.map(customer -> {
            String elem = String.valueOf(customer);
            return new ProducerMessage.Message<String, String, Customer>(
                    new ProducerRecord<>("customers", 0, null, elem), customer);
        }).via(Producer.flow(producerSettings)).map(result -> {
            ProducerRecord<String, String> record = result.message().record();
            System.out.println(record);
            return result;
        }).runWith(Sink.ignore(), materializer);

        return this.repository.saveAll(customerStream).then();
    }

}