package nl.elstarit.crypto.repository;

import nl.elstarit.crypto.model.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRespository extends ReactiveCrudRepository<Customer, String> {
	Mono<UserDetails> findByUsername(String username);
}
