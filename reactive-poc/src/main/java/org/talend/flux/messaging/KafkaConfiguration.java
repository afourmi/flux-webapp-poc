package org.talend.flux.messaging;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import akka.actor.ActorSystem;
import akka.kafka.ConsumerSettings;
import akka.kafka.ProducerSettings;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;

@Configuration
public class KafkaConfiguration {

    public static final String CUSTOMERS_TOPIC = "customers-reactive";

    @Value("${kafka.broker:localhost:9092}")
    private String kafkaBroker;

    @Bean
    public ActorSystem getSystem() {
        return ActorSystem.create("kafka-messaging");
    }

    @Bean
    public Materializer getMaterializer(ActorSystem system) {
        return ActorMaterializer.create(system);
    }

    @Bean
    public ProducerSettings<String, String> getProducerSettings(ActorSystem system) {
        return ProducerSettings
                .create(system, new StringSerializer(), new StringSerializer())
                .withBootstrapServers(kafkaBroker);
    }

    @Bean
    public KafkaProducer<String, String> getKafkaProducer(ProducerSettings<String, String> producerSettings) {
        return producerSettings.createKafkaProducer();
    }

    @Bean
    public ConsumerSettings<String, String> getConsumerSettings(ActorSystem system) {
        return ConsumerSettings
                .create(system, new StringDeserializer(), new StringDeserializer())
                .withBootstrapServers(kafkaBroker)
                .withGroupId("group1")
                .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    }
}
