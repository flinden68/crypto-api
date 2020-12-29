package nl.elstarit.crypto.repository;

import nl.elstarit.crypto.model.Transaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TransactionRespository extends ReactiveMongoRepository<Transaction, String> {
	Flux<Transaction> findByCustomerId(String customerId);
}
