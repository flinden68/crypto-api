package nl.elstarit.crypto.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.thymeleaf")
@Data
public class ThymeleafProperties {
    boolean cache;
}
