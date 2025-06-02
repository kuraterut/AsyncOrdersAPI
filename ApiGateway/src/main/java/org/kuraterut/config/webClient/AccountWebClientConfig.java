package org.kuraterut.config.webClient;

import lombok.RequiredArgsConstructor;
import org.kuraterut.config.properties.AccountServiceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class AccountWebClientConfig {

    private final AccountServiceProperties accountServiceProperties;

    @Bean
    public WebClient accountWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(accountServiceProperties.getBaseUrl())
                .build();
    }
}