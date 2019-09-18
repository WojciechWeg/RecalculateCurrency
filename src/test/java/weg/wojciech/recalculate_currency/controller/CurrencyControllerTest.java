package weg.wojciech.recalculate_currency.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StringUtils;
import weg.wojciech.recalculate_currency.model.RecalculatedCurrency;
import weg.wojciech.recalculate_currency.service.CurrencyService;
import java.util.Currency;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;

@AutoConfigureRestDocs()
@ExtendWith(SpringExtension.class)
@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Test
    public void calculatePLNtoUSD() throws Exception {


        RecalculatedCurrency expectedRecalculatedCurrency = new RecalculatedCurrency(25d, Currency.getInstance("USD"));
        when(currencyService.calculate(any(),any(),any())).thenReturn(expectedRecalculatedCurrency);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                                    .get("/currency")
                                    .param("amount","100")
                                    .param("currencyFrom","PLN")
                                    .param("currencyTo","USD"))
                                    .andDo(document("currency/calculate",
                                            responseFields(
                                                    fieldWithPath("amount").description("Amount of recalculated money"),
                                                    fieldWithPath("currency").description("Currency of recalculated money.")
                                            ),
                                            requestParameters(
                                                    parameterWithName("amount").description("Amount of money to be recalculated"),
                                                    parameterWithName("currencyFrom").description("Currency of input money"),
                                                    parameterWithName("currencyTo").description("Currency of returned money")
                                            )

                                    ))
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

        assertEquals( "BAD_REQUEST",jsonResponse.get("status"));
        assertEquals( "recalculatedCurrency.currencyFrom: currencyFrom code lenght must be 3",jsonResponse.get("messages"));

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

        assertEquals("BAD_REQUEST",jsonResponse.get("status") );
        assertEquals("recalculatedCurrency.amount: must be greater than or equal to 0",jsonResponse.get("messages"));

    }


}
