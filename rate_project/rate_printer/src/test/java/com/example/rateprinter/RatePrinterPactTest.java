package com.example.rateprinter;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "rate-provider")
public class RatePrinterPactTest {

    @Pact(consumer = "rate-printer", provider = "rate-provider")
    public RequestResponsePact usdRubPact(PactDslWithProvider builder) {
        return builder
            .given("usd rub rate exists")
            .uponReceiving("GET usd rub rate")
            .path("/api/rates/usdrub")
            .method("GET")
            .willRespondWith()
            .status(200)
            .body(new PactDslJsonBody()
                .stringType("fromCurrency", "USD")
                .stringType("toCurrency", "RUB")
                .numberType("rate")
                .numberType("timestamp"))
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "usdRubPact")
    void testGetUsdRub(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplate();
        String url = mockServer.getUrl() + "/api/rates/usdrub";
        CurrencyRate rate = restTemplate.getForObject(url, CurrencyRate.class);
        assertNotNull(rate);
        assertEquals("USD", rate.getFromCurrency());
        assertEquals("RUB", rate.getToCurrency());
    }
}
