package com.pi.farmease.services;

import com.pi.farmease.dto.responses.CurrencyResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyConverterService {

    private static final String BASE_URL = "https://openexchangerates.org/api/latest.json";
    private static final String API_KEY = "082ec74329f249eb9646f1c5cbcd5cf8";
    private final RestTemplate restTemplate;

    public CurrencyConverterService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String convertCurrency(double amount, String fromCurrency, String toCurrency) {
        String url = String.format("%s?app_id=%s&base=%s&symbols=%s", BASE_URL, API_KEY, fromCurrency, toCurrency);

        // Faire une requête HTTP pour obtenir les taux de change
        CurrencyResponse response = restTemplate.getForObject(url, CurrencyResponse.class);

        // Extraire le taux de conversion
        double exchangeRate = response.getRates().get(toCurrency);

        // Effectuer la conversion
        double convertedAmount = amount * exchangeRate;

        // Create an explanatory message
        String message = String.format("%.2f %s is equivalent to %.2f %s", amount, fromCurrency, convertedAmount, toCurrency);

        return message;
    }
}
