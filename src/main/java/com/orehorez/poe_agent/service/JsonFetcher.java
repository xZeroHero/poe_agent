package com.orehorez.poe_agent.service;

import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter

public class JsonFetcher {
/*    @Value("${apiUrl}")
    private static String url;*/

    public static String getJsonFromUrl() throws Exception {
        String url = "https://poe.ninja/api/data/currencyoverview?league=Settlers&type=Currency";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Failed to retrieve JSON: " + response.code());
            }
            assert response.body() != null;
            return response.body().string();
        }
    }

/*    public static String fetchJsonFromApi() throws Exception {

        String apiUrl = "https://poe.ninja/api/data/currencyoverview?league=Settlers&type=Currency";
        String json = JsonFetcher.getJsonFromUrl(apiUrl);
        *//*        System.out.println("API response(RAW): " + json);*//*
        return json;
    }*/
}