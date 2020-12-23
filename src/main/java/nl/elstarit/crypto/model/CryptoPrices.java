package nl.elstarit.crypto.model;

import lombok.Data;

import java.util.Map;

@Data
public class CryptoPrices {
	Map<String, CryptoPrice> cryptoPriceMap;
}
