
package com.example.weatheralertapp.ui.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatheralertapp.R;
import com.example.weatheralertapp.data.broadcast.WeatherBroadcastReceiver;
import com.example.weatheralertapp.data.services.WeatherService;

public class MainActivity extends AppCompatActivity {

    private Button startServiceButton, stopServiceButton;
    private CheckBox cbSunriseTime, cbSunsetTime, cbTemperatureChange, cbRain, cbStrongWind, cbSnow;
    private TextView tvWeatherInfo;

    private WeatherBroadcastReceiver weatherReceiver = new WeatherBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "weatherChannel",
                    "Weather Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("This channel is used for weather alerts notifications.");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        initializeUIElements();
        setupListeners();
    }

    private void initializeUIElements() {
        startServiceButton = findViewById(R.id.btn_start_service);
        stopServiceButton = findViewById(R.id.btn_stop_service);
        tvWeatherInfo = findViewById(R.id.tv_weather_info);

        cbSunriseTime = findViewById(R.id.cb_sunrise_time);
        cbSunsetTime = findViewById(R.id.cb_sunset_time);
        cbTemperatureChange = findViewById(R.id.cb_temperature_change);
        cbRain = findViewById(R.id.cb_rain);
        cbStrongWind = findViewById(R.id.cb_strong_wind);
        cbSnow = findViewById(R.id.cb_snow);
    }

    private void setupListeners() {
        startServiceButton.setOnClickListener(v -> startWeatherService());
        stopServiceButton.setOnClickListener(v -> stopWeatherService());
    }

    private void startWeatherService() {
        Intent intent = new Intent(MainActivity.this, WeatherService.class);
        intent.putExtra("sunrise", cbSunriseTime.isChecked());
        intent.putExtra("sunset", cbSunsetTime.isChecked());
        intent.putExtra("temperature_change", cbTemperatureChange.isChecked());
        intent.putExtra("rain", cbRain.isChecked());
        intent.putExtra("strong_wind", cbStrongWind.isChecked());
        intent.putExtra("snow", cbSnow.isChecked());
        startService(intent);
    }

    private void stopWeatherService() {
        Intent intent = new Intent(MainActivity.this, WeatherService.class);
        stopService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("ACTION_WEATHER_UPDATE");
        registerReceiver(weatherReceiver, filter, Context.RECEIVER_EXPORTED);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(weatherReceiver);
    }
}