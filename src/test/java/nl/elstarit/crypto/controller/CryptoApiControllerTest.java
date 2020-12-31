package nl.elstarit.crypto.controller;

import nl.elstarit.crypto.CryptoApiApplication;
import nl.elstarit.crypto.ResourceReader;
import nl.elstarit.crypto.configuration.WebSecurityConfiguration;
import nl.elstarit.crypto.model.Transaction;
import nl.elstarit.crypto.model.TransactionType;
import nl.elstarit.crypto.repository.CustomerRespository;
import nl.elstarit.crypto.repository.TransactionRespository;
import nl.elstarit.crypto.service.CryptoPriceTickerService;
import nl.elstarit.crypto.service.CustomerService;
import nl.elstarit.crypto.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigDecimal;

import static nl.elstarit.crypto.TestData.transaction;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = CryptoApiController.class)
@Import({CustomerService.class, WebSecurityConfiguration.class})
@ContextConfiguration(classes = CryptoApiApplication.class)
class CryptoApiControllerTest {

	@MockBean
	CryptoPriceTickerService cryptoPriceTickerService;

	@MockBean
	TransactionService transactionService;

	@MockBean
	CustomerService customerService;

	@MockBean
	MappingMongoConverter mappingMongoConverter;

	@MockBean
	ReactiveMongoTemplate reactiveMongoTemplate;

	@MockBean
	TransactionRespository transactionRespository;

	@MockBean
	CustomerRespository customerRespository;

	@Autowired
	ApplicationContext context;

	WebTestClient rest;

	@BeforeEach
	void setUp() {
		this.rest = WebTestClient
				.bindToApplicationContext(this.context)
				.configureClient()
				.build();
	}

	@Disabled
	@Test
	void prices() throws IOException {
		String json =  readFileFromResources("ok_btcdirect.json");
		when(cryptoPriceTickerService.getPricesList()).thenReturn(json);
		rest
				.get()
				.uri("/crypto-api/prices")
				.accept(MediaType.TEXT_EVENT_STREAM)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(String.class)
				.returnResult()
				.getResponseBody();
	}

	@WithMockUser(username = "user", password = "test")
	@Test
	void testBuyWithUser() {
		Transaction transactionMock = transaction();
		when(transactionService.save(anyString(), anyString(), anyString(), any(TransactionType.class))).thenReturn(Mono.just(transactionMock));

		rest
				.get()
				.uri("/crypto-api/buy/BTC/1")
				.exchange()
				.expectStatus().isOk();

		verify(transactionService, times(1)).save("BTC", "1", "user", TransactionType.BUY);

	}

	@WithAnonymousUser
	@Test
	void testBuyWithAnonymousUser() {
		rest
				.get()
				.uri("/crypto-api/buy/BTC/1")
				.exchange()
				.expectStatus().isForbidden();
	}

	@WithMockUser(username = "user", password = "test")
	@Test
	void testSellWithUser() {
		Transaction transactionMock = transaction();
		when(transactionService.save(anyString(), anyString(), anyString(), any(TransactionType.class))).thenReturn(Mono.just(transactionMock));

		rest
				.get()
				.uri("/crypto-api/sell/BTC/1")
				.exchange()
				.expectStatus().isOk();

		verify(transactionService, times(1)).save("BTC", "1", "user", TransactionType.BUY);

	}

	@WithAnonymousUser
	@Test
	void testSellWithAnonymousUser() {
		rest
				.get()
				.uri("/crypto-api/sell/BTC/1")
				.exchange()
				.expectStatus().isForbidden();
	}

	@WithMockUser(username = "user", password = "test")
	@Test
	void testTransactionsWithUser() {
		Transaction transaction1 = transaction(transaction -> {
			transaction.setCustomerId("user");
		});

		Transaction transaction2 = transaction(transaction -> {
			transaction.setCustomerId("user");
			transaction.setAmount(BigDecimal.TEN);
		});
		when(transactionService.findByCustomerName(anyString())).thenReturn(Flux.just(transaction1, transaction2));

		rest
				.get()
				.uri("/crypto-api/transactions")
				.exchange()
				.expectStatus().isOk();

		verify(transactionService, times(1)).findByCustomerName("user");
	}

	@WithAnonymousUser
	@Test
	void testTransactionsWithAnonymousUser() {
		rest
				.get()
				.uri("/crypto-api/transactions")
				.exchange()
				.expectStatus().isForbidden();
	}

	private static String readFileFromResources(String fileName) throws IOException {
		return ResourceReader.readFileToString(fileName);
	}
}