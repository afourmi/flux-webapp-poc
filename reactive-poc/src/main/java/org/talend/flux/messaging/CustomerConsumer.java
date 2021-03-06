package org.talend.flux.messaging;

import static org.talend.flux.messaging.KafkaConfiguration.CUSTOMERS_TOPIC;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.talend.flux.repo.Customer;

import com.fasterxml.jackson.databind.ObjectMapper;

import akka.Done;
import akka.actor.ActorSystem;
import akka.japi.function.Function;
import akka.kafka.ConsumerSettings;
import akka.kafka.Subscriptions;
import akka.kafka.javadsl.Consumer;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Sink;

@Component
public class CustomerConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerConsumer.class);

    @Autowired
    private ActorMaterializer materializer;

    @Autowired
    private ActorSystem system;

    @Autowired
    private ConsumerSettings<String, String> consumerSettings;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void createConsumer() {
        Consumer
                .plainSource(consumerSettings, Subscriptions.topics(CUSTOMERS_TOPIC))
                .mapAsync(1, consumeCustomer())
                .runWith(Sink.ignore(), materializer);
    }

    private Function<ConsumerRecord<String, String>, CompletionStage<Done>> consumeCustomer() {
        return record -> {
            try{
            Customer customer = objectMapper.readValue(record.value(), Customer.class);
            LOG.info("consuming customer " + customer.toString());
            } catch (Exception e){
                LOG.error("Error when consuming " + record);
            }
            return CompletableFuture.completedFuture(Done.getInstance());
        };
    }

}
