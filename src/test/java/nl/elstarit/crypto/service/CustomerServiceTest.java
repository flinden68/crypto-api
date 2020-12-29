package nl.elstarit.crypto.service;

import nl.elstarit.crypto.model.Customer;
import nl.elstarit.crypto.repository.CustomerRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static nl.elstarit.crypto.TestData.customer;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	@Mock
	CustomerRespository customerRespository;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@InjectMocks
	CustomerService customerService;

	@Test
	void save() {
		Customer customerMock = customer();

		when(customerRespository.save(any(Customer.class))).thenReturn(Mono.just(customerMock));

		customerService.save(customerMock);

		verify(customerRespository, times(1)).save(any(Customer.class));
	}
}