package weg.wojciech.recalculate_currency.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import weg.wojciech.recalculate_currency.model.RecalculatedCurrency;
import weg.wojciech.recalculate_currency.service.CurrencyService;

import java.math.BigDecimal;
import java.util.Currency;

@RestController
@RequestMapping("currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RecalculatedCurrency recalculatedCurrency()  {

            return  new RecalculatedCurrency(BigDecimal.valueOf(10L), Currency.getInstance("USD"));
    }




}
