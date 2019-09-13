package weg.wojciech.recalculate_currency.service;

import org.springframework.stereotype.Service;
import weg.wojciech.recalculate_currency.model.RecalculatedCurrency;

import java.math.BigDecimal;
import java.util.Currency;

@Service
public class CurrencyService {


    public RecalculatedCurrency calculate(){
        return new RecalculatedCurrency(BigDecimal.valueOf(20L), Currency.getInstance("PLN"));
    }


    private void connect(){}

}
