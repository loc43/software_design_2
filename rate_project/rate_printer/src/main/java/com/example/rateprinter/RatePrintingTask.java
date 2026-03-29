package com.example.rateprinter;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RatePrintingTask {
    
    private final RateFetchService rateFetchService;
    
    public RatePrintingTask(RateFetchService rateFetchService) {
        this.rateFetchService = rateFetchService;
    }
    
    @Scheduled(fixedRate = 5000)
    public void printCurrencyRate() {
        CurrencyRate rate = rateFetchService.fetchCurrentRate();
        if (rate != null) {
            System.out.println(rate.getFromCurrency() + "/" + rate.getToCurrency() + 
                ": " + rate.getRate());
        } else {
            System.out.println("Failed to get rate");
        }
    }
}
