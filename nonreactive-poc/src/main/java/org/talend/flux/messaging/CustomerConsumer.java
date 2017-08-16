package org.talend.flux.messaging;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.talend.flux.repo.Customer;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomerConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerConsumer.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaConsumer<String, String> kafkaConsumer;

    @PostConstruct
    public void createConsumer() {
        // kafkaConsumer.subscribe(Collections.singletonList(CUSTOMERS_TOPIC), new ConsumerRebalanceListener() {
        // @Override
        // public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        //
        // }
        //
        // @Override
        // public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        //
        // }
        // });
    }

    private void consumeCustomer(ConsumerRecord<String, String> record) {
        try {
            Customer customer = objectMapper.readValue(record.value(), Customer.class);
            LOG.info("consuming customer " + customer.toString());
        } catch (Exception e) {
            LOG.error("Error when consuming " + record);
        }
    }

}
