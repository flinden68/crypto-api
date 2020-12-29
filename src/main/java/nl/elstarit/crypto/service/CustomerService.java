package nl.elstarit.crypto.service;

import lombok.extern.slf4j.Slf4j;
import nl.elstarit.crypto.model.Customer;
import nl.elstarit.crypto.repository.CustomerRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CustomerService {

	@Autowired
	CustomerRespository customerRespository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public void save(Customer customer) {
		log.info("Save customer: {}", customer);
		customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
		customerRespository.save(customer).subscribe(result -> log.info("Entity has been saved: {}", result));;
	}
	
}
