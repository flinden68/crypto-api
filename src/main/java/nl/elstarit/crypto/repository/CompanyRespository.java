package nl.elstarit.crypto.repository;

import nl.elstarit.crypto.model.Company;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CompanyRespository extends ReactiveMongoRepository<Company, Integer> {
	Mono<Company> getById(int id);
	Flux<Company> findAll();
	Mono<Company> findByName(String name);
}
