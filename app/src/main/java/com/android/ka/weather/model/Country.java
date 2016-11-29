package com.android.ka.weather.model;

/**
 * Created by ka on 29/11/2016.
 */

public class Country {
    private String _id;
    private String name;
    private String country;

    public Country() {

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
