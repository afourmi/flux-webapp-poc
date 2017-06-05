package org.talend.flux.messaging;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import akka.actor.ActorSystem;
import akka.kafka.ProducerSettings;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;

@Configuration
public class KafkaConfiguration {

    protected final ActorSystem system = ActorSystem.create("kafka-messaging");

    protected final Materializer materializer = ActorMaterializer.create(system);

    @Bean
    public ProducerSettings<String, String> producerSettings() {
        return ProducerSettings
                .create(system, new StringSerializer(), new StringSerializer())
                .withBootstrapServers("localhost:9092");
    }

    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        return producerSettings().createKafkaProducer();
    }
}
