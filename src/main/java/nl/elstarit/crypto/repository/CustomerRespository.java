package nl.elstarit.crypto.repository;

import nl.elstarit.crypto.model.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRespository extends ReactiveCrudRepository<Customer, String> {
	Mono<Customer> getById(int id);
	Flux<Customer> findAll();
	Mono<Customer> findByName(String name);
	Mono<UserDetails> findByUsername(String username);
}
