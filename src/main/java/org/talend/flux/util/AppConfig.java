package org.talend.flux.util;

import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.mongodb.reactivestreams.client.MongoClients;

@EnableReactiveMongoRepositories
public class AppConfig extends AbstractReactiveMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "reactive";
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create();
    }
}
