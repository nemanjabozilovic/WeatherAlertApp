package com.example.weatheralertapp.data.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Base64;

import androidx.core.app.NotificationCompat;

import com.example.weatheralertapp.R;
import com.example.weatheralertapp.data.datasources.remote.ApiService;
import com.example.weatheralertapp.data.models.WeatherResponse;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherService extends Service {
    private String apiKey;
    private boolean notifySunrise, notifySunset, notifyTempChange, notifyRain, notifyStrongWind, notifySnow;
    private float lastTemp = -1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String encodedApiKey = getString(R.string.weather_api_key);
        byte[] decodedBytes = Base64.decode(encodedApiKey, Base64.DEFAULT);
        apiKey = new String(decodedBytes);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notifySunrise = intent.getBooleanExtra("sunrise", false);
        notifySunset = intent.getBooleanExtra("sunset", false);
        notifyTempChange = intent.getBooleanExtra("temperature_change", false);
        notifyRain = intent.getBooleanExtra("rain", false);
        notifyStrongWind = intent.getBooleanExtra("strong_wind", false);
        notifySnow = intent.getBooleanExtra("snow", false);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, WeatherService.class);
        PendingIntent pendingIntent =
                PendingIntent.getService(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        long triggerAt = SystemClock.elapsedRealtime() + interval;
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAt, interval, pendingIntent);

        getWeatherData("Belgrade");

        return START_STICKY;
    }

    public void getWeatherData(String cityName) {
        Call<WeatherResponse> call = ApiService.getWeatherApi().getCurrentWeather(cityName, apiKey, "metric");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherData = response.body();
                    handleWeatherConditions(weatherData);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendWeatherUpdateBroadcast(String city, float temp, String condition) {
        Intent intent = new Intent("ACTION_WEATHER_UPDATE");
        intent.putExtra("city", city);
        intent.putExtra("temp", temp);
        intent.putExtra("condition", condition);

        sendBroadcast(intent);
    }

    private void handleWeatherConditions(WeatherResponse weather) {
        LocalDateTime currentTime = LocalDateTime.now();

        sendWeatherUpdateBroadcast(weather.getName(), weather.getMain().getTemp(), weather.getWeather().get(0).getDescription());

        if (notifySunrise) {
            long sunriseTimestamp = weather.getSys().getSunrise();
            LocalDateTime sunriseDateTime = Instant.ofEpochSecond(sunriseTimestamp)
                    .atZone(ZoneOffset.UTC)
                    .toLocalDateTime();

            if (isTimeWithinRange(sunriseDateTime, currentTime)) {
                sendNotification("The sun is rising. Good morning!");
            }
        }

        if (notifySunset) {
            long sunsetTimestamp = weather.getSys().getSunset();
            LocalDateTime sunsetDateTime = Instant.ofEpochSecond(sunsetTimestamp)
                    .atZone(ZoneOffset.UTC)
                    .toLocalDateTime();

            if (isTimeWithinRange(sunsetDateTime, currentTime)) {
                sendNotification("The sun is setting. It's getting dark.");
            }
        }

        if (notifyRain) {
            for (WeatherResponse.Weather w : weather.getWeather()) {
                if (w.getMain().equalsIgnoreCase("Rain")) {
                    sendNotification("Rain is starting. Take an umbrella!");
                    break;
                }
            }
        }
        if (notifySnow) {
            for (WeatherResponse.Weather w : weather.getWeather()) {
                if (w.getMain().equalsIgnoreCase("Snow")) {
                    sendNotification("Snow is coming. Stay warm!");
                    break;
                }
            }
        }
        if (notifyTempChange) {
            float currentTemp = weather.getMain().getTemp();
            if (lastTemp != -1 && Math.abs(currentTemp - lastTemp) >= 3) {
                sendNotification("Temperature changed significantly. It's now " + currentTemp + "°C.");
            }
            lastTemp = currentTemp;
        }
        if (notifyStrongWind && weather.getWind().getSpeed() >= 10) {
            sendNotification("Strong wind detected. Be cautious!");
        }
    }

    private boolean isTimeWithinRange(LocalDateTime targetTime, LocalDateTime currentTime) {
        return Math.abs(ChronoUnit.MINUTES.between(currentTime, targetTime)) <= 10;
    }

    private void sendNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "weatherChannel")
                        .setSmallIcon(R.drawable.ic_weather)
                        .setContentTitle("Weather Alert")
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}