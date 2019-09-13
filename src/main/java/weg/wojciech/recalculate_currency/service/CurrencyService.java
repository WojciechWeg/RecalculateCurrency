package weg.wojciech.recalculate_currency.service;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import weg.wojciech.recalculate_currency.model.RecalculatedCurrency;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.HashMap;


@Service
public class CurrencyService {

    private String tableA_URL = "http://api.nbp.pl/api/exchangerates/tables/a";
    private String tableB_URL = "http://api.nbp.pl/api/exchangerates/tables/b";
    private HashMap<String,Double> codeMidMap;

    public CurrencyService() throws IOException, ParseException {
        this.codeMidMap = new HashMap<>();
        addToCodeMidMap(extractRates(connectAndRetrieveJSONArray(tableA_URL)));
        addToCodeMidMap(extractRates(connectAndRetrieveJSONArray(tableB_URL)));
        System.out.println(codeMidMap);
    }

    public RecalculatedCurrency calculate(){
        return new RecalculatedCurrency(BigDecimal.valueOf(20L), Currency.getInstance("PLN"));
    }


    private JSONArray connectAndRetrieveJSONArray(String urlString) throws IOException, ParseException {

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestMethod("GET");

        InputStream in = new BufferedInputStream(connection.getInputStream());

        String responseString = IOUtils.toString(in, StandardCharsets.UTF_8);

        in.close();
        connection.disconnect();

        JSONParser parser = new JSONParser();
        return (JSONArray) parser.parse(responseString);
    }

    private void addToCodeMidMap(JSONArray rates) {

        for(int i=0; i<rates.size(); i++){

            JSONObject rate = (JSONObject) rates.get(i);
            String code = (String) rate.get("code");
            Double mid = (Double) rate.get("mid");
            codeMidMap.put(code,mid);
        }

    }

    private JSONArray extractRates(JSONArray jsonArray) throws ParseException {

        JSONObject jsonContentObject = (JSONObject) jsonArray.get(0);

        return (JSONArray) jsonContentObject.get("rates");
    }
}
