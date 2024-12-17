package com.example.weatheralertapp.data.datasources.remote;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;
    private static final String WeatherApiBaseUrl = "https://api.openweathermap.org/data/2.5/";

    private RetrofitClient() {
    }

    public static Retrofit getInstance(String baseUrl) {
        if (instance != null) {
            return instance;
        }

        synchronized (RetrofitClient.class) {
            if (instance == null) {
                instance = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }

        return instance;
    }

    public static Retrofit getWeatherApiRetrofitInstance() {
        return  getInstance(WeatherApiBaseUrl);
    }
}
