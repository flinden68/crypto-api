package nl.elstarit.crypto.service;

import lombok.extern.slf4j.Slf4j;
import nl.elstarit.crypto.model.CryptoPrices;
import nl.elstarit.crypto.property.ExternalProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CryptoPriceTickerService {

	@Autowired
	ExternalProperty externalProperty;

	@Autowired
	WebClient webClient;

	@Autowired
	RestTemplate restTemplate;

	public String getPricesList(){
		log.debug("Get Crypto Prices from url {}", externalProperty.getBtcDirectApi());
		ResponseEntity<String> response = restTemplate.exchange(externalProperty.getBtcDirectApi(),
				HttpMethod.GET,
				null, String.class);
		log.debug("Response statuscode = " + response.getStatusCode().toString());
		if (response.getStatusCode().is2xxSuccessful()) {
			if (response.hasBody()) {
				return response.getBody();
			} else {
				return new String();
			}
		}

		return null;
	}

	public Mono<String> getPrices(){
		log.info("Get Crypto Prices from url {}", externalProperty.getBtcDirectApi());
		return this.webClient
				.get()
				.uri(externalProperty.getBtcDirectApi())

				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, clientResponse ->
						Mono.error(new RuntimeException("Crypto Prices could be found"))
				)
				.onStatus(HttpStatus::is5xxServerError, clientResponse ->
						Mono.error(new RuntimeException("Something went wrong: " + clientResponse))
				)
				.bodyToMono(String.class);
	}
}
