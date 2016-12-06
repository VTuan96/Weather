package com.android.ka.weather.api.service;

import com.android.ka.weather.model.DataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ka on 26/11/2016.
 */

public interface ApiWeatherService {

    @GET("forecast")
    Call<DataResponse> getDataByCityId(@Query("appid") String key,
                                       @Query("id") String cityId);

    @GET("forecast")
    Call<DataResponse> getDataByLocation(@Query("appid") String key,
                                         @Query("lat") String lat,
                                         @Query("lon") String lon);
}
