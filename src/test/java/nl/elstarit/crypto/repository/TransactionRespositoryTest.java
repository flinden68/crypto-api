package nl.elstarit.crypto.repository;

import lombok.extern.slf4j.Slf4j;
import nl.elstarit.crypto.MongodbContainerInitializer;
import nl.elstarit.crypto.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static nl.elstarit.crypto.TestData.transaction;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ContextConfiguration(initializers = {MongodbContainerInitializer.class})
@Slf4j
class TransactionRespositoryTest {

	@Autowired
	ReactiveMongoTemplate reactiveMongoTemplate;

	@Autowired
	TransactionRespository transactionRespository;

	@BeforeEach
	public void setup() {
		this.reactiveMongoTemplate.remove(Transaction.class).all()
				.subscribe(r -> log.debug("delete all transactions: " + r), e -> log.debug("error: " + e), () -> log.debug("done"));
	}

	@Test
	void testFindByCustomerId() {
		Transaction transaction1 = transaction(transaction -> {
			transaction.setCustomerId("user");
		});

		Transaction transaction2 = transaction(transaction -> {
			transaction.setCustomerId("user");
			transaction.setAmount(BigDecimal.TEN);
		});

		Flux<Transaction> allTransactions = Flux.just(transaction1, transaction2)
				.flatMap(this.transactionRespository::save)
				.thenMany(this.transactionRespository.findByCustomerId("user2"));

		StepVerifier.create(allTransactions)
				.expectNextMatches(t -> t.getAmount().equals(BigDecimal.ONE))
				.expectNextMatches(t -> t.getAmount().equals(BigDecimal.TEN))
				.verifyComplete();
	}

	@Test
	void testSave() {
		StepVerifier.create(this.transactionRespository.save(transaction()))
				.consumeNextWith(t -> assertThat(t.getAmount()).isEqualTo(BigDecimal.ONE))
				.expectComplete()
				.verify();
	}
}