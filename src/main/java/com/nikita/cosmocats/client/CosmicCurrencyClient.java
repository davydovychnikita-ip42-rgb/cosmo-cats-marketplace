package com.nikita.cosmocats.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class CosmicCurrencyClient {

    private final RestClient restClient;

    public CosmicCurrencyClient(@Value("${currency.service.url:http://localhost:8089}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public BigDecimal getExchangeRate(String currency) {
        RateResponse response = restClient.get()
                .uri("/api/rates/{currency}", currency)
                .retrieve()
                .body(RateResponse.class);
        return response != null ? response.rate() : BigDecimal.ONE;
    }

    public record RateResponse(String currency, BigDecimal rate) {}
}