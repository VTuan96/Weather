package com.android.ka.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ka on 27/11/2016.
 */

public class WeatherDisplay implements Parcelable {
    public static final Creator<WeatherDisplay> CREATOR = new Creator<WeatherDisplay>() {
        @Override
        public WeatherDisplay createFromParcel(Parcel in) {
            return new WeatherDisplay(in);
        }

        @Override
        public WeatherDisplay[] newArray(int size) {
            return new WeatherDisplay[size];
        }
    };
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

    protected WeatherDisplay(Parcel in) {
        weatherId = in.readInt();
        temp = in.readString();
        tempAva = in.readString();
        city = in.readString();
        timeForecast = in.readString();
        status = in.readString();
        wind = in.readString();
        humidity = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(weatherId);
        dest.writeString(temp);
        dest.writeString(tempAva);
        dest.writeString(city);
        dest.writeString(timeForecast);
        dest.writeString(status);
        dest.writeString(wind);
        dest.writeString(humidity);
    }
}
