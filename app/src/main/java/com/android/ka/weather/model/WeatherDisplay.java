package com.android.ka.weather.model;

/**
 * Created by ka on 27/11/2016.
 */

public class WeatherDisplay {
    private int weatherId;
    private String temp;
    private String tempAva;
    private String city;
    private String timeForecast;
    private String status;
    private String wind;
    private String humidity;

    public WeatherDisplay() {

    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public String getTempAva() {
        return tempAva;
    }

    public void setTempAva(String tempAva) {
        this.tempAva = tempAva;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTimeForecast() {
        return timeForecast;
    }

    public void setTimeForecast(String timeForecast) {
        this.timeForecast = timeForecast;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
