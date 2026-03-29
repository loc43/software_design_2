package com.example.rateprinter;

public class CurrencyRate {
    private String fromCurrency;
    private String toCurrency;
    private double rate;
    private long timestamp;
    
    public CurrencyRate() {}
    
    public CurrencyRate(String fromCurrency, String toCurrency, double rate) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getFromCurrency() { return fromCurrency; }
    public String getToCurrency() { return toCurrency; }
    public double getRate() { return rate; }
    public long getTimestamp() { return timestamp; }
    
    public void setFromCurrency(String fromCurrency) { this.fromCurrency = fromCurrency; }
    public void setToCurrency(String toCurrency) { this.toCurrency = toCurrency; }
    public void setRate(double rate) { this.rate = rate; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    @Override
    public String toString() {
        return String.format("Курс %s/%s: %.2f", fromCurrency, toCurrency, rate);
    }
}
