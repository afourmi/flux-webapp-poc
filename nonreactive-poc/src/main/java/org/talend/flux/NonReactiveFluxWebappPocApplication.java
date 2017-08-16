package org.talend.flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class NonReactiveFluxWebappPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(NonReactiveFluxWebappPocApplication.class, args);
    }
}
