package nl.elstarit.crypto.configuration;

import nl.elstarit.crypto.property.ThymeleafProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.StandardCharsets;

@Configuration
public class ThymeleafConfiguration {

    @Bean
    public SpringTemplateEngine springTemplateEngine(@Autowired ThymeleafProperties thymeleafProperties) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(htmlTemplateResolver(thymeleafProperties));
        return templateEngine;
    }

    @Bean
    public SpringResourceTemplateResolver htmlTemplateResolver(ThymeleafProperties thymeleafProperties) {
        SpringResourceTemplateResolver htmlTemplateResolver = new SpringResourceTemplateResolver();
        htmlTemplateResolver.setPrefix("classpath:/templates/");
        htmlTemplateResolver.setSuffix(".html");
        htmlTemplateResolver.setTemplateMode(TemplateMode.HTML);
        htmlTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        htmlTemplateResolver.setCacheable(thymeleafProperties.isCache());
        return htmlTemplateResolver;
    }

}
