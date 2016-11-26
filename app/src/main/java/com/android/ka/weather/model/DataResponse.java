package com.android.ka.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by ka on 26/11/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataResponse {
    public City city;
    public String cod;
    public List<Forecast> list;
}
