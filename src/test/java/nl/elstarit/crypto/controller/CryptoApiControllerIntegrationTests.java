package nl.elstarit.crypto.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class CryptoApiControllerIntegrationTests {

	@LocalServerPort
	int port;

	WebTestClient client;

	@BeforeEach
	public void setup() {

		this.client = WebTestClient.bindToServer()
				.baseUrl("http://localhost:" + this.port)
				.build();
	}

	@Test
	void pricesShouldBeOk() {
		client.get().uri("/crypto-api/prices").exchange()
				.expectStatus().isOk();
	}
}
