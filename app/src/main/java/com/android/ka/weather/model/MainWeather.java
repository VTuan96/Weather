package com.android.ka.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ka on 26/11/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MainWeather {
    public double temp;
    @JsonProperty("temp_min")
    public double tempMin;
    @JsonProperty("temp_max")
    public double tempMax;
    public int humidity;
}
