package org.kuraterut.config.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api-gateway.services.order")
@Getter
@Setter
public class OrderServiceProperties {
    private String baseUrl;
}
