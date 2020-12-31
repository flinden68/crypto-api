package nl.elstarit.crypto.service;

import lombok.extern.slf4j.Slf4j;
import nl.elstarit.crypto.model.Customer;
import nl.elstarit.crypto.model.Transaction;
import nl.elstarit.crypto.model.TransactionType;
import nl.elstarit.crypto.repository.CustomerRespository;
import nl.elstarit.crypto.repository.TransactionRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Service
public class TransactionService {

	@Autowired
	TransactionRespository transactionRespository;

	public Mono<Transaction> save(String coinCode, String amount, String username, TransactionType transactionType){
		Transaction transaction = new Transaction();
		transaction.setAmount(new BigDecimal(amount));
		transaction.setCurrencyCode(coinCode);
		transaction.setCustomerName(username);
		transaction.setType(transactionType);

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

	public Flux<Transaction> findByCustomerName(String customerName) {
		return transactionRespository.findByCustomerName(customerName);
	}
}
