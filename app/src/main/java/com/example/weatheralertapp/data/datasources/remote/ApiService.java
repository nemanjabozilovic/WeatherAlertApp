package com.example.weatheralertapp.data.datasources.remote;

public class ApiService {
    private static IOpenWeatherMapService weatherApi;
    public static IOpenWeatherMapService getWeatherApi() {
        if (weatherApi == null) {
            weatherApi = RetrofitClient.getWeatherApiRetrofitInstance().create(IOpenWeatherMapService.class);
        }

        return weatherApi;
    }
}