package nl.elstarit.crypto.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "external.api")
@Data
public class ExternalApiProperties {
    private ConnectionPoolTimeoutProperties timeouts;
    private PoolsizeProperties pool;
}
