package weg.wojciech.recalculate_currency.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import weg.wojciech.recalculate_currency.model.RecalculatedCurrency;
import weg.wojciech.recalculate_currency.service.CurrencyService;
import java.util.Currency;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;


    @Test
    public void calculatePLNtoUSD() throws Exception {

        RecalculatedCurrency expectedRecalculatedCurrency = new RecalculatedCurrency(100d, Currency.getInstance("USD"));
        when(currencyService.calculate(any(),any(),any())).thenReturn(expectedRecalculatedCurrency);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                                    .get("/currency")
                                    .param("amount","100")
                                    .param("currencyFrom","PLN")
                                    .param("currencyTo","USD"))
                                    .andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);

        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(result.getResponse().getContentAsString());

        assertNotNull(jsonResponse);


    }

    @Test
    public void calculateEURtoUSD() throws Exception {

        RecalculatedCurrency expectedRecalculatedCurrency = new RecalculatedCurrency(100d, Currency.getInstance("USD"));
        when(currencyService.calculate(any(),any(),any())).thenReturn(expectedRecalculatedCurrency);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/currency")
                .param("amount","100")
                .param("currencyFrom","EUR")
                .param("currencyTo","USD"))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);

        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(result.getResponse().getContentAsString());

        assertNotNull(jsonResponse);
        assertEquals("USD",jsonResponse.get("currency"));

    }

    @Test
    public void checkIfCodeIsValid() throws Exception {

        RecalculatedCurrency expectedRecalculatedCurrency = new RecalculatedCurrency(100d, Currency.getInstance("USD"));
        when(currencyService.calculate(any(),any(),any())).thenReturn(expectedRecalculatedCurrency);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/currency")
                .param("amount","100")
                .param("currencyFrom","EUDd")
                .param("currencyTo","USD"))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);

        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(result.getResponse().getContentAsString());

        assertEquals(jsonResponse.get("status"), "BAD_REQUEST");
        assertEquals(jsonResponse.get("messages"), "recalculatedCurrency.currencyFrom: currencyFrom code lenght must be 3");

    }

    @Test
    public void checkIfAmountIsValid() throws Exception {
        RecalculatedCurrency expectedRecalculatedCurrency = new RecalculatedCurrency(100d, Currency.getInstance("USD"));
        when(currencyService.calculate(any(),any(),any())).thenReturn(expectedRecalculatedCurrency);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/currency")
                .param("amount","-100")
                .param("currencyFrom","EUD")
                .param("currencyTo","USD"))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);

        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(result.getResponse().getContentAsString());

        assertEquals(jsonResponse.get("status"), "BAD_REQUEST");
        assertEquals(jsonResponse.get("messages"), "recalculatedCurrency.amount: must be greater than or equal to 0");

    }


}
