package com.example.weatheralertapp.data.broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.example.weatheralertapp.R;
import com.example.weatheralertapp.ui.activities.MainActivity;

public class WeatherBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case "ACTION_SUNRISE":
                    showNotification(context, "The sun is rising. Good morning!");
                    break;
                case "ACTION_SUNSET":
                    showNotification(context, "The sun is setting. It's getting dark.");
                    break;
                case "ACTION_TEMP_UP":
                    showNotification(context, "The temperature is rising. Stay cool!");
                    break;
                case "ACTION_TEMP_DOWN":
                    showNotification(context, "The temperature is dropping. Bundle up!");
                    break;
                case "ACTION_RAIN":
                    showNotification(context, "Rain is starting. Take an umbrella!");
                    break;
                case "ACTION_WEATHER_UPDATE":
                    String city = intent.getStringExtra("city");
                    float temp = intent.getFloatExtra("temp", -1);
                    String weatherCondition = intent.getStringExtra("condition");

                    TextView tvWeatherInfo = ((MainActivity) context).findViewById(R.id.tv_weather_info);
                    if (tvWeatherInfo != null) {
                        String weatherInfo =
                                "City: " + city + ", " +
                                "Temperature: " +  temp + "°C, " +
                                "Weather Condition: " + weatherCondition;
                        tvWeatherInfo.setText(weatherInfo);
                    }
                    break;
            }
        }
    }

    private void showNotification(Context context, String message) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "weatherChannel")
                .setSmallIcon(R.drawable.ic_weather)
                .setContentTitle("Weather Alert")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify(1, builder.build());
    }
}