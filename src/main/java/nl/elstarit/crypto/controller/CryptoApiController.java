package nl.elstarit.crypto.controller;

import nl.elstarit.crypto.model.Transaction;
import nl.elstarit.crypto.model.TransactionType;
import nl.elstarit.crypto.service.CryptoPriceTickerService;
import nl.elstarit.crypto.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/crypto-api")
public class CryptoApiController {

	@Autowired
	CryptoPriceTickerService cryptoPriceTickerService;

	@Autowired
	TransactionService transactionService;

	@GetMapping(value = "/prices", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<String>> prices(){
		return Flux.interval(Duration.ofSeconds(10))
				.map(sequence -> ServerSentEvent.<String> builder()
						.id(String.valueOf(sequence))
						.data(cryptoPriceTickerService.getPricesList())
						.build());
	}

	@GetMapping("/buy/{coinCode}/{amount}")
	public Mono<Transaction> buy(@PathVariable("coinCode") String coinCode, @PathVariable("amount") String amount, @AuthenticationPrincipal UserDetails userDetails){
		if (userDetails == null) {
			return Mono.empty();
		}

		return transactionService.save(coinCode, amount, userDetails.getUsername(), TransactionType.BUY);
	}

	@GetMapping("/sell/{coinCode}/{amount}")
	public Mono<Transaction> sell(@PathVariable("coinCode") String coinCode, @PathVariable("amount") String amount, @AuthenticationPrincipal UserDetails userDetails){
		if (userDetails == null) {
			return Mono.empty();
		}

		return transactionService.save(coinCode, amount, userDetails.getUsername(), TransactionType.SELL);
	}

	@GetMapping("/transactions")
	public Flux<Transaction> transactions(@AuthenticationPrincipal UserDetails userDetails){
		if (userDetails == null) {
			return Flux.empty();
		}

		return transactionService.findByCustomerName(userDetails.getUsername());
	}

}
