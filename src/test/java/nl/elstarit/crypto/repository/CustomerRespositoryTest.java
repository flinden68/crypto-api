package nl.elstarit.crypto.repository;

import lombok.extern.slf4j.Slf4j;
import nl.elstarit.crypto.MongodbContainerInitializer;
import nl.elstarit.crypto.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static nl.elstarit.crypto.TestData.customer;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ContextConfiguration(initializers = {MongodbContainerInitializer.class})
@Slf4j
class CustomerRespositoryTest {

	@Autowired
	ReactiveMongoTemplate reactiveMongoTemplate;

	@Autowired
	CustomerRespository customerRespository ;

	@BeforeEach
	public void setup() {
		this.reactiveMongoTemplate.remove(Customer.class).all()
				.subscribe(r -> log.debug("delete all customers: " + r), e -> log.debug("error: " + e), () -> log.debug("done"));
	}

	@Test
	void testFindByUsername() {
		Customer customer = customer();

		Mono<UserDetails> currentCustomer = Mono.just(customer)
				.flatMap(this.customerRespository::save)
				.then(this.customerRespository.findByUsername("username"));

		StepVerifier.create(currentCustomer)
				.expectNextMatches(userDetails -> userDetails.getUsername().equals("username"))
				.verifyComplete();
	}

	@Test
	void testSave() {
		StepVerifier.create(this.customerRespository.save(customer()))
				.consumeNextWith(customer -> assertThat(customer.getUsername()).isEqualTo("username"))
				.expectComplete()
				.verify();
	}
}