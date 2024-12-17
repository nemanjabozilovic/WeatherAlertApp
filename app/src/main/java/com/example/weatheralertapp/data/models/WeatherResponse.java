package com.example.weatheralertapp.data.models;

import java.util.List;

public class WeatherResponse {
    private Coord coord;
    private List<Weather> weather;
    private String base;
    private Main main;
    private int visibility;
    private Wind wind;
    private Clouds clouds;
    private long dt;
    private Sys sys;
    private int timezone;
    private long id;
    private String name;
    private int cod;

    public Coord getCoord() {
        return coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public int getVisibility() {
        return visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public long getDt() {
        return dt;
    }

    public Sys getSys() {
        return sys;
    }

    public int getTimezone() {
        return timezone;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }

    public static class Coord {
        private double lon;
        private double lat;

        public double getLon() {
            return lon;
        }

        public double getLat() {
            return lat;
        }
    }

    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;

        public int getId() {
            return id;
        }

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }

    public static class Main {
        private float temp;
        private float feels_like;
        private float temp_min;
        private float temp_max;
        private int pressure;
        private int humidity;

        public float getTemp() {
            return temp;
        }

        public float getFeelsLike() {
            return feels_like;
        }

        public float getTempMin() {
            return temp_min;
        }

        public float getTempMax() {
            return temp_max;
        }

        public int getPressure() {
            return pressure;
        }

        public int getHumidity() {
            return humidity;
        }
    }

    public static class Wind {
        private float speed;
        private int deg;

        public float getSpeed() {
            return speed;
        }

        public int getDeg() {
            return deg;
        }
    }

    public static class Clouds {
        private int all;

        public int getAll() {
            return all;
        }
    }

    public static class Sys {
        private int type;
        private long id;
        private float message;
        private String country;
        private long sunrise;
        private long sunset;

        public int getType() {
            return type;
        }

        public long getId() {
            return id;
        }

        public float getMessage() {
            return message;
        }

        public String getCountry() {
            return country;
        }

        public long getSunrise() {
            return sunrise;
        }

        public long getSunset() {
            return sunset;
        }
    }
}