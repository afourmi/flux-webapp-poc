package org.talend.flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class FluxWebappPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(FluxWebappPocApplication.class, args);
    }
}
