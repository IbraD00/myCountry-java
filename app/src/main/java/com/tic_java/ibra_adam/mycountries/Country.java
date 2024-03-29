package com.tic_java.ibra_adam.mycountries;

import android.util.ArrayMap;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by IbraD00 & Adm94 on 28/04/2016.
 */
public class Country {
    private final OkHttpClient client = new OkHttpClient();
    protected static CountryType[] data;
    private final Gson gson = new Gson();

    public Map<String, ArrayList<CountryType>> run(String region) throws Exception {
        Request request = new Request.Builder()
                .url("https://restcountries-v1.p.mashape.com/all")
                .header("X-Mashape-Key", "BD4xOiVqPumshYypkX11bqtcVsiap1h1abZjsnShGKIuzED3cY")
                .addHeader("Accept", "application/json;")
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();

                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                Reader stream = response.body().charStream();
                Country.data = gson.fromJson(stream, CountryType[].class);
            }
        });

        Map<String, ArrayList<CountryType>> response = this.getRegion(Country.data);

        return response;
    }

    public Map<String, ArrayList<CountryType>> getRegion(CountryType[] data)
    {
        Map<String, ArrayList<CountryType>> region = new ArrayMap<>();

        ArrayList americas = new ArrayList<>();
        ArrayList asia = new ArrayList<>();
        ArrayList europe = new ArrayList<>();
        ArrayList africa = new ArrayList<>();
        ArrayList oceania = new ArrayList<>();

        for (CountryType item : data) {
            switch (item.region) {
                case "Americas":
                    americas.add(item);
                    break;
                case "Asia":
                    asia.add(item);
                    break;
                case "Europe":
                    europe.add(item);
                    break;
                case "Africa":
                    africa.add(item);
                    break;
                case "Oceania":
                    oceania.add(item);
                    break;
            }
        }

        region.put("Asia", asia);
        region.put("Americas", americas);
        region.put("Europe", europe);
        region.put("Africa", africa);
        region.put("Oceania", oceania);

        return region;
    }

    static class CountryType {
        String name;
        String capital;
        String region;
        String population;
    }
}
