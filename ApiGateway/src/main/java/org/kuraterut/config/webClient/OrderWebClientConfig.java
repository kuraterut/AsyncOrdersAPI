package org.kuraterut.config.webClient;

import lombok.RequiredArgsConstructor;
import org.kuraterut.config.properties.OrderServiceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class OrderWebClientConfig {

    private final OrderServiceProperties orderServiceProperties;

    @Bean
    public WebClient orderWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(orderServiceProperties.getBaseUrl())
                .build();
    }
}