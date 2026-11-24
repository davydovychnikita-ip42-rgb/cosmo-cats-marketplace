package com.nikita.cosmocats.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

class CosmicCurrencyClientIT {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @Test
    void getExchangeRateShouldReturnStubbedRate() {
        wireMock.stubFor(get(urlEqualTo("/api/rates/GALACTIC_CREDIT"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"currency\":\"GALACTIC_CREDIT\",\"rate\":42.50}")));

        CosmicCurrencyClient client = new CosmicCurrencyClient(wireMock.baseUrl());
        BigDecimal rate = client.getExchangeRate("GALACTIC_CREDIT");

        assertEquals(new BigDecimal("42.50"), rate);
        wireMock.verify(1, getRequestedFor(urlEqualTo("/api/rates/GALACTIC_CREDIT")));
    }

    @Test
    void getExchangeRateShouldThrowWhenExternalServiceFails() {
        wireMock.stubFor(get(urlEqualTo("/api/rates/UNKNOWN"))
                .willReturn(aResponse().withStatus(500)));

        CosmicCurrencyClient client = new CosmicCurrencyClient(wireMock.baseUrl());

        assertThrows(HttpServerErrorException.class, () -> client.getExchangeRate("UNKNOWN"));
    }
}