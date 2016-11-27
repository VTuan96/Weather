package com.android.ka.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by ka on 26/11/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Forecast {
    @JsonProperty("dt")
    public long timeForecast;
    public MainWeather main;
    public List<Weather> weather;
    public Wind wind;
}
