package weg.wojciech.recalculate_currency.service;

import org.junit.jupiter.api.*;
import weg.wojciech.recalculate_currency.exceptions.ResourceNotFoundException;
import weg.wojciech.recalculate_currency.model.RecalculatedCurrency;
import java.util.Currency;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CurrencyServiceTest {

    private CurrencyService currencyService;
    HashMap<String,Double> codeMidMap;


    @BeforeEach
    public void setUp() throws Exception {
        currencyService = new CurrencyService();
        codeMidMap = currencyService.getCodeMidMap();
    }

    @Test
    public void isDataDownloaded() throws Exception{
        assertNotEquals(0,currencyService.getCodeMidMap().size());

    }

    @Test
    public void calculatePLNtoUSD(){

        RecalculatedCurrency expectedRecalculatedCurrency = new RecalculatedCurrency(0d, Currency.getInstance("USD"));
        expectedRecalculatedCurrency.setAmount( 100.0/codeMidMap.get("USD") );

        RecalculatedCurrency actualRecalculatedCurrency = currencyService.calculate(100.0,"PLN","USD");

        assertEquals(expectedRecalculatedCurrency.getCurrency(),actualRecalculatedCurrency.getCurrency());
        assertEquals(expectedRecalculatedCurrency.getAmount(),actualRecalculatedCurrency.getAmount());

    }

    @Test
    public void calculateUSDtoEUR(){

        RecalculatedCurrency expectedRecalculatedCurrency = new RecalculatedCurrency(0d, Currency.getInstance("EUR"));
        expectedRecalculatedCurrency.setAmount( 100.0*codeMidMap.get("USD")/codeMidMap.get("EUR") );

        RecalculatedCurrency actualRecalculatedCurrency = currencyService.calculate(100.0,"USD","EUR");

        assertEquals(expectedRecalculatedCurrency.getCurrency(),actualRecalculatedCurrency.getCurrency());
        assertEquals(expectedRecalculatedCurrency.getAmount(),actualRecalculatedCurrency.getAmount());

    }

    @Test
    public void checkIfCodeExist(){

        assertThrows(ResourceNotFoundException.class,
                ()-> currencyService.calculate(100.0,"USD","fUR"));

    }

}
