package com.android.ka.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ka on 29/11/2016.
 */

public class Country implements Parcelable {
    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
    private String _id;
    private String name;
    private String country;

    public Country() {

    }

    protected Country(Parcel in) {
        _id = in.readString();
        name = in.readString();
        country = in.readString();
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

    @Override
    public String toString() {
        return getName() + ", " + getCountry();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(name);
        dest.writeString(country);
    }
}
