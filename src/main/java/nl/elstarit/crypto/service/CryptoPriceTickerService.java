package nl.elstarit.crypto.service;

import lombok.extern.slf4j.Slf4j;
import nl.elstarit.crypto.property.ExternalProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class CryptoPriceTickerService {

	@Autowired
	ExternalProperty externalProperty;

	@Autowired
	RestTemplate restTemplate;

	public String getPricesList(){
		log.debug("Get Crypto Prices from url {}", externalProperty.getBtcDirectApi());
		ResponseEntity<String> response = restTemplate.exchange(externalProperty.getBtcDirectApi(),
				HttpMethod.GET,
				null,
				String.class);
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

}
