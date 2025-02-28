package nl.elstarit.crypto.service;

import lombok.extern.slf4j.Slf4j;
import nl.elstarit.crypto.model.Customer;
import nl.elstarit.crypto.repository.CustomerRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerService {

	@Autowired
	CustomerRespository customerRespository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public void save(Customer customer) {
		customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
		customerRespository.save(customer).subscribe();;
	}

}
