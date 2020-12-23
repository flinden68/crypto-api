package nl.elstarit.crypto.controller;

import nl.elstarit.crypto.service.CryptoPriceTickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping("/crypto-api")
public class CryptoPriceController {

	@Autowired
	CryptoPriceTickerService cryptoPriceTickerService;

	@GetMapping(value = "/prices", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<String>> prices(){
		return Flux.interval(Duration.ofSeconds(10))
				.map(sequence -> ServerSentEvent.<String> builder()
						.id(String.valueOf(sequence))
						.data(cryptoPriceTickerService.getPricesList())
						.build());
	}

}
