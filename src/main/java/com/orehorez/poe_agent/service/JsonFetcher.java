package com.orehorez.poe_agent.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JsonFetcher {
    public static String getJsonFromUrl(String url) throws Exception {
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
}