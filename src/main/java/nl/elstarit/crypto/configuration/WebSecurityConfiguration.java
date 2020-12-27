package nl.elstarit.crypto.configuration;

import nl.elstarit.crypto.repository.CustomerRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfiguration {

  @Autowired
  CustomerRespository customerRespository;

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
    return http
            .csrf().disable()
            .authorizeExchange()
            .pathMatchers("/actuator/**").permitAll()
            .pathMatchers("/crypto-api/prices").permitAll()
            .pathMatchers("/", "/welcome", "/login", "/registration").permitAll()
            .pathMatchers("/js/**").permitAll()
            .anyExchange().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .and()
            .csrf().disable()
            .build();
  }

  @Bean
  public ReactiveUserDetailsService userDetailsService() {
    return (username) -> customerRespository.findByUsername(username);
  }

}
