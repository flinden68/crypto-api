package nl.elstarit.crypto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CryptoPrice {

	@JsonProperty("EUR")
	String euro;

	@JsonProperty("USD")
	String dollar;
}
