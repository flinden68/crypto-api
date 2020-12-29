package nl.elstarit.crypto.service;

import nl.elstarit.crypto.ResourceReader;
import nl.elstarit.crypto.property.ExternalProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CryptoPriceTickerServiceTest {

	@Mock
	RestTemplate mockRestTemplate;

	@Mock
	ExternalProperty externalProperty;

	@InjectMocks
	CryptoPriceTickerService cryptoPriceTickerService;

	@BeforeEach
	void setup(){
		when(externalProperty.getBtcDirectApi()).thenReturn("link");
	}

	@Test
	void testGetPricesList200HttpStatusCode() throws IOException {
		String json =  readFileFromResources("ok_btcdirect.json");
		ResponseEntity<String> response = new ResponseEntity<>(json, HttpStatus.OK);

		when(mockRestTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenReturn(response);

		String result = cryptoPriceTickerService.getPricesList();

		verify(mockRestTemplate, times(1)).exchange(anyString(), any(), any(), eq(String.class));
		assertEquals(json, result);
	}

	@Test
	void testGetPricesListNot200HttpStatusCode() throws IOException {
		String json =  readFileFromResources("ok_btcdirect.json");
		ResponseEntity<String> response = new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);

		when(mockRestTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenReturn(response);

		String result = cryptoPriceTickerService.getPricesList();

		verify(mockRestTemplate, times(1)).exchange(anyString(), any(), any(), eq(String.class));
		assertEquals(null, result);
	}

	@Test
	void testGetPricesList200HttpStatusCodeButWithNoBody() throws IOException {
		String json =  readFileFromResources("ok_btcdirect.json");
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);

		when(mockRestTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenReturn(response);

		String result = cryptoPriceTickerService.getPricesList();

		verify(mockRestTemplate, times(1)).exchange(anyString(), any(), any(), eq(String.class));
		assertEquals(new String(), result);
	}

	private static String readFileFromResources(String fileName) throws IOException {
		return ResourceReader.readFileToString(fileName);
	}
}