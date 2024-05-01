package com.pi.farmease.controllers;

import com.pi.farmease.services.CurrencyConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyConverterController {

    @Autowired
    private CurrencyConverterService currencyConverterService;

    @GetMapping("/convert")
    public String convertCurrency(@RequestParam double amount,
                                  @RequestParam String fromCurrency,
                                  @RequestParam String toCurrency) {
        return currencyConverterService.convertCurrency(amount, fromCurrency, toCurrency);
    }
}
