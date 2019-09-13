package weg.wojciech.recalculate_currency.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import weg.wojciech.recalculate_currency.model.RecalculatedCurrency;
import weg.wojciech.recalculate_currency.service.CurrencyService;


@RestController
@RequestMapping("currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RecalculatedCurrency recalculatedCurrency(@RequestParam Double amount,
                                                     @RequestParam String currencyFrom,
                                                     @RequestParam String currencyTo)  {

            return currencyService.calculate(amount,currencyFrom,currencyTo);
    }




}
