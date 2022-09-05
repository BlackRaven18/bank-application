package com.bank;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.MalformedURLException;
import java.util.Locale;

public class APINBPManager {

    private String currency;

    private final String currencyAPI = "http://api.nbp.pl/api/exchangerates/rates/a/";

    public String getTodaysCurrencyInformation(String currencyCode) throws MalformedURLException{
        APIConnector apiConnectorCurrency = new APIConnector(currencyAPI);

        JSONObject jsonData = apiConnectorCurrency.getJSONObject(currencyCode.toLowerCase(Locale.ROOT));
        String currencyValue = jsonData.get("rates").toString().substring(33, 39);

        return currencyValue;
    }

    public JSONObject GetTodaysCurrencyInformation(String woeid) throws MalformedURLException {
        APIConnector apiConnectorCurrency = new APIConnector(currencyAPI);

        JSONObject weatherJSONObject = apiConnectorCurrency.getJSONObject(woeid + "/");

        JSONArray currencyArray = (JSONArray) weatherJSONObject.get("consolidated_weather");

        return  (JSONObject) currencyArray.get(0);
    }

}
