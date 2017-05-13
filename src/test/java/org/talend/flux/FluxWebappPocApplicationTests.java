package org.talend.flux;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;

import org.junit.Test;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.talend.flux.repo.Customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxWebappPocApplicationTests {

    private static final Logger LOG = LoggerFactory.getLogger(FluxWebappPocApplicationTests.class);

    @Test
    public void list() throws InterruptedException {
        LOG.info("start test");

        WebClient client = WebClient.create("http://localhost:19080");

        Mono<Customer> customerFlux = client.get().uri("/customer").accept(APPLICATION_STREAM_JSON).exchange().subscribe()
                .flatMap(response -> response.bodyToMono(Customer.class))
                .doOnEach(customerSignal -> System.out.println(customerSignal.get()));

        customerFlux.subscribe();

        Thread.sleep(10000);
    }

	@Test
	public void create() throws InterruptedException {

		WebClient client = WebClient.create("http://localhost:19080");

		Flux<Customer> newCustomers = Flux.just(new Customer("Aurélien", "Fourmi"));
		Mono<ClientResponse> response = client.post().uri("/customer").accept(APPLICATION_STREAM_JSON).body(newCustomers, Customer.class).exchange();
		response.subscribe();

		Thread.sleep(1000);
	}

}
