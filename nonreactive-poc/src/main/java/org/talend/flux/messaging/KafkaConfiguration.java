package org.talend.flux.messaging;

import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {

    public static final String CUSTOMERS_TOPIC = "customers-non-reactive";

    @Value("${kafka.broker:localhost:9092}")
    private String kafkaBroker;

    @Bean
    public KafkaProducer<String, String> getKafkaProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaBroker);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("metadata.fetch.timeout.ms", 1000);

        return new KafkaProducer<>(props);
    }

    @Bean
    public KafkaConsumer<String, String> getConsumerSettings() {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaBroker);
        props.put("group.id", "consumer-id");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        return new KafkaConsumer<>(props);
    }
}
