package com.android.ka.weather.api.service;

import com.android.ka.weather.model.DataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ka on 26/11/2016.
 */

public interface WeatherService {

    @GET("forecast")
    Call<DataResponse> getDataResponse(@Query("appid") String key,
                                       @Query("id") String cityId);
}
