package weg.wojciech.recalculate_currency.controller;


import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weg.wojciech.recalculate_currency.model.RecalculatedCurrency;
import weg.wojciech.recalculate_currency.service.CurrencyService;


import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


@RestController
@RequestMapping("currency")
@Validated
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RecalculatedCurrency recalculatedCurrency(@RequestParam @Min(0)Double amount,
                                                     @RequestParam @Size(min= 3, max = 3, message = "currencyFrom code lenght must be 3") String currencyFrom,
                                                     @RequestParam @Size(min= 3, max = 3, message = "currencyTo code lenght must be 3") String currencyTo)  {

            return currencyService.calculate(amount,currencyFrom,currencyTo);
    }




}
