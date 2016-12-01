package com.android.ka.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ka on 26/11/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class City {
    @JsonProperty("id")
    public String _id;
    public String name;
    public String country;
}
