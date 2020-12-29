package nl.elstarit.crypto.service;

import nl.elstarit.crypto.model.Customer;
import nl.elstarit.crypto.model.Transaction;
import nl.elstarit.crypto.repository.CustomerRespository;
import nl.elstarit.crypto.repository.TransactionRespository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static nl.elstarit.crypto.TestData.customer;
import static nl.elstarit.crypto.TestData.transaction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	@Mock
	TransactionRespository transactionRespository;

	@InjectMocks
	TransactionService transactionService;

	@Test
	void testSave() {
		Transaction transactionMock = transaction();

		when(transactionRespository.save(any(Transaction.class))).thenReturn(Mono.just(transactionMock));

		Mono<Transaction> transactionMono = transactionService.save(transactionMock);

		verify(transactionRespository, times(1)).save(any(Transaction.class));
		assertThat(transactionMock.getAmount()).isEqualTo(transactionMock.getAmount());
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

		when(transactionRespository.findByCustomerId(anyString())).thenReturn(Flux.just(transaction1, transaction2));

		Flux<Transaction> allTransactions = transactionService.findByCustomerId("user");
		verify(transactionRespository, times(1)).findByCustomerId("user");

		StepVerifier.create(allTransactions)
				.expectNextMatches(t -> t.getAmount().equals(BigDecimal.ONE))
				.expectNextMatches(t -> t.getAmount().equals(BigDecimal.TEN))
				.verifyComplete();
	}
}