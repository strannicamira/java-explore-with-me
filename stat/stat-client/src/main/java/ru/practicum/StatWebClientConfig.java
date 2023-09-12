package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
@RequiredArgsConstructor
public class StatWebClientConfig {

    @Value("${stat-server.url}")
    private String serviceUrl;

    @Bean
    public StatClient statClient(RestTemplateBuilder builder) {
        var restTemplate =
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serviceUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build();

        StatClient client = new StatClient(restTemplate);
        return client;
    }

}
