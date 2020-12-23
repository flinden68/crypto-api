package nl.elstarit.crypto.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "external")
@Data
public class ExternalProperty {
	String btcDirectApi;
}
