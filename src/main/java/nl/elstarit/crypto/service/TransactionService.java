package nl.elstarit.crypto.service;

import lombok.extern.slf4j.Slf4j;
import nl.elstarit.crypto.model.Customer;
import nl.elstarit.crypto.model.Transaction;
import nl.elstarit.crypto.repository.CustomerRespository;
import nl.elstarit.crypto.repository.TransactionRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class TransactionService {

	@Autowired
	TransactionRespository transactionRespository;
	public Mono<Transaction> save(Transaction transaction) {
		return transactionRespository.save(transaction)
				.map(
						t1 -> Transaction.builder()
						.id(t1.getId())
						.type(t1.getType())
						.customerId(t1.getCustomerId())
						.amount(t1.getAmount())
						.currencyCode(t1.getCurrencyCode())
								.build()
				);
	}

	public Flux<Transaction> findByCustomerId(String customerId) {
		return transactionRespository.findByCustomerId(customerId);
	}
}
