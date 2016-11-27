package com.android.ka.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ka on 26/11/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Wind {
    public double speed;
    public float deg;
}
