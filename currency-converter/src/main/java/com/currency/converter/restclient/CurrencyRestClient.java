package com.currency.converter.restclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class CurrencyRestClient {

    @Value("${currConv.base.url}")
    private String currConvBaseUrl;

    @Value("${currConv.apiKey}")
    private String currConvApiKey;

    private WebClient cmsWebClient;

    @PostConstruct
    void init() {
        this.cmsWebClient = WebClient.builder()
                .baseUrl(currConvBaseUrl)
                .build();
    }

    public Mono<Map> getResponse(String source, String target) {
        String query = source+"_"+target;
        return cmsWebClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v7/convert")
                                .queryParam("q", query)
                                .queryParam("compact", "ultra")
                                .queryParam("apiKey", currConvApiKey)
                                .build()
                ).exchange().flatMap(response -> response.bodyToMono(Map.class));
    }
}