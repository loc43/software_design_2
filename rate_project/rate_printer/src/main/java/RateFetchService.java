package com.example.rateprinter;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;

@Service
public class RateFetchService {
    
    private final RestTemplate restTemplate;
    private final String providerUrl = "http://localhost:8081/api/rates/usdrub";
    
    public RateFetchService() {
        this.restTemplate = new RestTemplate();
    }
    
    public CurrencyRate fetchCurrentRate() {
        try {
            CurrencyRate rate = restTemplate.getForObject(providerUrl, CurrencyRate.class);
            return rate;
        } catch (ResourceAccessException e) {
            System.err.println("Что-то пошло не так:(");
            return null;
        } catch (Exception e) {
            System.err.println("Что-то пошло не так:(");
            return null;
        }
    }
}
