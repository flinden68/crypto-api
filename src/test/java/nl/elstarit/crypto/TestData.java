package nl.elstarit.crypto;

import nl.elstarit.crypto.model.Customer;
import nl.elstarit.crypto.model.Transaction;
import nl.elstarit.crypto.model.TransactionType;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class TestData {

	@SafeVarargs
	public static Transaction transaction(Consumer<Transaction>... configuration) {
		Transaction transaction = Transaction.builder()
				.amount(BigDecimal.ONE)
				.type(TransactionType.BUY)
				.currencyCode("BTC")
				.build();
		Stream.of(configuration).forEach(c -> c.accept(transaction));

		return transaction;
	}

	@SafeVarargs
	public static Customer customer(Consumer<Customer>... configuration) {
		Customer customer = new Customer();
		customer.setPassword("password");
		customer.setUsername("username");
		customer.setName("name");
		Stream.of(configuration).forEach(c -> c.accept(customer));

		return customer;
	}
}
