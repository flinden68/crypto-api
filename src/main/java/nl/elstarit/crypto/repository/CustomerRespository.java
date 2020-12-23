package nl.elstarit.crypto.repository;

import nl.elstarit.crypto.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRespository extends ReactiveMongoRepository<Customer, Integer> {
	Mono<Customer> getById(int id);
	Flux<Customer> findAll();
	Mono<Customer> findByName(String name);
	Mono<Customer> findByUsername(String username);
}
