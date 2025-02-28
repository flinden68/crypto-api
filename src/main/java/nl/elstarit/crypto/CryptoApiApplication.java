package nl.elstarit.crypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableMongoAuditing
@EnableReactiveMongoRepositories
public class CryptoApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(CryptoApiApplication.class, args);
	}
}
