package com.example.rateprinter;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RatePrintingTask {
    
    private final RateFetchService rateFetchService;
    private int attemptCount = 0;
    
    public RatePrintingTask(RateFetchService rateFetchService) {
        this.rateFetchService = rateFetchService;
    }
    
    @Scheduled(fixedRate = 5000)
    public void printCurrencyRate() {
        attemptCount++;
        
        System.out.println("\n");
        System.out.println("Попытка #" + attemptCount);
        
        CurrencyRate rate = rateFetchService.fetchCurrentRate();
        
        if (rate != null) {
            System.out.println("Текущий курс:" "   " + rate);
        } else {
            System.out.println("Что-то пошло не так:(");
        }
    }
}
