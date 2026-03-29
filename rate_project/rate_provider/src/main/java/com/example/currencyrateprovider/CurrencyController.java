package com.example.currencyrateprovider;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Random;

@RestController
@RequestMapping("/api/rates")
public class CurrencyController {
    
    private final Random random = new Random();
    private double baseRate = 80.0;
  
    @GetMapping("/usdrub")
    public CurrencyRate getUsdRubRate() {
        double fluctuation = (random.nextDouble() * 4) - 2;
        double currentRate = baseRate + fluctuation;
        
        if (random.nextInt(10) == 0) {
            baseRate += (random.nextDouble() - 0.5) * 2;
            if (baseRate < 70) baseRate = 70;
            if (baseRate > 110) baseRate = 110;
        }
        
        return new CurrencyRate("USD", "RUB", currentRate);
    }
}
